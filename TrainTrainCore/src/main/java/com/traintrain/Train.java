package com.traintrain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Train {

  private TrainId trainId;
  private final Map<String, Coach> mapOfCoach;

  public Train(TrainId trainId, List<Seat> seats) {
    this.trainId = trainId;
    mapOfCoach = new HashMap<>();

    for (Seat seat : seats) {

      if (!mapOfCoach.containsKey(seat.getCoachName())) {
        Coach coach = new Coach();
        coach.addSeat(seat);
        mapOfCoach.put(seat.getCoachName(), coach);
      } else {
        Coach coach = mapOfCoach.get(seat.getCoachName());
        coach.addSeat(seat);
      }
    }
  }

  public int getMaxSeat() {
    return this.getSeats().size();
  }

  private List<Seat> getSeats() {
    return mapOfCoach.values().stream()
        .map(Coach::getSeats)
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }

  public boolean hasLessThanThreshold(int i) {
    return getReservedSeats() < i;
  }

  public int getReservedSeats() {
    return (int) this.getSeats().stream().filter(seat -> !"".equals(seat.getBookingRef())).count();
  }

  boolean isReservationAllowed(SeatRequestCount seatRequestedCount) {
    return (getReservedSeats() + seatRequestedCount.getSeatCount()) <= Math
        .floor(ThresholdManager.getMaxRes() * getMaxSeat());
  }

  List<Seat> findAvailableSeats(int seatRequestedCount) {

    List<Seat> availableSeats = new ArrayList<>();

    for (Coach coach : mapOfCoach.values()) {
      if (coach.isReservationAllowed(seatRequestedCount)) {
        availableSeats.addAll(coach.findAvailableSeats(seatRequestedCount));
      }
    }

    return availableSeats;
  }

  ReservationAttempt buildReservationAttempt(TrainId trainId, SeatRequestCount seatRequestedCount) {
    return new ReservationAttempt(trainId,
            findAvailableSeats(seatRequestedCount.getSeatCount()), seatRequestedCount);
  }

  public List<Coach> getCoaches() {
    return new ArrayList<>(mapOfCoach.values());
  }
}