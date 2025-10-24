package com.github.joao_felisberto.microservice.service;

import com.github.joao_felisberto.microservice.domain.Client;
import com.github.joao_felisberto.microservice.domain.mappers.ClientMapper;
import com.github.joao_felisberto.microservice.repository.ClientRepository;
import com.github.joao_felisberto.microservice.service.criteria.ClientCriteria;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;


// @RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
class ClientQueryServiceMockTest {

    private static final ClientMapper clientMapper = Mappers.getMapper(ClientMapper.class);

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientQueryService clientQueryService;

    @Test
    void findByCriteria() {
        final ClientCriteria clientCriteria = null;

        when(clientRepository.findAll(Specification.where(null))).thenReturn(new ArrayList<>());

        final List<Client> res = clientQueryService.findByCriteria(clientCriteria);

        verify(clientRepository, times(1)).findAll(Specification.where(null));

        // todo how to verify a method of the same class?
        // verify(clientQueryService, times(1)).createSpecification(clientCriteria);

        Assertions.assertEquals(0, res.size());
    }

    @Test
    void createSpecificationNullCriteria() {
        final ClientCriteria clientCriteria = null;

        try (final MockedStatic<Specification> specification = mockStatic(Specification.class)) {

            final Specification<Client> res = clientQueryService.createSpecification(clientCriteria);

            specification.verify(
                () -> Specification.allOf(null, null, null, null, null, null, null),
                times(0)
            );
            specification.verify(
                () -> Specification.where(null)
            );

            Assertions.assertEquals(Specification.where(null), res);
        }
    }

    @Test
    void createSpecificationCriteria() {
        final ClientCriteria clientCriteria = new ClientCriteria();

        try (final MockedStatic<Specification> specification = mockStatic(Specification.class)) {

            final Specification<Client> res = clientQueryService.createSpecification(clientCriteria);

            specification.verify(
                () -> Specification.allOf(null, null, null, null, null, null, null)
            );
            specification.verify(
                () -> Specification.where(null),
                times(0)
            );

            Assertions.assertEquals(Specification.where(null), res);
        }
    }

    @Test
    void createSpecificationCriteriaFalseDistinct() {
        final ClientCriteria clientCriteria = new ClientCriteria();
        clientCriteria.setDistinct(false);

        try (final MockedStatic<Specification> specification = mockStatic(Specification.class)) {

            final Specification<Client> res = clientQueryService.createSpecification(clientCriteria);

            specification.verify(
                () -> Specification.allOf(null, null, null, null, null, null, null)
            );
            specification.verify(
                () -> Specification.where(null),
                times(0)
            );

            Assertions.assertEquals(Specification.where(null), res);
        }
    }
}
