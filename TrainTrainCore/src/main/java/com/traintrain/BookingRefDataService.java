package com.traintrain;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class BookingRefDataService implements IProvideBookingRef {

  private final String uriBookingReferenceService;

  public BookingRefDataService(String uriBookingReferenceService) {
    this.uriBookingReferenceService = uriBookingReferenceService;
  }

  @Override
  public String getBookingRef() {
    String bookingRef;
    Client client = ClientBuilder.newClient();
    try {
      bookingRef = getBookRef(client);
    } finally {
      client.close();
    }
    return bookingRef;
  }

  private String getBookRef(Client client) {
    String bookingRef;

    WebTarget target = client
        .target(uriBookingReferenceService + "/booking_reference/");
    bookingRef = target.request(MediaType.APPLICATION_JSON).get(String.class);

    return bookingRef;
  }
}