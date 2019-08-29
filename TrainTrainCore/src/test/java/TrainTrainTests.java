import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.traintrain.IProvideBookingRef;
import com.traintrain.IProvideTrainTopology;
import com.traintrain.Reservation;
import com.traintrain.ReservationAdapter;
import com.traintrain.SeatRequestCount;
import com.traintrain.Train;
import com.traintrain.TrainDataServiceAdapter;
import com.traintrain.TrainId;
import com.traintrain.TrainTopology;
import com.traintrain.WebTicketManager;
import java.io.IOException;
import org.junit.Test;

public class TrainTrainTests {

  private final TrainTopology trainTopology = new TrainTopology();

  @Test
  public void should_reserved_3_seats_when_the_train_is_empty() throws IOException {
    IProvideTrainTopology topology = mock(IProvideTrainTopology.class);
    TrainId trainId = new TrainId("express_2000");
    given(topology.getTrain(trainId)).willReturn(
        new Train(
            trainId, TrainDataServiceAdapter.adaptTrainTopology(
            trainTopology.buildTrainWithEmptyCoachWithTenSeats()
        )
        )
    );

    IProvideBookingRef bookingRef = mock(IProvideBookingRef.class);
    given(bookingRef.getBookingRef()).willReturn("75bcd15");

    WebTicketManager webTicketManager = new WebTicketManager(topology, bookingRef);
    Reservation reservation = webTicketManager.reserve(trainId, new SeatRequestCount(3));
    assertThat(new ReservationAdapter().adaptReservation(reservation)).isEqualTo(
        "{\"train_id\": \"express_2000\", "
            + "\"booking_reference\": \"75bcd15\", "
            + "\"seats\": [\"1A\", \"2A\", \"3A\"]}");
  }

  @Test
  public void should_return_failure_when_the_request_has_more_than_70_percent_of_the_train()
      throws InterruptedException, IOException {
    IProvideTrainTopology topology = mock(IProvideTrainTopology.class);
    TrainId trainId = new TrainId("express_2000");
    given(topology.getTrain(trainId)).willReturn(

        new Train(
            trainId, TrainDataServiceAdapter.adaptTrainTopology(
            trainTopology.buildTrainWithEmptyCoachWithTenSeats())
        )
    );

    IProvideBookingRef bookingRef = mock(IProvideBookingRef.class);
    given(bookingRef.getBookingRef()).willReturn("75bcd15");

    WebTicketManager webTicketManager = new WebTicketManager(topology, bookingRef);
    Reservation reservation = webTicketManager.reserve(trainId, new SeatRequestCount(8));

    assertThat(new ReservationAdapter().adaptReservation(reservation)).isEqualTo(
        "{\"train_id\": \"express_2000\", "
            + "\"booking_reference\": \"\", "
            + "\"seats\": []}");
  }

  @Test
  public void should_return__when_the_request_has_more_than_50_percent_of_each_coach()
      throws InterruptedException, IOException {

    IProvideTrainTopology topology = mock(IProvideTrainTopology.class);
    TrainId trainId = new TrainId("express_2000");
    given(topology.getTrain(trainId)).willReturn(

        new Train(
            trainId, TrainDataServiceAdapter.adaptTrainTopology(
            trainTopology.buildTrainWith2CoachWithSeatsLessThan70Percent())
        )
    );

    IProvideBookingRef bookingRef = mock(IProvideBookingRef.class);
    given(bookingRef.getBookingRef()).willReturn("75bcd15");

    WebTicketManager webTicketManager = new WebTicketManager(topology, bookingRef);
    Reservation reservation = webTicketManager.reserve(trainId, new SeatRequestCount(8));

    assertThat(new ReservationAdapter().adaptReservation(reservation)).isEqualTo(
        "{\"train_id\": \"express_2000\", "
            + "\"booking_reference\": \"\", "
            + "\"seats\": []}");
  }

  @Test
  public void should_return_failure_when_the_request_has_more_than_70_percent_of_a_coach()
      throws InterruptedException, IOException {

    IProvideTrainTopology topology = mock(IProvideTrainTopology.class);
    TrainId trainId = new TrainId("express_2000");
    given(topology.getTrain(trainId)).willReturn(

        new Train(
            trainId, TrainDataServiceAdapter.adaptTrainTopology(
            trainTopology.buildTrainWith2CoachWithSeatsLessThan70Percent())
        )
    );

    IProvideBookingRef bookingRef = mock(IProvideBookingRef.class);
    given(bookingRef.getBookingRef()).willReturn("75bcd15");

    WebTicketManager webTicketManager = new WebTicketManager(topology, bookingRef);
    Reservation reservation = webTicketManager.reserve(trainId, new SeatRequestCount(8));

    assertThat(new ReservationAdapter().adaptReservation(reservation)).isEqualTo(
        "{\"train_id\": \"express_2000\", "
            + "\"booking_reference\": \"\", "
            + "\"seats\": []}");
  }
}
