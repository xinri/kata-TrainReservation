package com.traintrain;

public interface ITrainCaching {
    void Clear();

    void Save(String trainId, Train train, String bookingReference) throws InterruptedException;

    void Save(TrainEntity trainEntity) throws InterruptedException;
}
