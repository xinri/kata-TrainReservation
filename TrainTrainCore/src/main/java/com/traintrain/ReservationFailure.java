package com.traintrain;

import java.util.Collections;

/**
 * @author hlay
 * @version 1.0
 */
public class ReservationFailure extends Reservation {

  public ReservationFailure(TrainId trainId) {
    super(trainId, "", Collections.emptyList());
  }
}
