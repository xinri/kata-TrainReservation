import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.traintrain.IProvideBookingRef;
import com.traintrain.IProvideTrainTopology;
import com.traintrain.Train;
import com.traintrain.TrainDataServiceAdapter;
import com.traintrain.WebTicketManager;
import java.io.IOException;
import org.junit.Test;

public class TrainTrainTests {

  @Test
  public void should_reserved_3_seats_when_the_train_is_empty()
      throws InterruptedException, IOException {
    IProvideTrainTopology topology = mock(IProvideTrainTopology.class);
    given(topology.getTrain("express_2000")).willReturn(
        new Train(
            TrainDataServiceAdapter.adaptTrainTopology(
                "{\"seats\": "
                    + "{\"1A\": {\"booking_reference\": \"\", \"seat_number\": \"1\", \"coach\": \"A\"}, "
                    + "\"2A\": {\"booking_reference\": \"\", \"seat_number\": \"2\", \"coach\": \"A\"}, "
                    + "\"3A\": {\"booking_reference\": \"\", \"seat_number\": \"3\", \"coach\": \"A\"}, "
                    + "\"4A\": {\"booking_reference\": \"\", \"seat_number\": \"4\", \"coach\": \"A\"}, "
                    + "\"5A\": {\"booking_reference\": \"\", \"seat_number\": \"5\", \"coach\": \"A\"}, "
                    + "\"6A\": {\"booking_reference\": \"\", \"seat_number\": \"6\", \"coach\": \"A\"}, "
                    + "\"7A\": {\"booking_reference\": \"\", \"seat_number\": \"7\", \"coach\": \"A\"}, "
                    + "\"8A\": {\"booking_reference\": \"\", \"seat_number\": \"8\", \"coach\": \"A\"}, "
                    + "\"9A\": {\"booking_reference\": \"\", \"seat_number\": \"9\", \"coach\": \"A\"}, "
                    + "\"10A\": {\"booking_reference\": \"\", \"seat_number\": \"10\", \"coach\": \"A\"}"
                    + "}}"
            )
        )
    );

    IProvideBookingRef bookingRef = mock(IProvideBookingRef.class);
    given(bookingRef.getBookingRef()).willReturn("75bcd15");

    WebTicketManager webTicketManager = new WebTicketManager(topology, bookingRef);
    String reservation = webTicketManager.reserve("express_2000", 3);
    assertThat(reservation).isEqualTo(
        "{\"train_id\": \"express_2000\", "
            + "\"booking_reference\": \"75bcd15\", "
            + "\"seats\": [\"1A\", \"2A\", \"3A\"]}");
  }

  @Test
  public void should_return_failure_when_the_request_has_more_than_70_percent_of_the_train()
      throws InterruptedException, IOException {
    IProvideTrainTopology topology = mock(IProvideTrainTopology.class);
    given(topology.getTrain("express_2000")).willReturn(

        new Train(
            TrainDataServiceAdapter.adaptTrainTopology(
                "{\"seats\": "
                    + "{\"1A\": {\"booking_reference\": \"\", \"seat_number\": \"1\", \"coach\": \"A\"}, "
                    + "\"2A\": {\"booking_reference\": \"\", \"seat_number\": \"2\", \"coach\": \"A\"}, "
                    + "\"3A\": {\"booking_reference\": \"\", \"seat_number\": \"3\", \"coach\": \"A\"}, "
                    + "\"4A\": {\"booking_reference\": \"\", \"seat_number\": \"4\", \"coach\": \"A\"}, "
                    + "\"5A\": {\"booking_reference\": \"\", \"seat_number\": \"5\", \"coach\": \"A\"}, "
                    + "\"6A\": {\"booking_reference\": \"\", \"seat_number\": \"6\", \"coach\": \"A\"}, "
                    + "\"7A\": {\"booking_reference\": \"\", \"seat_number\": \"7\", \"coach\": \"A\"}, "
                    + "\"8A\": {\"booking_reference\": \"\", \"seat_number\": \"8\", \"coach\": \"A\"}, "
                    + "\"9A\": {\"booking_reference\": \"\", \"seat_number\": \"9\", \"coach\": \"A\"}, "
                    + "\"10A\": {\"booking_reference\": \"\", \"seat_number\": \"10\", \"coach\": \"A\"}"
                    + "}}"
            )
        )
    );

    IProvideBookingRef bookingRef = mock(IProvideBookingRef.class);
    given(bookingRef.getBookingRef()).willReturn("75bcd15");

    WebTicketManager webTicketManager = new WebTicketManager(topology, bookingRef);
    String reservation = webTicketManager.reserve("express_2000", 8);

    assertThat(reservation).isEqualTo(
        "{\"train_id\": \"express_2000\", "
            + "\"booking_reference\": \"\", "
            + "\"seats\": []}");
  }
}
