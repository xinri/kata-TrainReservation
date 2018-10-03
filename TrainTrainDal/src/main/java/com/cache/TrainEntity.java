package com.traintrain;

import java.util.ArrayList;
import java.util.List;

public class TrainEntity {

    private final String trainId;
    public List<Seat> Seats = new ArrayList<Seat>();

    public TrainEntity(String trainId) {

        this.trainId = trainId;
    }

    public String getTrainId() {
        return trainId;
    }
}
