package com.github.joao_felisberto.microservice.web.rest;

import com.github.joao_felisberto.microservice.domain.Client;
import com.github.joao_felisberto.microservice.domain.mappers.ClientMapper;
import com.github.joao_felisberto.microservice.service.ClientQueryService;
import com.github.joao_felisberto.microservice.service.MiscService;
import com.github.joao_felisberto.microservice.service.api.dto.ClientDTO;
import com.github.joao_felisberto.microservice.service.criteria.ClientCriteria;
import jakarta.validation.Valid;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;

/**
 * Controller for miscellaneous endpoints
 */
@RestController
@RequestMapping("/")
@Transactional
public class MiscController {

    private static final Logger LOG = LoggerFactory.getLogger(MiscController.class);
    private final ClientMapper clientMapper = Mappers.getMapper(ClientMapper.class);

    private final ClientQueryService clientQueryService;
    private final MiscService miscService;

    /**
     * Constructor for the MiscController
     *
     * @param clientQueryService the service to query clients by criteria
     * @param miscService        the service of this controller
     */
    public MiscController(ClientQueryService clientQueryService, MiscService miscService) {
        this.clientQueryService = clientQueryService;
        this.miscService = miscService;
    }

    /**
     * {@code `/shortcut`}: A shortcut to create a client
     * Created to prove a correct SSL implementation, since there is no use case for a microservice calling
     * itself over HTTPS, like load balancing.
     *
     * @param clientDTO The client to add
     * @return The added client with HTTP status {@link HttpStatus#OK} if everything succeeded,
     * {@link HttpStatus#INTERNAL_SERVER_ERROR} otherwise.
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

    /**
     * {@code /api/client/filter}:  Query clients by provided criteria.
     * <p>
     * See <a href=https://www.jhipster.tech/entities-filtering/#public-interface>the jhipster documentation</a> for the syntax rules
     *
     * @param criteria The filter that the clients should match
     * @return All clients that match the criteria
     */
    @GetMapping("/api/client/filter")
    public ResponseEntity<List<ClientDTO>> filterClients(ClientCriteria criteria) {
        LOG.info("REST request to get Clients by criteria: {}", criteria);

        final List<Client> res = clientQueryService.findByCriteria(criteria);
        return new ResponseEntity<>(
            res.stream().map(clientMapper::clientToClientDTO).toList(),
            HttpStatus.OK
        );
    }
}
