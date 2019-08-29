package com.traintrain;

import java.io.IOException;

/**
 * @author hlay
 * @version 1.0
 */
public interface IProvideTrainTopology {

  void sendReservation(ReservationAttempt reservationAttempt);

  Train getTrain(TrainId trainId) throws IOException;
}
