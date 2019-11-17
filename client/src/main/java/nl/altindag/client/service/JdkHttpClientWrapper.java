package nl.altindag.client.service;

import static nl.altindag.client.ClientType.JDK_HTTP_CLIENT;
import static nl.altindag.client.Constants.HEADER_KEY_CLIENT_TYPE;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nl.altindag.client.model.ClientResponse;

@Service
public class JdkHttpClientWrapper extends RequestService {

    private final HttpClient httpClient;

    @Autowired
    public JdkHttpClientWrapper(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public ClientResponse executeRequest(String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                                         .GET()
                                         .header(HEADER_KEY_CLIENT_TYPE, JDK_HTTP_CLIENT.getValue())
                                         .uri(URI.create(url))
                                         .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return new ClientResponse(response.body(), response.statusCode());
    }

}
