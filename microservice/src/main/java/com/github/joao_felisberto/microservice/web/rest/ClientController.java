package com.github.joao_felisberto.microservice.web.rest;

import com.github.joao_felisberto.microservice.domain.Address;
import com.github.joao_felisberto.microservice.domain.Client;
import com.github.joao_felisberto.microservice.domain.mappers.ClientMapper;
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
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import tech.jhipster.web.util.HeaderUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.github.joao_felisberto.microservice.domain.Client}.
 * <p/>
 * Exposes the following endpoints:
 * <ul>
 *     <li> {@code POST /api/client} Create a new client</li>
 *     <li> {@code DELETE /api/client} Delete a client</li>
 *     <li> {@code GET /api/client/<nif>} Find a client by their NIF</li>
 *     <li> {@code GET /api/client} List all clients</li>
 *     <li> {@code GET /api/client?name=<name>} Find all clients with a name that contains {@code name}</li>
 * </ul>
 */
@Service
public class ClientController implements ClientApiDelegate {
    private static final Logger LOG = LoggerFactory.getLogger(ClientController.class);
    private static final String ENTITY_NAME = Client.class.getName();

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClientRepository clientRepository;
    private final AddressRepository addressRepository;
    private final ClientMapper clientMapper;

    @Autowired
    public ClientController(ClientRepository clientRepository, AddressRepository addressRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.addressRepository = addressRepository;
        this.clientMapper = clientMapper;
    }

    /**
     * {@code POST  /client} : Add a new client.
     *
     * @param clientDTO the client to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new client,
     * or with status {@code 400 (Bad Request)} if the client has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect because the generated ID is not well formed.
     */
    @Override
    public ResponseEntity<ClientDTO> postClient(@Valid @RequestBody ClientDTO clientDTO) /*throws URISyntaxException*/ {
        final Client client = clientMapper.clientDTOToClient(clientDTO);

        if (client.getId() != null) {
            LOG.error("A new client cannot already have an ID");
            throw new BadRequestAlertException("A new client cannot already have an ID", ENTITY_NAME, "idexists");
        }

        final Address addr = client.getAddress();
        final Optional<Address> addrDB = addressRepository.findSameAddress(addr);
        addrDB.ifPresentOrElse(
            address -> addr.setId(address.getId()),
            () -> addressRepository.save(client.getAddress())
        );

        final Client clientRes = clientRepository.save(client);
        LOG.info("Created Client with id {}", client.getId());
        try {
            return ResponseEntity.created(new URI("/api/clients/" + client.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, client.getId().toString()))
                .body(clientMapper.clientToClientDTO(clientRes));
        } catch (URISyntaxException e) {
            LOG.error("Malformed URI: '{}'", "/api/clients/" + client.getId());
            clientRepository.delete(client);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * {@code DELETE  /clients} : Delete an existing client.
     *
     * @param id the id of the Client to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (No Content)},
     * or {@code 404 (Not Found)} if no id is provided
     */
    @Override
    public ResponseEntity<Void> deleteClient(Long id) {
        LOG.debug("REST request to delete Client: {}", id);
        clientRepository.deleteById(id);
        LOG.info("Deleted Client with id {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<ClientDTO> getClientByNIF(String nif) {
        LOG.debug("REST request for Client with NIF: {}", nif);

        final Optional<Client> clientRes = clientRepository.findBynif(nif);
        if (clientRes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        final ClientDTO client = clientMapper.clientToClientDTO(clientRes.orElseThrow());

        return new ResponseEntity<>(client, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<ClientDTO>> listClients() {
        LOG.debug("REST request for full list of Clients");

        return new ResponseEntity<>(
            clientRepository.findAll().stream()
                .map(clientMapper::clientToClientDTO)
                .toList(),
            HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<List<ClientDTO>> getClientsByName(@RequestParam String name) {
        LOG.debug("REST request for Clients with name");

        return new ResponseEntity<>(
            clientRepository.findAllByNameLike(name).stream()
                .map(clientMapper::clientToClientDTO)
                .toList(),
            HttpStatus.OK
        );
    }
}
