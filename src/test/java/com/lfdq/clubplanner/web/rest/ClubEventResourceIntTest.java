package com.lfdq.clubplanner.web.rest;

import com.lfdq.clubplanner.ClubplannerApp;
import com.lfdq.clubplanner.domain.ClubEvent;
import com.lfdq.clubplanner.repository.ClubEventRepository;
import com.lfdq.clubplanner.service.ClubEventService;
import com.lfdq.clubplanner.repository.search.ClubEventSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.lfdq.clubplanner.domain.enumeration.EventType;

/**
 * Test class for the ClubEventResource REST controller.
 *
 * @see ClubEventResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ClubplannerApp.class)
@WebAppConfiguration
@IntegrationTest
public class ClubEventResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_SCHEDULE = "AAAAA";
    private static final String UPDATED_SCHEDULE = "BBBBB";

    private static final EventType DEFAULT_EVENT_TYPE = EventType.MATCH;
    private static final EventType UPDATED_EVENT_TYPE = EventType.TRAINING;

    @Inject
    private ClubEventRepository clubEventRepository;

    @Inject
    private ClubEventService clubEventService;

    @Inject
    private ClubEventSearchRepository clubEventSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restClubEventMockMvc;

    private ClubEvent clubEvent;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ClubEventResource clubEventResource = new ClubEventResource();
        ReflectionTestUtils.setField(clubEventResource, "clubEventService", clubEventService);
        this.restClubEventMockMvc = MockMvcBuilders.standaloneSetup(clubEventResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        clubEventSearchRepository.deleteAll();
        clubEvent = new ClubEvent();
        clubEvent.setName(DEFAULT_NAME);
        clubEvent.setSchedule(DEFAULT_SCHEDULE);
        clubEvent.setEventType(DEFAULT_EVENT_TYPE);
    }

    @Test
    @Transactional
    public void createClubEvent() throws Exception {
        int databaseSizeBeforeCreate = clubEventRepository.findAll().size();

        // Create the ClubEvent

        restClubEventMockMvc.perform(post("/api/club-events")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(clubEvent)))
                .andExpect(status().isCreated());

        // Validate the ClubEvent in the database
        List<ClubEvent> clubEvents = clubEventRepository.findAll();
        assertThat(clubEvents).hasSize(databaseSizeBeforeCreate + 1);
        ClubEvent testClubEvent = clubEvents.get(clubEvents.size() - 1);
        assertThat(testClubEvent.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testClubEvent.getSchedule()).isEqualTo(DEFAULT_SCHEDULE);
        assertThat(testClubEvent.getEventType()).isEqualTo(DEFAULT_EVENT_TYPE);

        // Validate the ClubEvent in ElasticSearch
        ClubEvent clubEventEs = clubEventSearchRepository.findOne(testClubEvent.getId());
        assertThat(clubEventEs).isEqualToComparingFieldByField(testClubEvent);
    }

    @Test
    @Transactional
    public void getAllClubEvents() throws Exception {
        // Initialize the database
        clubEventRepository.saveAndFlush(clubEvent);

        // Get all the clubEvents
        restClubEventMockMvc.perform(get("/api/club-events?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(clubEvent.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].schedule").value(hasItem(DEFAULT_SCHEDULE.toString())))
                .andExpect(jsonPath("$.[*].eventType").value(hasItem(DEFAULT_EVENT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getClubEvent() throws Exception {
        // Initialize the database
        clubEventRepository.saveAndFlush(clubEvent);

        // Get the clubEvent
        restClubEventMockMvc.perform(get("/api/club-events/{id}", clubEvent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(clubEvent.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.schedule").value(DEFAULT_SCHEDULE.toString()))
            .andExpect(jsonPath("$.eventType").value(DEFAULT_EVENT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingClubEvent() throws Exception {
        // Get the clubEvent
        restClubEventMockMvc.perform(get("/api/club-events/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClubEvent() throws Exception {
        // Initialize the database
        clubEventService.save(clubEvent);

        int databaseSizeBeforeUpdate = clubEventRepository.findAll().size();

        // Update the clubEvent
        ClubEvent updatedClubEvent = new ClubEvent();
        updatedClubEvent.setId(clubEvent.getId());
        updatedClubEvent.setName(UPDATED_NAME);
        updatedClubEvent.setSchedule(UPDATED_SCHEDULE);
        updatedClubEvent.setEventType(UPDATED_EVENT_TYPE);

        restClubEventMockMvc.perform(put("/api/club-events")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedClubEvent)))
                .andExpect(status().isOk());

        // Validate the ClubEvent in the database
        List<ClubEvent> clubEvents = clubEventRepository.findAll();
        assertThat(clubEvents).hasSize(databaseSizeBeforeUpdate);
        ClubEvent testClubEvent = clubEvents.get(clubEvents.size() - 1);
        assertThat(testClubEvent.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testClubEvent.getSchedule()).isEqualTo(UPDATED_SCHEDULE);
        assertThat(testClubEvent.getEventType()).isEqualTo(UPDATED_EVENT_TYPE);

        // Validate the ClubEvent in ElasticSearch
        ClubEvent clubEventEs = clubEventSearchRepository.findOne(testClubEvent.getId());
        assertThat(clubEventEs).isEqualToComparingFieldByField(testClubEvent);
    }

    @Test
    @Transactional
    public void deleteClubEvent() throws Exception {
        // Initialize the database
        clubEventService.save(clubEvent);

        int databaseSizeBeforeDelete = clubEventRepository.findAll().size();

        // Get the clubEvent
        restClubEventMockMvc.perform(delete("/api/club-events/{id}", clubEvent.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean clubEventExistsInEs = clubEventSearchRepository.exists(clubEvent.getId());
        assertThat(clubEventExistsInEs).isFalse();

        // Validate the database is empty
        List<ClubEvent> clubEvents = clubEventRepository.findAll();
        assertThat(clubEvents).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchClubEvent() throws Exception {
        // Initialize the database
        clubEventService.save(clubEvent);

        // Search the clubEvent
        restClubEventMockMvc.perform(get("/api/_search/club-events?query=id:" + clubEvent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clubEvent.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].schedule").value(hasItem(DEFAULT_SCHEDULE.toString())))
            .andExpect(jsonPath("$.[*].eventType").value(hasItem(DEFAULT_EVENT_TYPE.toString())));
    }
}
