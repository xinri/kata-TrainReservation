package trainTrain.api;

import com.traintrain.ReservationAdapter;
import com.traintrain.SeatRequestCount;
import com.traintrain.TrainId;
import com.traintrain.WebTicketManager;
import java.io.IOException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReservationsController {

  @RequestMapping(method = RequestMethod.POST, value = "api/reservations")
  public String update(@RequestBody RequestDto requestDto)
      throws IOException, InterruptedException {

    WebTicketManager webTicketManager = new WebTicketManager();
    ReservationAdapter reservationAdapter = new ReservationAdapter();

    return reservationAdapter.adaptReservation(
        webTicketManager.reserve(new TrainId(requestDto.getTrain_id()),
            new SeatRequestCount(requestDto.getNumber_of_seats())));
  }

  @RequestMapping(method = RequestMethod.GET, value = "api/value")
  public String get() {
    return "Reservations";
  }
}
