package com.traintrain;

import java.util.List;

public class ReservationAdapter {

  public ReservationAdapter() {
  }

  public String adaptReservation(Reservation reservation) {
    return String.format(
        "{\"train_id\": \"%s\", \"booking_reference\": \"%s\", \"seats\": %s}",
        reservation.getTrainId().getId(),
        reservation.getBookingRef(),
        dumpSeats(reservation.getSeats()));
  }

  String dumpSeats(List<Seat> seats) {
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
}