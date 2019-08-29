package com.traintrain;


import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.Test;

public class TrainShould {

  @Test
  public void expose_coaches() throws IOException {
    TrainId trainId = new TrainId("express2000");
    Train train = new Train(trainId, TrainDataServiceAdapter
        .adaptTrainTopology(TrainTopology.buildTrainWithEmptyCoachWithTenSeats()));

    train.getCoaches().forEach(coach -> assertThat(coach.getSeats()).hasSize(10));
  }
}