package com.lfdq.clubplanner.web.rest;

import com.lfdq.clubplanner.ClubplannerApp;
import com.lfdq.clubplanner.domain.Site;
import com.lfdq.clubplanner.repository.SiteRepository;
import com.lfdq.clubplanner.repository.search.SiteSearchRepository;

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
 * Test class for the SiteResource REST controller.
 *
 * @see SiteResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ClubplannerApp.class)
@WebAppConfiguration
@IntegrationTest
public class SiteResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_ADRESS = "AAAAA";
    private static final String UPDATED_ADRESS = "BBBBB";
    private static final String DEFAULT_MAP_LINK = "AAAAA";
    private static final String UPDATED_MAP_LINK = "BBBBB";

    private static final Boolean DEFAULT_IS_GYMNASIUM = false;
    private static final Boolean UPDATED_IS_GYMNASIUM = true;
    private static final String DEFAULT_RESIDENT_CLUB_NAME = "AAAAA";
    private static final String UPDATED_RESIDENT_CLUB_NAME = "BBBBB";

    @Inject
    private SiteRepository siteRepository;

    @Inject
    private SiteSearchRepository siteSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSiteMockMvc;

    private Site site;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SiteResource siteResource = new SiteResource();
        ReflectionTestUtils.setField(siteResource, "siteSearchRepository", siteSearchRepository);
        ReflectionTestUtils.setField(siteResource, "siteRepository", siteRepository);
        this.restSiteMockMvc = MockMvcBuilders.standaloneSetup(siteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        siteSearchRepository.deleteAll();
        site = new Site();
        site.setName(DEFAULT_NAME);
        site.setAdress(DEFAULT_ADRESS);
        site.setMapLink(DEFAULT_MAP_LINK);
        site.setIsGymnasium(DEFAULT_IS_GYMNASIUM);
        site.setResidentClubName(DEFAULT_RESIDENT_CLUB_NAME);
    }

    @Test
    @Transactional
    public void createSite() throws Exception {
        int databaseSizeBeforeCreate = siteRepository.findAll().size();

        // Create the Site

        restSiteMockMvc.perform(post("/api/sites")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(site)))
                .andExpect(status().isCreated());

        // Validate the Site in the database
        List<Site> sites = siteRepository.findAll();
        assertThat(sites).hasSize(databaseSizeBeforeCreate + 1);
        Site testSite = sites.get(sites.size() - 1);
        assertThat(testSite.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSite.getAdress()).isEqualTo(DEFAULT_ADRESS);
        assertThat(testSite.getMapLink()).isEqualTo(DEFAULT_MAP_LINK);
        assertThat(testSite.isIsGymnasium()).isEqualTo(DEFAULT_IS_GYMNASIUM);
        assertThat(testSite.getResidentClubName()).isEqualTo(DEFAULT_RESIDENT_CLUB_NAME);

        // Validate the Site in ElasticSearch
        Site siteEs = siteSearchRepository.findOne(testSite.getId());
        assertThat(siteEs).isEqualToComparingFieldByField(testSite);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = siteRepository.findAll().size();
        // set the field null
        site.setName(null);

        // Create the Site, which fails.

        restSiteMockMvc.perform(post("/api/sites")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(site)))
                .andExpect(status().isBadRequest());

        List<Site> sites = siteRepository.findAll();
        assertThat(sites).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSites() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get all the sites
        restSiteMockMvc.perform(get("/api/sites?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(site.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].adress").value(hasItem(DEFAULT_ADRESS.toString())))
                .andExpect(jsonPath("$.[*].mapLink").value(hasItem(DEFAULT_MAP_LINK.toString())))
                .andExpect(jsonPath("$.[*].isGymnasium").value(hasItem(DEFAULT_IS_GYMNASIUM.booleanValue())))
                .andExpect(jsonPath("$.[*].residentClubName").value(hasItem(DEFAULT_RESIDENT_CLUB_NAME.toString())));
    }

    @Test
    @Transactional
    public void getSite() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);

        // Get the site
        restSiteMockMvc.perform(get("/api/sites/{id}", site.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(site.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.adress").value(DEFAULT_ADRESS.toString()))
            .andExpect(jsonPath("$.mapLink").value(DEFAULT_MAP_LINK.toString()))
            .andExpect(jsonPath("$.isGymnasium").value(DEFAULT_IS_GYMNASIUM.booleanValue()))
            .andExpect(jsonPath("$.residentClubName").value(DEFAULT_RESIDENT_CLUB_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSite() throws Exception {
        // Get the site
        restSiteMockMvc.perform(get("/api/sites/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSite() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);
        siteSearchRepository.save(site);
        int databaseSizeBeforeUpdate = siteRepository.findAll().size();

        // Update the site
        Site updatedSite = new Site();
        updatedSite.setId(site.getId());
        updatedSite.setName(UPDATED_NAME);
        updatedSite.setAdress(UPDATED_ADRESS);
        updatedSite.setMapLink(UPDATED_MAP_LINK);
        updatedSite.setIsGymnasium(UPDATED_IS_GYMNASIUM);
        updatedSite.setResidentClubName(UPDATED_RESIDENT_CLUB_NAME);

        restSiteMockMvc.perform(put("/api/sites")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedSite)))
                .andExpect(status().isOk());

        // Validate the Site in the database
        List<Site> sites = siteRepository.findAll();
        assertThat(sites).hasSize(databaseSizeBeforeUpdate);
        Site testSite = sites.get(sites.size() - 1);
        assertThat(testSite.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSite.getAdress()).isEqualTo(UPDATED_ADRESS);
        assertThat(testSite.getMapLink()).isEqualTo(UPDATED_MAP_LINK);
        assertThat(testSite.isIsGymnasium()).isEqualTo(UPDATED_IS_GYMNASIUM);
        assertThat(testSite.getResidentClubName()).isEqualTo(UPDATED_RESIDENT_CLUB_NAME);

        // Validate the Site in ElasticSearch
        Site siteEs = siteSearchRepository.findOne(testSite.getId());
        assertThat(siteEs).isEqualToComparingFieldByField(testSite);
    }

    @Test
    @Transactional
    public void deleteSite() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);
        siteSearchRepository.save(site);
        int databaseSizeBeforeDelete = siteRepository.findAll().size();

        // Get the site
        restSiteMockMvc.perform(delete("/api/sites/{id}", site.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean siteExistsInEs = siteSearchRepository.exists(site.getId());
        assertThat(siteExistsInEs).isFalse();

        // Validate the database is empty
        List<Site> sites = siteRepository.findAll();
        assertThat(sites).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSite() throws Exception {
        // Initialize the database
        siteRepository.saveAndFlush(site);
        siteSearchRepository.save(site);

        // Search the site
        restSiteMockMvc.perform(get("/api/_search/sites?query=id:" + site.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(site.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].adress").value(hasItem(DEFAULT_ADRESS.toString())))
            .andExpect(jsonPath("$.[*].mapLink").value(hasItem(DEFAULT_MAP_LINK.toString())))
            .andExpect(jsonPath("$.[*].isGymnasium").value(hasItem(DEFAULT_IS_GYMNASIUM.booleanValue())))
            .andExpect(jsonPath("$.[*].residentClubName").value(hasItem(DEFAULT_RESIDENT_CLUB_NAME.toString())));
    }
}
