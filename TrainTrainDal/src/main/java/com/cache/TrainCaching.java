package com.cache;


public class TrainCaching implements ITrainCaching {

    @Override
    public void Clear() {

    }

    @Override
    public void Save(String trainId, String bookingReference) {

    }
}

final class Factory {
    public static ITrainCaching create() {
        return new TrainCaching();
    }
}