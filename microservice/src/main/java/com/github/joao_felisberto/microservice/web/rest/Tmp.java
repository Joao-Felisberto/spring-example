package com.github.joao_felisberto.microservice.web.rest;

import com.github.joao_felisberto.microservice.domain.Client;
import com.github.joao_felisberto.microservice.service.ClientQueryService;
import com.github.joao_felisberto.microservice.service.api.dto.ClientDTO;
import com.github.joao_felisberto.microservice.service.criteria.ClientCriteria;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/")
@Transactional
public class Tmp {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final Logger LOG = LoggerFactory.getLogger(Tmp.class);
    private final ClientQueryService clientQueryService;

    public Tmp(ClientQueryService clientQueryService) {
        this.clientQueryService = clientQueryService;
    }

    /**
     * A shortcut to create a client
     * Created to prove a correct SSL implementation, since there is no use case for a microservice calling
     * itself over HTTPS, like load balancing.
     *
     * @param clientDTO The client to add
     * @return The added client
     */
    @PostMapping("/shortcut")
    public ResponseEntity<ClientDTO> shortcut(@Valid @RequestBody ClientDTO clientDTO) /*throws URISyntaxException*/ {
        return restTemplate.postForEntity("https://localhost:8081/api/clients", clientDTO, ClientDTO.class);
//        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/api/client/filter")
    public ResponseEntity<List<Client>> getAllClients(ClientCriteria criteria) {
        LOG.debug("REST request to get Clients by criteria: {}", criteria);

        List<Client> entityList = clientQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }
}
