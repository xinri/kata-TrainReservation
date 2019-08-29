package com.traintrain;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class TrainDataServiceAdapter implements IProvideTrainTopology {

  private String uriTrainDataService;

  public TrainDataServiceAdapter(String uriTrainDataService) {
    this.uriTrainDataService = uriTrainDataService;
  }

  public static List<Seat> adaptTrainTopology(String trainTopol) throws IOException {
    List<Seat> seats = new ArrayList<>();

    //var sample:
    //"{\"seats\": {\"1A\": {\"booking_reference\": \"\", \"seat_number\": \"1\", \"coach\": \"A\"}, \"2A\": {\"booking_reference\": \"\", \"seat_number\": \"2\", \"coach\": \"A\"}}}";
    final ObjectMapper objectMapper = new ObjectMapper();

    Map<String, Map<String, SeatJson>> stuff_in_stuff = objectMapper
        .readValue(trainTopol, new TypeReference<Map<String, Map<String, SeatJson>>>() {
        });

    for (Map<String, SeatJson> value : stuff_in_stuff.values()) {
      for (SeatJson seatJson : value.values()) {
        int seat_number = Integer.parseInt(seatJson.seat_number);
        seats.add(new Seat(seatJson.coach, seat_number, seatJson.booking_reference));
      }
    }
    return seats;
  }

  static String buildPostContent(ReservationAttempt reservationAttempt) {
    StringBuilder seats = new StringBuilder("[");

    boolean firstTime = true;

    for (Seat seat : reservationAttempt.getSeats()) {
      if (!firstTime) {
        seats.append(", ");
      } else {
        firstTime = false;
      }

      seats.append(String.format("\"%d%s\"", seat.getSeatNumber(), seat.getCoachName()));
    }

    seats.append("]");

    String result = String.format(
        "{{\r\n\t\"train_id\": \"%s\",\r\n\t\"seats\": %s,\r\n\t\"booking_reference\": \"%S\"\r\n}}",
        reservationAttempt.getTrainId(), seats.toString(), reservationAttempt.getBookingRef());

    return result;
  }

  @Override
  public void sendReservation(ReservationAttempt reservationAttempt) {
    Client client = ClientBuilder.newClient();
    try {
      WebTarget webTarget = client.target(uriTrainDataService + "/reserve/");
      Builder request = webTarget.request(MediaType.APPLICATION_JSON_TYPE);
      String postContent = buildPostContent(reservationAttempt);
      request.post(Entity.text(postContent));
    } finally {
      client.close();
    }
  }

  @Override
  public Train getTrain(TrainId trainId) throws IOException {
    String JsonTrainTopology;
    Client client = ClientBuilder.newClient();
    try {
      WebTarget target = client.target(uriTrainDataService + "/api/data_for_train/");
      WebTarget path = target.path(String.valueOf(trainId));
      Builder request = path.request(MediaType.APPLICATION_JSON);
      JsonTrainTopology = request.get(String.class);
    } finally {
      client.close();
    }
    List<Seat> seats = adaptTrainTopology(JsonTrainTopology);
    return new Train(trainId, seats);
  }
}