package com.github.joao_felisberto.microservice.service;

import com.github.joao_felisberto.microservice.service.api.dto.ClientDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.net.URISyntaxException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MiscServiceTest {
//    private final MiscService miscService = new MiscService();

    @Mock
    private RestClient.RequestBodyUriSpec mockUriSpec;

    @Mock
    private RestClient.RequestBodySpec mockBodySpec;

    @Mock
    private RestClient.ResponseSpec mockResponseSpec;

    @Mock
    private RestClient mockRestClient;

    private MockedStatic<RestClient> restClientStatic;

    @InjectMocks
    private MiscService miscService;

    @BeforeEach
    void setUp() {
        restClientStatic = mockStatic(RestClient.class);
        mockRestClient = mock(RestClient.class);
        when(RestClient.create()).thenReturn(mockRestClient);

        when(mockRestClient.post()).thenReturn(mockUriSpec);
        when(mockUriSpec.uri(any(URI.class))).thenReturn(mockUriSpec);
        when(mockUriSpec.body(any(ClientDTO.class))).thenReturn(mockBodySpec);
        when(mockBodySpec.retrieve()).thenReturn(mockResponseSpec);
    }

    @AfterEach
    void tearDown() {
        if (restClientStatic != null) {
            restClientStatic.close();
        }
    }

    @Test
    void createClientWithHTTPSCall_success() throws URISyntaxException, ErrorResponseException {
        final ClientDTO input = new ClientDTO();
        final ClientDTO expectedResponse = new ClientDTO();
        when(mockResponseSpec.onStatus(any(), any())).thenReturn(mockResponseSpec);
        when(mockResponseSpec.body(ClientDTO.class)).thenReturn(expectedResponse);

        final ClientDTO result = Assertions.assertDoesNotThrow(
            () -> miscService.createClientWithHTTPSCall(input)
        );

        Assertions.assertEquals(expectedResponse, result);

        verify(mockRestClient).post();
        verify(mockUriSpec).uri(new URI("https://localhost:8081/api/client"));
        verify(mockUriSpec).body(input);
        verify(mockBodySpec).retrieve();
        verify(mockResponseSpec).onStatus(any(), any());
        verify(mockResponseSpec).body(ClientDTO.class);
    }

    @Test
    void createClientWithHTTPSCall_errorResponse() throws URISyntaxException {
        final ClientDTO input = new ClientDTO();
        final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        final ErrorResponseException expectedException = new ErrorResponseException(HttpStatus.BAD_REQUEST, problemDetail, null);

        when(mockResponseSpec.onStatus(any(), any())).thenThrow(expectedException);

        Assertions.assertThrows(
            ErrorResponseException.class,
            () -> miscService.createClientWithHTTPSCall(input)
        );

        verify(mockRestClient).post();
        verify(mockUriSpec).uri(new URI("https://localhost:8081/api/client"));
        verify(mockUriSpec).body(input);
        verify(mockBodySpec).retrieve();
        verify(mockResponseSpec).onStatus(any(), any());
    }


    // @Test
    void createClientWithHTTPSCall_uriSyntaxException() {
        final ClientDTO input = new ClientDTO();

        try (MockedConstruction<URI> mockURI = mockConstruction(
            URI.class,
            withSettings().defaultAnswer(invocation -> {
                throw new URISyntaxException("a", "a");
            })
        )) {

            // fixme: why does this not work? Throws NPE
            Assertions.assertThrows(
                URISyntaxException.class,
                () -> miscService.createClientWithHTTPSCall(input)
            );
        }
    }
}
