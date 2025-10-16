package com.github.joao_felisberto.microservice.web.rest;

import com.github.joao_felisberto.microservice.domain.Address;
import com.github.joao_felisberto.microservice.domain.Client;
import com.github.joao_felisberto.microservice.domain.enumeration.CountryCode;
import com.github.joao_felisberto.microservice.domain.mappers.ClientMapper;
import com.github.joao_felisberto.microservice.repository.AddressRepository;
import com.github.joao_felisberto.microservice.repository.ClientRepository;
import com.github.joao_felisberto.microservice.service.api.dto.AddressDTO;
import com.github.joao_felisberto.microservice.service.api.dto.ClientDTO;
import com.github.joao_felisberto.microservice.web.rest.errors.BadRequestAlertException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.github.joao_felisberto.microservice.TestUtil.createClientDTO;
import static com.github.joao_felisberto.microservice.TestUtil.createDistinctClientDTO;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(OutputCaptureExtension.class)
@ExtendWith(MockitoExtension.class)
class ClientControllerTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private ClientMapper clientMapper;

    @InjectMocks
    private ClientController clientController;


    @Test
    void postClient(CapturedOutput output) {
        // todo: is this allowed?
        //      Cloner: using something we're mocking
        //      Manual: manual cloning is bad and would be abundant in tests
        final ClientDTO clientDTO = createClientDTO();
        final Client client = clientDTOToClient(clientDTO);
        final Long id = 1L;
        Assertions.assertNull(client.getId());

        when(clientMapper.clientDTOToClient(clientDTO)).thenReturn(client);
        when(clientMapper.clientToClientDTO(client)).thenReturn(clientDTO);
        when(clientRepository.save(client)).thenAnswer(i -> {
            final Client c = i.getArgument(0, Client.class);
            c.setId(id);
            return c;
        });

        final ResponseEntity<ClientDTO> res = Assertions.assertDoesNotThrow(
            () -> clientController.postClient(clientDTO)
        );

        verify(addressRepository).save(client.getAddress());
        verify(addressRepository).findSameAddress(client.getAddress());
        verify(clientRepository).save(client);
        verify(clientMapper).clientDTOToClient(clientDTO);
        verify(clientMapper).clientToClientDTO(client);

        Assertions.assertTrue(output.getOut().contains(String.format("Created Client with id %d", id)));

        Assertions.assertEquals(HttpStatus.CREATED, res.getStatusCode());
        Assertions.assertEquals(clientDTO, res.getBody());
    }

    @Test
    void postClientWithDuplicateAddress(CapturedOutput output) {
        // todo: is this allowed?
        //      Cloner: using something we're mocking
        //      Manual: manual cloning is bad and would be abundant in tests
        final ClientDTO clientDTO = createClientDTO();
        final Client client = clientDTOToClient(clientDTO);
        final Long id = 1L;
        Assertions.assertNull(client.getId());

        when(clientMapper.clientDTOToClient(clientDTO)).thenReturn(client);
        when(clientMapper.clientToClientDTO(client)).thenReturn(clientDTO);
        when(addressRepository.findSameAddress(client.getAddress())).thenReturn(Optional.of(client.getAddress()));
        when(clientRepository.save(client)).thenAnswer(i -> {
            final Client c = i.getArgument(0, Client.class);
            c.setId(id);
            return c;
        });

        final ResponseEntity<ClientDTO> res = Assertions.assertDoesNotThrow(
            () -> clientController.postClient(clientDTO)
        );

        verify(addressRepository, times(0)).save(client.getAddress());
        verify(addressRepository).findSameAddress(client.getAddress());
        verify(clientRepository).save(client);
        verify(clientMapper).clientDTOToClient(clientDTO);
        verify(clientMapper).clientToClientDTO(client);

        Assertions.assertTrue(output.getOut().contains(String.format("Created Client with id %d", id)));

        Assertions.assertEquals(HttpStatus.CREATED, res.getStatusCode());
        Assertions.assertEquals(clientDTO, res.getBody());
    }

    @Test
    void postClientWithId(CapturedOutput output) {
        final ClientDTO clientDTO = createClientDTO();
        final Client client = clientDTOToClient(clientDTO);
        final Long clientID = 10L;
        client.setId(clientID);

        Assertions.assertNotNull(client.getId());
        when(clientMapper.clientDTOToClient(clientDTO)).thenReturn(client);

        final BadRequestAlertException thrown = Assertions.assertThrows(
            BadRequestAlertException.class,
            () -> clientController.postClient(clientDTO)
        );
        Assertions.assertEquals("idexists", thrown.getErrorKey());

        Assertions.assertTrue(output.getOut().contains("A new client cannot already have an ID"));
    }

    @Test
    void deleteClient(CapturedOutput output) {
        final Long clientID = 0L;
        doNothing().when(clientRepository).deleteById(clientID);

        clientController.deleteClient(clientID);

        verify(clientRepository).deleteById(clientID);

        Assertions.assertTrue(output.getOut().contains(String.format("Deleted Client with id %d", clientID)));
        Assertions.assertEquals("", output.getErr());
    }

    @Test
    void getClientByExistingNIF() {
        final ClientDTO clientDTO = createClientDTO();
        final Client client = clientDTOToClient(clientDTO);
        final Long clientID = 10L;
        client.setId(clientID);

        Assertions.assertNotNull(client.getId());
        when(clientMapper.clientToClientDTO(client)).thenReturn(clientDTO);
        when(clientRepository.findBynif(client.getNif())).thenReturn(Optional.of(client));

        final ResponseEntity<ClientDTO> res = Assertions.assertDoesNotThrow(
            () -> clientController.getClientByNIF(client.getNif())
        );

        Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());
        Assertions.assertEquals(clientDTO, res.getBody());
    }

    @Test
    void getClientByNonExistingNIF() {
        final ClientDTO clientDTO = createClientDTO();
        final Client client = clientDTOToClient(clientDTO);
        final Long clientID = 10L;
        client.setId(clientID);

        Assertions.assertNotNull(client.getId());
        when(clientRepository.findBynif(client.getNif())).thenReturn(Optional.empty());

        final ResponseEntity<ClientDTO> res = Assertions.assertDoesNotThrow(
            () -> clientController.getClientByNIF(client.getNif())
        );

        Assertions.assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
        Assertions.assertNull(res.getBody());
    }

    @Test
    void listClients() {
        final List<ClientDTO> dtos = List.of(
            createDistinctClientDTO("a"),
            createDistinctClientDTO("b"),
            createDistinctClientDTO("c"),
            createDistinctClientDTO("d"),
            createDistinctClientDTO("e")
        );

        final List<Client> clients = dtos.stream()
            .map(dto -> {
                final Client c = clientDTOToClient(dto);
                when(clientMapper.clientToClientDTO(c)).thenReturn(dto);
                return c;
            })
            .toList();

        when(clientRepository.findAll()).thenReturn(clients);

        final ResponseEntity<List<ClientDTO>> res = Assertions.assertDoesNotThrow(
            () -> clientController.listClients()
        );

        Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());
        Assertions.assertEquals(dtos, res.getBody());
    }

    @Test
    void listClientsOne() {
        final List<ClientDTO> dtos = List.of(
            createDistinctClientDTO("a")
        );

        final List<Client> clients = dtos.stream()
            .map(dto -> {
                final Client c = clientDTOToClient(dto);
                when(clientMapper.clientToClientDTO(c)).thenReturn(dto);
                return c;
            })
            .toList();

        when(clientRepository.findAll()).thenReturn(clients);

        final ResponseEntity<List<ClientDTO>> res = Assertions.assertDoesNotThrow(
            () -> clientController.listClients()
        );

        Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());
        Assertions.assertEquals(dtos, res.getBody());
    }

    @Test
    void listClientsEmpty() {
        when(clientRepository.findAll()).thenReturn(new ArrayList<>());

        final ResponseEntity<List<ClientDTO>> res = Assertions.assertDoesNotThrow(
            () -> clientController.listClients()
        );

        Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());
        Assertions.assertNotNull(res.getBody());
        Assertions.assertEquals(0, res.getBody().size());
    }

    @Test
    void getClientsByName() {
        final String name = "a";
        final List<ClientDTO> dtos = List.of(
            createDistinctClientDTO("a"),
            createDistinctClientDTO("b"),
            createDistinctClientDTO("c"),
            createDistinctClientDTO("d"),
            createDistinctClientDTO("e")
        );

        final List<Client> clients = dtos.stream()
            .map(dto -> {
                final Client c = clientDTOToClient(dto);
                when(clientMapper.clientToClientDTO(c)).thenReturn(dto);
                return c;
            })
            .toList();

        when(clientRepository.findAllByNameLike(name)).thenReturn(clients);

        final ResponseEntity<List<ClientDTO>> res = Assertions.assertDoesNotThrow(
            () -> clientController.getClientsByName(name)
        );

        Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());
        Assertions.assertEquals(dtos, res.getBody());
    }

    @Test
    void getClientsByNameOne() {
        final String name = "a";
        final List<ClientDTO> dtos = List.of(
            createDistinctClientDTO("a")
        );

        final List<Client> clients = dtos.stream()
            .map(dto -> {
                final Client c = clientDTOToClient(dto);
                when(clientMapper.clientToClientDTO(c)).thenReturn(dto);
                return c;
            })
            .toList();

        when(clientRepository.findAllByNameLike(name)).thenReturn(clients);

        final ResponseEntity<List<ClientDTO>> res = Assertions.assertDoesNotThrow(
            () -> clientController.getClientsByName(name)
        );

        Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());
        Assertions.assertEquals(dtos, res.getBody());
    }

    @Test
    void getClientsByNameEmpty() {
        final String name = "a";
        when(clientRepository.findAllByNameLike(name)).thenReturn(new ArrayList<>());

        final ResponseEntity<List<ClientDTO>> res = Assertions.assertDoesNotThrow(
            () -> clientController.getClientsByName(name)
        );

        Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());
        Assertions.assertNotNull(res.getBody());
        Assertions.assertEquals(0, res.getBody().size());
    }

    private Client clientDTOToClient(ClientDTO clientDTO) {
        if (clientDTO == null) {
            return null;
        }

        Client client = new Client();

        client.countryCode(CountryCode.values()[clientDTO.getPhoneCountryCode().intValue()]);
        client.setName(clientDTO.getName());
        client.setNif(clientDTO.getNif());
        client.address(addressDTOToAddress(clientDTO.getAddress()));
        if (clientDTO.getPhoneNumber() != null) {
            client.phoneNumber(clientDTO.getPhoneNumber().longValue());
        }

        return client;
    }

    private Address addressDTOToAddress(AddressDTO addressDTO) {
        if (addressDTO == null) {
            return null;
        }

        Address address = new Address();

        address.setCity(addressDTO.getCity());
        address.setCountry(CountryCode.values()[addressDTO.getCountry().intValue()]);
        address.setPostcode(addressDTO.getPostcode());
        address.setStateProvince(addressDTO.getStateProvince());
        address.setStreetOne(addressDTO.getStreetOne());
        address.setStreetTwo(addressDTO.getStreetTwo());
        address.setEmailAddress(addressDTO.getEmailAddress());

        return address;
    }
}
