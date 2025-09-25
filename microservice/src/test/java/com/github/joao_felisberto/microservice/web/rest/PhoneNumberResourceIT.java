package com.github.joao_felisberto.microservice.web.rest;

import static com.github.joao_felisberto.microservice.domain.PhoneNumberAsserts.*;
import static com.github.joao_felisberto.microservice.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.joao_felisberto.microservice.IntegrationTest;
import com.github.joao_felisberto.microservice.domain.PhoneNumber;
import com.github.joao_felisberto.microservice.domain.enumeration.CountryCode;
import com.github.joao_felisberto.microservice.repository.PhoneNumberRepository;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PhoneNumberResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PhoneNumberResourceIT {

    private static final CountryCode DEFAULT_COUNTRY_CODE = CountryCode.PORTUGAL;
    private static final CountryCode UPDATED_COUNTRY_CODE = CountryCode.SPAIN;

    private static final Long DEFAULT_NUMBER = 0L;
    private static final Long UPDATED_NUMBER = 1L;

    private static final String ENTITY_API_URL = "/api/phone-numbers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PhoneNumberRepository phoneNumberRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPhoneNumberMockMvc;

    private PhoneNumber phoneNumber;

    private PhoneNumber insertedPhoneNumber;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PhoneNumber createEntity() {
        return new PhoneNumber().countryCode(DEFAULT_COUNTRY_CODE).number(DEFAULT_NUMBER);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PhoneNumber createUpdatedEntity() {
        return new PhoneNumber().countryCode(UPDATED_COUNTRY_CODE).number(UPDATED_NUMBER);
    }

    @BeforeEach
    void initTest() {
        phoneNumber = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPhoneNumber != null) {
            phoneNumberRepository.delete(insertedPhoneNumber);
            insertedPhoneNumber = null;
        }
    }

    // @Test
    @Transactional
    void createPhoneNumber() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PhoneNumber
        var returnedPhoneNumber = om.readValue(
            restPhoneNumberMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(phoneNumber)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PhoneNumber.class
        );

        // Validate the PhoneNumber in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPhoneNumberUpdatableFieldsEquals(returnedPhoneNumber, getPersistedPhoneNumber(returnedPhoneNumber));

        insertedPhoneNumber = returnedPhoneNumber;
    }

    // @Test
    @Transactional
    void createPhoneNumberWithExistingId() throws Exception {
        // Create the PhoneNumber with an existing ID
        phoneNumber.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPhoneNumberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(phoneNumber)))
            .andExpect(status().isBadRequest());

        // Validate the PhoneNumber in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    // @Test
    @Transactional
    void checkCountryCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        phoneNumber.setCountryCode(null);

        // Create the PhoneNumber, which fails.

        restPhoneNumberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(phoneNumber)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    // @Test
    @Transactional
    void checkNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        phoneNumber.setNumber(null);

        // Create the PhoneNumber, which fails.

        restPhoneNumberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(phoneNumber)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    // @Test
    @Transactional
    void getAllPhoneNumbers() throws Exception {
        // Initialize the database
        insertedPhoneNumber = phoneNumberRepository.saveAndFlush(phoneNumber);

        // Get all the phoneNumberList
        restPhoneNumberMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(phoneNumber.getId().intValue())))
            .andExpect(jsonPath("$.[*].countryCode").value(hasItem(DEFAULT_COUNTRY_CODE.toString())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER.intValue())));
    }

    // @Test
    @Transactional
    void getPhoneNumber() throws Exception {
        // Initialize the database
        insertedPhoneNumber = phoneNumberRepository.saveAndFlush(phoneNumber);

        // Get the phoneNumber
        restPhoneNumberMockMvc
            .perform(get(ENTITY_API_URL_ID, phoneNumber.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(phoneNumber.getId().intValue()))
            .andExpect(jsonPath("$.countryCode").value(DEFAULT_COUNTRY_CODE.toString()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER.intValue()));
    }

    // @Test
    @Transactional
    void getNonExistingPhoneNumber() throws Exception {
        // Get the phoneNumber
        restPhoneNumberMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    // @Test
    @Transactional
    void putExistingPhoneNumber() throws Exception {
        // Initialize the database
        insertedPhoneNumber = phoneNumberRepository.saveAndFlush(phoneNumber);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the phoneNumber
        PhoneNumber updatedPhoneNumber = phoneNumberRepository.findById(phoneNumber.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPhoneNumber are not directly saved in db
        em.detach(updatedPhoneNumber);
        updatedPhoneNumber.countryCode(UPDATED_COUNTRY_CODE).number(UPDATED_NUMBER);

        restPhoneNumberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPhoneNumber.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPhoneNumber))
            )
            .andExpect(status().isOk());

        // Validate the PhoneNumber in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPhoneNumberToMatchAllProperties(updatedPhoneNumber);
    }

    // @Test
    @Transactional
    void putNonExistingPhoneNumber() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        phoneNumber.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPhoneNumberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, phoneNumber.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(phoneNumber))
            )
            .andExpect(status().isBadRequest());

        // Validate the PhoneNumber in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    // @Test
    @Transactional
    void putWithIdMismatchPhoneNumber() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        phoneNumber.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhoneNumberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(phoneNumber))
            )
            .andExpect(status().isBadRequest());

        // Validate the PhoneNumber in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    // @Test
    @Transactional
    void putWithMissingIdPathParamPhoneNumber() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        phoneNumber.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhoneNumberMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(phoneNumber)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PhoneNumber in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    // @Test
    @Transactional
    void partialUpdatePhoneNumberWithPatch() throws Exception {
        // Initialize the database
        insertedPhoneNumber = phoneNumberRepository.saveAndFlush(phoneNumber);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the phoneNumber using partial update
        PhoneNumber partialUpdatedPhoneNumber = new PhoneNumber();
        partialUpdatedPhoneNumber.setId(phoneNumber.getId());

        partialUpdatedPhoneNumber.countryCode(UPDATED_COUNTRY_CODE);

        restPhoneNumberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPhoneNumber.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPhoneNumber))
            )
            .andExpect(status().isOk());

        // Validate the PhoneNumber in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPhoneNumberUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPhoneNumber, phoneNumber),
            getPersistedPhoneNumber(phoneNumber)
        );
    }

    // @Test
    @Transactional
    void fullUpdatePhoneNumberWithPatch() throws Exception {
        // Initialize the database
        insertedPhoneNumber = phoneNumberRepository.saveAndFlush(phoneNumber);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the phoneNumber using partial update
        PhoneNumber partialUpdatedPhoneNumber = new PhoneNumber();
        partialUpdatedPhoneNumber.setId(phoneNumber.getId());

        partialUpdatedPhoneNumber.countryCode(UPDATED_COUNTRY_CODE).number(UPDATED_NUMBER);

        restPhoneNumberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPhoneNumber.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPhoneNumber))
            )
            .andExpect(status().isOk());

        // Validate the PhoneNumber in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPhoneNumberUpdatableFieldsEquals(partialUpdatedPhoneNumber, getPersistedPhoneNumber(partialUpdatedPhoneNumber));
    }

    // @Test
    @Transactional
    void patchNonExistingPhoneNumber() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        phoneNumber.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPhoneNumberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, phoneNumber.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(phoneNumber))
            )
            .andExpect(status().isBadRequest());

        // Validate the PhoneNumber in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    // @Test
    @Transactional
    void patchWithIdMismatchPhoneNumber() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        phoneNumber.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhoneNumberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(phoneNumber))
            )
            .andExpect(status().isBadRequest());

        // Validate the PhoneNumber in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    // @Test
    @Transactional
    void patchWithMissingIdPathParamPhoneNumber() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        phoneNumber.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhoneNumberMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(phoneNumber)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PhoneNumber in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    // @Test
    @Transactional
    void deletePhoneNumber() throws Exception {
        // Initialize the database
        insertedPhoneNumber = phoneNumberRepository.saveAndFlush(phoneNumber);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the phoneNumber
        restPhoneNumberMockMvc
            .perform(delete(ENTITY_API_URL_ID, phoneNumber.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return phoneNumberRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected PhoneNumber getPersistedPhoneNumber(PhoneNumber phoneNumber) {
        return phoneNumberRepository.findById(phoneNumber.getId()).orElseThrow();
    }

    protected void assertPersistedPhoneNumberToMatchAllProperties(PhoneNumber expectedPhoneNumber) {
        assertPhoneNumberAllPropertiesEquals(expectedPhoneNumber, getPersistedPhoneNumber(expectedPhoneNumber));
    }

    protected void assertPersistedPhoneNumberToMatchUpdatableProperties(PhoneNumber expectedPhoneNumber) {
        assertPhoneNumberAllUpdatablePropertiesEquals(expectedPhoneNumber, getPersistedPhoneNumber(expectedPhoneNumber));
    }
}
