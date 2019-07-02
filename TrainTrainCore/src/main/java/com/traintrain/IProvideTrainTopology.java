package com.traintrain;

import java.io.IOException;

/**
 * @author hlay
 * @version 1.0
 */
public interface IProvideTrainTopology {

  void sendReservation(String postContent);

  Train getTrain(String trainId) throws IOException;
}
