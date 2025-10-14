package com.github.joao_felisberto.microservice.service;

import com.github.joao_felisberto.microservice.service.api.dto.ClientDTO;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Service;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.net.URISyntaxException;

@Service
public class MiscService {

    public ClientDTO createClientWithHTTPSCall(ClientDTO clientDTO) throws URISyntaxException, ErrorResponseException {
        final URI uri = new URI("https://localhost:8081/api/client");
        return RestClient.create()
            .post()
            .uri(uri)
            .body(clientDTO)
            .retrieve()
            .onStatus(HttpStatusCode::isError, (request, response) -> {
                final ProblemDetail detail = ProblemDetail.forStatus(response.getStatusCode());
//                    detail.setInstance(uri);
//                    detail.setTitle("Error creating Client");
//                    detail.setProperty("request", request);
//                    detail.setProperty("response", response);

                throw new ErrorResponseException(response.getStatusCode(), detail, null);
            })
            .body(ClientDTO.class);
    }
}
