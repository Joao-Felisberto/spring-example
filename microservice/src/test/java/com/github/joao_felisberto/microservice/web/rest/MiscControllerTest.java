package com.github.joao_felisberto.microservice.web.rest;

import com.github.joao_felisberto.microservice.service.ClientQueryService;
import com.github.joao_felisberto.microservice.service.MiscService;
import com.github.joao_felisberto.microservice.service.api.dto.ClientDTO;
import com.github.joao_felisberto.microservice.service.criteria.ClientCriteria;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponseException;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static com.github.joao_felisberto.microservice.TestUtil.createClientDTO;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
class MiscControllerTest {

    @Mock
    private MiscService miscService;

    @Mock
    private ClientQueryService clientQueryService;

    @InjectMocks
    private MiscController miscController;

    @Test
    void shortcutFound() throws URISyntaxException {
        final ClientDTO clientDTO = createClientDTO();

        when(miscService.createClientWithHTTPSCall(clientDTO)).thenReturn(clientDTO);

        final ResponseEntity<ClientDTO> res = Assertions.assertDoesNotThrow(
            () -> miscController.shortcut(clientDTO)
        );

        Assert.assertEquals(HttpStatus.OK, res.getStatusCode());
        Assert.assertEquals(clientDTO, res.getBody());
    }

    @Test
    void shortcutErrorResponse() throws URISyntaxException {
        final ClientDTO clientDTO = createClientDTO();

        when(miscService.createClientWithHTTPSCall(clientDTO)).thenThrow(new ErrorResponseException(HttpStatus.BAD_REQUEST));

        final ResponseEntity<ClientDTO> res = Assertions.assertDoesNotThrow(
            () -> miscController.shortcut(clientDTO)
        );

        Assert.assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
        Assert.assertNull(res.getBody());
    }

    @Test
    void shortcutURISyntax() throws URISyntaxException {
        final ClientDTO clientDTO = createClientDTO();

        when(miscService.createClientWithHTTPSCall(clientDTO)).thenThrow(URISyntaxException.class);

        final ResponseEntity<ClientDTO> res = Assertions.assertDoesNotThrow(
            () -> miscController.shortcut(clientDTO)
        );

        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, res.getStatusCode());
        Assert.assertNull(res.getBody());
    }

    @Test
    void filterClients() {
        final ClientCriteria clientCriteria = new ClientCriteria();

        when(clientQueryService.findByCriteria(clientCriteria)).thenReturn(new ArrayList<>());

        final ResponseEntity<List<ClientDTO>> res = miscController.filterClients(clientCriteria);

        Assertions.assertNotNull(res.getBody());
        Assertions.assertEquals(0, res.getBody().size());
        Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());
    }
}
