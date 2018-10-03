package com.cache;

public interface ITrainCaching {
    void Clear();

    void Save(String trainId, String bookingReference) throws InterruptedException;
}
