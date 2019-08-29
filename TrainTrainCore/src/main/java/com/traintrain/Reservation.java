package com.traintrain;

import java.util.List;

/**
 * @author hlay
 * @version 1.0
 */
public class Reservation {

  private final TrainId trainId;
  private final String bookingRef;
  private final List<Seat> seats;

  public Reservation(TrainId trainId, String bookingRef, List<Seat> seats) {
    this.trainId = trainId;
    this.bookingRef = bookingRef;
    this.seats = seats;
  }

  public TrainId getTrainId() {
    return trainId;
  }

  public String getBookingRef() {
    return bookingRef;
  }

  public List<Seat> getSeats() {
    return seats;
  }
}
