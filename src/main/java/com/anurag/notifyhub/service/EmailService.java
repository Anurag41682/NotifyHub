package com.anurag.notifyhub.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService {

  @Value("${resend.api.key}")
  private String resendApiKey;

  public void sendEmail(String to, String subject, String body) throws IOException, InterruptedException {

    String jsonBody = String.format("""
        {
          "from": "notifications@agnomerf.store",
          "to" : "%s",
          "subject" : "%s",
          "html" : "%s"
        }
          """, to, subject, body);

    HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://api.resend.com/emails"))
        .header("Authorization", "Bearer " + resendApiKey).header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(jsonBody)).build();
    HttpClient client = HttpClient.newHttpClient();

    client.send(request, HttpResponse.BodyHandlers.ofString());
  }

}
