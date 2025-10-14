package com.github.joao_felisberto.microservice.service;

import com.github.joao_felisberto.microservice.service.api.dto.ClientDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestClient;

import java.net.URISyntaxException;

import static com.github.joao_felisberto.microservice.TestUtil.createClientDTO;

@RunWith(MockitoJUnitRunner.class)
class MiscServiceTest {

    @Mock
    private RestClient restClient;

    @Autowired
    private MiscService miscService;

    // @Test
    void createClientWithHTTPSCall() {
//        RestClient.RequestBodyUriSpec requestBodyUriSpec = mock(RestClient.RequestBodyUriSpec.class);
//        RestClient.RequestBodySpec requestBodySpec = mock(RestClient.RequestBodySpec.class);
//        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);
//
//        when(restClient.post()).thenReturn(requestBodyUriSpec);
//        when(requestBodySpec.body(anyMap())).thenReturn(requestBodySpec);
//        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
//        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
//        when(responseSpec.body(ClientDTO.class)).thenReturn(createClientDTO());
//        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);


        try {
            final ClientDTO res = miscService.createClientWithHTTPSCall(createClientDTO());
            Assertions.assertNull(res);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
