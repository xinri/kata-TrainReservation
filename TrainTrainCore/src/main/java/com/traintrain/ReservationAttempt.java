package com.traintrain;

import java.util.List;

/**
 * @author hlay
 * @version 1.0
 */
public class ReservationAttempt {

  private final List<Seat> seats;
  private final int seatRequestedCount;

  public ReservationAttempt(List<Seat> seats, int seatRequestedCount) {
    this.seats = seats;
    this.seatRequestedCount = seatRequestedCount;
  }

  public List<Seat> getSeats() {
    return seats;
  }

  boolean isFulfilled() {
    return this.seatRequestedCount == getSeats().size();
  }
}
