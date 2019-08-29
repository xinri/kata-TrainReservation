package com.traintrain;

import java.util.Objects;

/**
 * @author hlay
 * @version 1.0
 */
public class TrainId {

  private String id;

  public TrainId(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof TrainId)) {
      return false;
    }
    TrainId trainId = (TrainId) o;
    return Objects.equals(id, trainId.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
