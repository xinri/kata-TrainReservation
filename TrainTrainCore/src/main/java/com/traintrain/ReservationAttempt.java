package com.traintrain;

import java.util.List;

/**
 * @author hlay
 * @version 1.0
 */
public class ReservationAttempt {

  private TrainId trainId;
  private final List<Seat> seats;
  private final SeatRequestCount seatRequestedCount;
  private String bookingRef;

  public ReservationAttempt(TrainId trainId, List<Seat> seats, SeatRequestCount seatRequestedCount) {
    this.trainId = trainId;
    this.seats = seats;
    this.seatRequestedCount = seatRequestedCount;
  }

  public List<Seat> getSeats() {
    return seats;
  }

  boolean isFulfilled() {
    return this.seatRequestedCount.getSeatCount() == getSeats().size();
  }

  void assignBookingReference(String bookingRef) {
    this.bookingRef = bookingRef;
    for (Seat availableSeat : getSeats()) {
      availableSeat.setBookingRef(bookingRef);
    }
  }

  public String getBookingRef() {
    return bookingRef;
  }

  public TrainId getTrainId() {
    return trainId;
  }

  public Reservation confirm() {
    return new Reservation(trainId, bookingRef, seats);
  }
}
