package com.github.joao_felisberto.microservice.service;

import com.github.joao_felisberto.microservice.service.api.dto.ClientDTO;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Service for the {@link com.github.joao_felisberto.microservice.web.rest.MiscController}.
 * </p>
 * Deals with the business logic of the endpoints.
 */
@Service
public class MiscService {

    /**
     * Creates a client by sending a request to the endpoint to create clients.
     * This endpoint was created to prove the correctness of the SSL implementation.
     *
     * @param clientDTO the client to create
     * @return the created client
     * @throws URISyntaxException     when the URI of the client creation endpoint is incorrect. Should be unreachable
     * @throws ErrorResponseException when the client creation returned an error status code. See {@link com.github.joao_felisberto.microservice.web.rest.ClientController#postClient(ClientDTO)}
     */
    public ClientDTO createClientWithHTTPSCall(ClientDTO clientDTO) throws URISyntaxException, ErrorResponseException {
        final URI uri = new URI("https://localhost:8081/api/client");
        return RestClient.create()
            .post()
            .uri(uri)
            .body(clientDTO)
            .retrieve()
            .onStatus(HttpStatusCode::isError, (request, response) -> {
                throw new ErrorResponseException(response.getStatusCode(), null);
            })
            .body(ClientDTO.class);
    }
}
