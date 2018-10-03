package com.traintrain;

public class TrainCaching implements ITrainCaching {
    TrainEntity trainEntity;

    public void Clear() {
        if (trainEntity != null) trainEntity.Seats.clear();
    }

    public void Save(String trainId, Train train, String bookingReference) throws InterruptedException {
        trainEntity = new TrainEntity(trainId);
        for (Seat seat : train.Seats) {
            trainEntity.Seats.add(new SeatEntity(trainId, bookingReference, seat.getCoachName(), seat.getSeatNumber()));
        }
        Factory.Create().Save(trainEntity);
    }

    public void Save(TrainEntity trainEntity) throws InterruptedException {
        Thread.sleep(500);
    }
}

final class Factory {
    public static ITrainCaching Create() {
        return new TrainCaching();
    }
}