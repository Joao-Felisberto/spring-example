package com.github.joao_felisberto.microservice.web.rest;

import com.github.joao_felisberto.microservice.domain.Client;
import com.github.joao_felisberto.microservice.service.ClientQueryService;
import com.github.joao_felisberto.microservice.service.MiscService;
import com.github.joao_felisberto.microservice.service.api.dto.ClientDTO;
import com.github.joao_felisberto.microservice.service.criteria.ClientCriteria;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/")
@Transactional
public class MiscController {

    private static final Logger LOG = LoggerFactory.getLogger(MiscController.class);
    private final ClientQueryService clientQueryService;
    private final MiscService miscService;

    public MiscController(ClientQueryService clientQueryService, MiscService miscService) {
        this.clientQueryService = clientQueryService;
        this.miscService = miscService;
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
    public ResponseEntity<ClientDTO> shortcut(@Valid @RequestBody ClientDTO clientDTO) {
        try {
            final ClientDTO res = miscService.createClientWithHTTPSCall(clientDTO);

            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (ErrorResponseException e) {
            return new ResponseEntity<>(e.getStatusCode());
        } catch (URISyntaxException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/api/client/filter")
    public ResponseEntity<List<Client>> filterClients(ClientCriteria criteria) {
        LOG.debug("REST request to get Clients by criteria: {}", criteria);

        final List<Client> entityList = clientQueryService.findByCriteria(criteria);
        return new ResponseEntity<>(entityList, HttpStatus.OK);
    }
}
