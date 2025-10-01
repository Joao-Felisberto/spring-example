package com.github.joao_felisberto.microservice.web.rest;

import com.github.joao_felisberto.microservice.domain.Address;
import com.github.joao_felisberto.microservice.domain.Client;
import com.github.joao_felisberto.microservice.repository.AddressRepository;
import com.github.joao_felisberto.microservice.repository.ClientRepository;
import com.github.joao_felisberto.microservice.service.api.dto.ClientDTO;
import com.github.joao_felisberto.microservice.web.api.ClientApiDelegate;
import com.github.joao_felisberto.microservice.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * REST controller for managing {@link com.github.joao_felisberto.microservice.domain.Client}.
 * <p/>
 * Exposes the followingg endpoints:
 * <ul>
 *     <li> {@code POST /api/clients} Add a new client</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/clients")
@Transactional
public class ClientController implements ClientApiDelegate {
    private static final Logger LOG = LoggerFactory.getLogger(ClientController.class);
    private static final String ENTITY_NAME =  Client.class.getName();

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClientRepository clientRepository;
    private final AddressRepository addressRepository;

    @Autowired
    public ClientController(ClientRepository clientRepository, AddressRepository addressRepository) {
        this.clientRepository = clientRepository;
        this.addressRepository = addressRepository;
    }

    /**
     * {@code POST  /clients} : Add a new client.
     *
     * @param clientDTO the client to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new client,
     *          or with status {@code 400 (Bad Request)} if the client has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect because the generated ID is not well formed.
     */
    @PostMapping("")
    @Override
    public ResponseEntity<ClientDTO> postClient(@Valid @RequestBody ClientDTO clientDTO) /*throws URISyntaxException*/ {
        LOG.info("Got DTO : {}", clientDTO);
        final Client client = Client.fromDTO(clientDTO);
        LOG.info("POST request to save Client : {}", client);

        if (client.getId() != null) {
            LOG.error("A new client cannot already have an ID");
            throw new BadRequestAlertException("A new client cannot already have an ID", ENTITY_NAME, "idexists");
        }

        final Address addressRes = addressRepository.save(client.getAddress());

        // fixme does this exfiltrate data since relationships are ill encoded in POJO?
        final Client clientRes = clientRepository.save(client);
        try {
            return ResponseEntity.created(new URI("/api/clients/" + client.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, client.getId().toString()))
                .body(clientRes.toDTO());
        } catch (URISyntaxException e) {
            LOG.error("Malformed URI: '{}'", "/api/clients/" + client.getId());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
