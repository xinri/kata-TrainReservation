package com.traintrain;

import com.cache.ITrainCaching;
import com.cache.SeatEntity;
import com.cache.TrainCaching;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WebTicketManager {

  private static final String uriTrainDataService = "http://localhost:50680";
  private static final String uriBookingReferenceService = "http://localhost:51691";

  private ITrainCaching trainCaching;
  private IProvideTrainTopology trainTopology;
  private IProvideBookingRef bookingRefProvider;

  public WebTicketManager() throws InterruptedException {
    this(
        new TrainDataServiceAdapter(uriTrainDataService),
        new BookingRefDataService(uriBookingReferenceService)
    );
  }

  public WebTicketManager(IProvideTrainTopology trainTopology,
      IProvideBookingRef bookingRefProvider)
      throws InterruptedException {
    this.trainTopology = trainTopology;
    this.bookingRefProvider = bookingRefProvider;
    trainCaching = new TrainCaching();
    trainCaching.Clear();
  }

  private static String buildPostContent(String trainId, String booking_ref,
      List<Seat> availableSeats) {
    StringBuilder seats = new StringBuilder("[");

    boolean firstTime = true;

    for (Seat seat : availableSeats) {
      if (!firstTime) {
        seats.append(", ");
      } else {
        firstTime = false;
      }

      seats.append(String.format("\"%d%s\"", seat.getSeatNumber(), seat.getCoachName()));
    }

    seats.append("]");

    String result = String.format(
        "{{\r\n\t\"train_id\": \"%s\",\r\n\t\"seats\": %s,\r\n\t\"booking_reference\": \"%S\"\r\n}}",
        trainId, seats.toString(), booking_ref);

    return result;
  }

  public String reserve(String trainId, int seatRequestedCount)
      throws IOException {
    String bookingRef;

    // get the trainId
    Train train = trainTopology.getTrain(trainId);

    if (isReservationAllowed(seatRequestedCount, train)) {

      // find seatRequestedCount to reserve
      ReservationAttempt reservationAttempt =
          buildReservationAttempt(seatRequestedCount, train);

      if (reservationAttempt.isFulfilled()) {
        bookingRef = bookingRefProvider.getBookingRef();
        for (Seat availableSeat : reservationAttempt.getSeats()) {
          availableSeat.setBookingRef(bookingRef);
        }

        String postContent = buildPostContent(trainId, bookingRef, reservationAttempt.getSeats());

        trainTopology.sendReservation(postContent);

        return String.format(
            "{\"train_id\": \"%s\", \"booking_reference\": \"%s\", \"seats\": %s}",
            trainId,
            bookingRef,
            dumpSeats(reservationAttempt.getSeats()));
      }
    }

    return String
        .format("{\"train_id\": \"%s\", \"booking_reference\": \"\", \"seats\": []}",
            trainId);
  }

  private boolean isReservationAllowed(int seatRequestedCount, Train train) {
    return (train.getReservedSeats() + seatRequestedCount) <= Math
        .floor(ThresholdManager.getMaxRes() * train.getMaxSeat());
  }

  private ReservationAttempt buildReservationAttempt(int seatRequestedCount, Train train) {
    ArrayList<Seat> availableSeats = new ArrayList<>();
    for (int index = 0, i = 0; index < train.seats.size(); index++) {
      Seat seat = train.seats.get(index);
      if ("".equals(seat.getBookingRef())) {
        i++;
        if (i <= seatRequestedCount) {
          availableSeats.add(seat);
        }
      }
    }

    return new ReservationAttempt(availableSeats, seatRequestedCount);
  }

  private String dumpSeats(List<Seat> seats) {
    StringBuilder sb = new StringBuilder("[");

    boolean firstTime = true;

    for (Seat seat : seats) {
      if (!firstTime) {
        sb.append(", ");
      } else {
        firstTime = false;
      }

      sb.append(String.format("\"%d%s\"", seat.getSeatNumber(), seat.getCoachName()));
    }

    sb.append("]");

    return sb.toString();
  }

  private List<SeatEntity> toSeatsEntities(String train, List<Seat> availableSeats,
      String bookingRef) {
    List<SeatEntity> seatEntities = new ArrayList<>();
    for (Seat seat : availableSeats) {
      seatEntities
          .add(new SeatEntity(train, bookingRef, seat.getCoachName(), seat.getSeatNumber()));
    }
    return seatEntities;
  }
}
