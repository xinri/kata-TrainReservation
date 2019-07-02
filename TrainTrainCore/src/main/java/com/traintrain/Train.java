package com.traintrain;

import java.util.List;

public class Train {

  public List<Seat> seats;

  public Train(List<Seat> seats) {
    this.seats = seats;
  }

  public int getMaxSeat() {
    return this.seats.size();
  }

  public boolean hasLessThanThreshold(int i) {
    return getReservedSeats() < i;
  }

  public int getReservedSeats() {
    return (int) this.seats.stream().filter(seat -> !"".equals(seat.getBookingRef())).count();
  }


}