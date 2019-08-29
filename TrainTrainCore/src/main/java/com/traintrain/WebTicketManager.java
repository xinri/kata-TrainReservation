package com.traintrain;

import java.io.IOException;

public class WebTicketManager {

  private static final String uriTrainDataService = "http://localhost:50680";
  private static final String uriBookingReferenceService = "http://localhost:51691";

  private IProvideTrainTopology trainDataService;
  private IProvideBookingRef bookingRefProvider;

  public WebTicketManager() {
    this(
        new TrainDataServiceAdapter(uriTrainDataService),
        new BookingRefDataService(uriBookingReferenceService)
    );
  }

  public WebTicketManager(IProvideTrainTopology trainDataService,
      IProvideBookingRef bookingRefProvider) {
    this.trainDataService = trainDataService;
    this.bookingRefProvider = bookingRefProvider;
  }

  public Reservation reserve(TrainId trainId, SeatRequestCount seatRequestedCount)
      throws IOException {
    // get the trainId
    Train train = trainDataService.getTrain(trainId);

    if (train.isReservationAllowed(seatRequestedCount)) {

      // find seatRequestedCount to reserve
      ReservationAttempt reservationAttempt = train
          .buildReservationAttempt(trainId, seatRequestedCount);

      if (reservationAttempt.isFulfilled()) {
        reservationAttempt.assignBookingReference(
            bookingRefProvider.getBookingRef());

        trainDataService.sendReservation(reservationAttempt);

        return reservationAttempt.confirm();
      }
    }

    return new ReservationFailure(trainId);
  }
}
