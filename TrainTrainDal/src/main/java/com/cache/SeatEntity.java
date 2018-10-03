package com.traintrain;

public class SeatEntity extends Seat {
    private final String trainId;

    public SeatEntity(String trainId, String bookingReference, String coachName, int seatNumber) {
        super(coachName, seatNumber, bookingReference);
        this.trainId = trainId;
    }
}
