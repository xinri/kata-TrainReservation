package com.traintrain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hlay
 * @version 1.0
 */
public class Coach {

  private List<Seat> seats = new ArrayList<>();

  public List<Seat> getSeats() {
    return seats;
  }

  public void addSeat(Seat seat) {
    seats.add(seat);
  }


  public List<Seat> findAvailableSeats(int seatRequestedCount) {
    return seats.stream()
        .filter(seat -> seat.getBookingRef().equals(""))
        .limit(seatRequestedCount)
        .collect(Collectors.toList());
  }

  boolean isReservationAllowed(int seatRequestedCount) {
    return (getReservedSeats() + seatRequestedCount) <= Math
        .floor(ThresholdManager.getMaxRes() * getMaxSeat());
  }

  public int getReservedSeats() {
    return (int) this.getSeats().stream().filter(seat -> !"".equals(seat.getBookingRef())).count();
  }

  public int getMaxSeat() {
    return this.getSeats().size();
  }


}
