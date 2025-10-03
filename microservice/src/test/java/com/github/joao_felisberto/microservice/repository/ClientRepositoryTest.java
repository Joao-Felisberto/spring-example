package com.github.joao_felisberto.microservice.repository;

//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ClientRepositoryTest {
/*
    //    @Autowired
    private final ClientRepository clientRepository;

    //    @Autowired
    private final AddressRepository addressRepository;

    @Autowired
    public ClientRepositoryTest(ClientRepository clientRepository, AddressRepository addressRepository) {
        this.clientRepository = clientRepository;
        this.addressRepository = addressRepository;
    }

    @BeforeEach
    void cleanDB() {
        addressRepository.deleteAll();
        addressRepository.flush();
        clientRepository.deleteAll();
        clientRepository.flush();
    }

    @Test
    public void testFindExistingClientByNIF() throws Exception {
        final Client client = Client.fromDTO(createClientDTO());

        addressRepository.saveAndFlush(client.getAddress());
        clientRepository.saveAndFlush(client);

        final Optional<Client> retrieved = clientRepository.findBynif(client.getNif());
        Assertions.assertTrue(retrieved.isPresent());

        Assertions.assertEquals(client.getNif(), retrieved.orElseThrow().getNif());
    }

    @Test
    public void testFindNonExistingClientByNIF() throws Exception {
        final Client client = Client.fromDTO(createClientDTO());

        addressRepository.saveAndFlush(client.getAddress());
        clientRepository.saveAndFlush(client);

        final Optional<Client> retrieved = clientRepository.findBynif(client.getNif());
        Assertions.assertTrue(retrieved.isEmpty());
    }

    @Test
    public void testFindNonExistingClientByName() throws Exception {
        final Client[] cs = {
            Client.fromDTO(createDistinctClientDTO("a")),
            Client.fromDTO(createDistinctClientDTO("b")),
            Client.fromDTO(createDistinctClientDTO("c")),
            Client.fromDTO(createDistinctClientDTO("d")),
        };
        for (final Client c : cs) {
            addressRepository.saveAndFlush(c.getAddress());
            clientRepository.saveAndFlush(c);
        }

        final List<Client> retrieved = clientRepository.findAllByNameContaining("NON EXISTENT");
        Assertions.assertEquals(0, retrieved.size());
    }

    @Test
    public void testFindExistingClientByExactName() throws Exception {
        final String[] names = {"a", "b", "c", "d"};
        final List<Client> cs = Arrays.stream(names).map(name -> Client.fromDTO(createDistinctClientDTO(name))).collect(Collectors.toList());
        cs.forEach(c -> {
            addressRepository.saveAndFlush(c.getAddress());
            clientRepository.saveAndFlush(c);
        });

        for (final String name : names) {
            final List<Client> retrieved = clientRepository.findAllByNameContaining(name);

            Assertions.assertEquals(1, retrieved.size());
            Assertions.assertEquals(name, retrieved.get(0).getName());
        }
    }

    @Test
    public void testFindExistingClientByApproximateName() throws Exception {
        final String[] names = {"a", "aa", "ab", "bb", "cc c"};
        final List<Client> cs = Arrays.stream(names)
            .map(name -> Client.fromDTO(createDistinctClientDTO(name)))
            .toList();
        cs.forEach(c -> {
            addressRepository.saveAndFlush(c.getAddress());
            clientRepository.saveAndFlush(c);
        });

        final Map<String, List<String>> expected = Map.of(
            "a", List.of("a", "aa", "ab"),
            "b", List.of("b", "bd")
        );
        expected.forEach((k, v) -> {
            final List<Client> results = clientRepository.findAllByNameContaining(k);
            Assertions.assertEquals(v.size(), results.size());
            results.stream().map(Client::getName).forEach(n -> Assertions.assertTrue(v.contains(n)));
        });
    }

    @Test
    public void fail() throws Exception {
        Assertions.assertTrue(false);
    }
 */
}
