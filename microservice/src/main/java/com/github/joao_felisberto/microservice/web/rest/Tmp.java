package com.github.joao_felisberto.microservice.web.rest;

import com.github.joao_felisberto.microservice.service.api.dto.ClientDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/")
@Transactional
public class Tmp {

    private final RestTemplate restTemplate = new RestTemplate();

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
}
