package com.lfdq.clubplanner.web.rest;

import com.lfdq.clubplanner.ClubplannerApp;
import com.lfdq.clubplanner.domain.Club;
import com.lfdq.clubplanner.repository.ClubRepository;
import com.lfdq.clubplanner.service.ClubService;
import com.lfdq.clubplanner.repository.search.ClubSearchRepository;

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


/**
 * Test class for the ClubResource REST controller.
 *
 * @see ClubResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ClubplannerApp.class)
@WebAppConfiguration
@IntegrationTest
public class ClubResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private ClubRepository clubRepository;

    @Inject
    private ClubService clubService;

    @Inject
    private ClubSearchRepository clubSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restClubMockMvc;

    private Club club;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ClubResource clubResource = new ClubResource();
        ReflectionTestUtils.setField(clubResource, "clubService", clubService);
        this.restClubMockMvc = MockMvcBuilders.standaloneSetup(clubResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        clubSearchRepository.deleteAll();
        club = new Club();
        club.setName(DEFAULT_NAME);
        club.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createClub() throws Exception {
        int databaseSizeBeforeCreate = clubRepository.findAll().size();

        // Create the Club

        restClubMockMvc.perform(post("/api/clubs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(club)))
                .andExpect(status().isCreated());

        // Validate the Club in the database
        List<Club> clubs = clubRepository.findAll();
        assertThat(clubs).hasSize(databaseSizeBeforeCreate + 1);
        Club testClub = clubs.get(clubs.size() - 1);
        assertThat(testClub.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testClub.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the Club in ElasticSearch
        Club clubEs = clubSearchRepository.findOne(testClub.getId());
        assertThat(clubEs).isEqualToComparingFieldByField(testClub);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = clubRepository.findAll().size();
        // set the field null
        club.setName(null);

        // Create the Club, which fails.

        restClubMockMvc.perform(post("/api/clubs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(club)))
                .andExpect(status().isBadRequest());

        List<Club> clubs = clubRepository.findAll();
        assertThat(clubs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllClubs() throws Exception {
        // Initialize the database
        clubRepository.saveAndFlush(club);

        // Get all the clubs
        restClubMockMvc.perform(get("/api/clubs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(club.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getClub() throws Exception {
        // Initialize the database
        clubRepository.saveAndFlush(club);

        // Get the club
        restClubMockMvc.perform(get("/api/clubs/{id}", club.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(club.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingClub() throws Exception {
        // Get the club
        restClubMockMvc.perform(get("/api/clubs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClub() throws Exception {
        // Initialize the database
        clubService.save(club);

        int databaseSizeBeforeUpdate = clubRepository.findAll().size();

        // Update the club
        Club updatedClub = new Club();
        updatedClub.setId(club.getId());
        updatedClub.setName(UPDATED_NAME);
        updatedClub.setDescription(UPDATED_DESCRIPTION);

        restClubMockMvc.perform(put("/api/clubs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedClub)))
                .andExpect(status().isOk());

        // Validate the Club in the database
        List<Club> clubs = clubRepository.findAll();
        assertThat(clubs).hasSize(databaseSizeBeforeUpdate);
        Club testClub = clubs.get(clubs.size() - 1);
        assertThat(testClub.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testClub.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the Club in ElasticSearch
        Club clubEs = clubSearchRepository.findOne(testClub.getId());
        assertThat(clubEs).isEqualToComparingFieldByField(testClub);
    }

    @Test
    @Transactional
    public void deleteClub() throws Exception {
        // Initialize the database
        clubService.save(club);

        int databaseSizeBeforeDelete = clubRepository.findAll().size();

        // Get the club
        restClubMockMvc.perform(delete("/api/clubs/{id}", club.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean clubExistsInEs = clubSearchRepository.exists(club.getId());
        assertThat(clubExistsInEs).isFalse();

        // Validate the database is empty
        List<Club> clubs = clubRepository.findAll();
        assertThat(clubs).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchClub() throws Exception {
        // Initialize the database
        clubService.save(club);

        // Search the club
        restClubMockMvc.perform(get("/api/_search/clubs?query=id:" + club.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(club.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
}
