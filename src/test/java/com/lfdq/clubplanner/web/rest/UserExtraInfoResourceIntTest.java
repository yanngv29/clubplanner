package com.lfdq.clubplanner.web.rest;

import com.lfdq.clubplanner.ClubplannerApp;
import com.lfdq.clubplanner.domain.UserExtraInfo;
import com.lfdq.clubplanner.repository.UserExtraInfoRepository;
import com.lfdq.clubplanner.repository.search.UserExtraInfoSearchRepository;

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

import com.lfdq.clubplanner.domain.enumeration.UserType;

/**
 * Test class for the UserExtraInfoResource REST controller.
 *
 * @see UserExtraInfoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ClubplannerApp.class)
@WebAppConfiguration
@IntegrationTest
public class UserExtraInfoResourceIntTest {

    private static final String DEFAULT_NICKNAME = "AAAAA";
    private static final String UPDATED_NICKNAME = "BBBBB";

    private static final UserType DEFAULT_USER_TYPE = UserType.PLAYER;
    private static final UserType UPDATED_USER_TYPE = UserType.LEADER;

    @Inject
    private UserExtraInfoRepository userExtraInfoRepository;

    @Inject
    private UserExtraInfoSearchRepository userExtraInfoSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restUserExtraInfoMockMvc;

    private UserExtraInfo userExtraInfo;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserExtraInfoResource userExtraInfoResource = new UserExtraInfoResource();
        ReflectionTestUtils.setField(userExtraInfoResource, "userExtraInfoSearchRepository", userExtraInfoSearchRepository);
        ReflectionTestUtils.setField(userExtraInfoResource, "userExtraInfoRepository", userExtraInfoRepository);
        this.restUserExtraInfoMockMvc = MockMvcBuilders.standaloneSetup(userExtraInfoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        userExtraInfoSearchRepository.deleteAll();
        userExtraInfo = new UserExtraInfo();
        userExtraInfo.setNickname(DEFAULT_NICKNAME);
        userExtraInfo.setUserType(DEFAULT_USER_TYPE);
    }

    @Test
    @Transactional
    public void createUserExtraInfo() throws Exception {
        int databaseSizeBeforeCreate = userExtraInfoRepository.findAll().size();

        // Create the UserExtraInfo

        restUserExtraInfoMockMvc.perform(post("/api/user-extra-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userExtraInfo)))
                .andExpect(status().isCreated());

        // Validate the UserExtraInfo in the database
        List<UserExtraInfo> userExtraInfos = userExtraInfoRepository.findAll();
        assertThat(userExtraInfos).hasSize(databaseSizeBeforeCreate + 1);
        UserExtraInfo testUserExtraInfo = userExtraInfos.get(userExtraInfos.size() - 1);
        assertThat(testUserExtraInfo.getNickname()).isEqualTo(DEFAULT_NICKNAME);
        assertThat(testUserExtraInfo.getUserType()).isEqualTo(DEFAULT_USER_TYPE);

        // Validate the UserExtraInfo in ElasticSearch
        UserExtraInfo userExtraInfoEs = userExtraInfoSearchRepository.findOne(testUserExtraInfo.getId());
        assertThat(userExtraInfoEs).isEqualToComparingFieldByField(testUserExtraInfo);
    }

    @Test
    @Transactional
    public void getAllUserExtraInfos() throws Exception {
        // Initialize the database
        userExtraInfoRepository.saveAndFlush(userExtraInfo);

        // Get all the userExtraInfos
        restUserExtraInfoMockMvc.perform(get("/api/user-extra-infos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(userExtraInfo.getId().intValue())))
                .andExpect(jsonPath("$.[*].nickname").value(hasItem(DEFAULT_NICKNAME.toString())))
                .andExpect(jsonPath("$.[*].userType").value(hasItem(DEFAULT_USER_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getUserExtraInfo() throws Exception {
        // Initialize the database
        userExtraInfoRepository.saveAndFlush(userExtraInfo);

        // Get the userExtraInfo
        restUserExtraInfoMockMvc.perform(get("/api/user-extra-infos/{id}", userExtraInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(userExtraInfo.getId().intValue()))
            .andExpect(jsonPath("$.nickname").value(DEFAULT_NICKNAME.toString()))
            .andExpect(jsonPath("$.userType").value(DEFAULT_USER_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUserExtraInfo() throws Exception {
        // Get the userExtraInfo
        restUserExtraInfoMockMvc.perform(get("/api/user-extra-infos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserExtraInfo() throws Exception {
        // Initialize the database
        userExtraInfoRepository.saveAndFlush(userExtraInfo);
        userExtraInfoSearchRepository.save(userExtraInfo);
        int databaseSizeBeforeUpdate = userExtraInfoRepository.findAll().size();

        // Update the userExtraInfo
        UserExtraInfo updatedUserExtraInfo = new UserExtraInfo();
        updatedUserExtraInfo.setId(userExtraInfo.getId());
        updatedUserExtraInfo.setNickname(UPDATED_NICKNAME);
        updatedUserExtraInfo.setUserType(UPDATED_USER_TYPE);

        restUserExtraInfoMockMvc.perform(put("/api/user-extra-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedUserExtraInfo)))
                .andExpect(status().isOk());

        // Validate the UserExtraInfo in the database
        List<UserExtraInfo> userExtraInfos = userExtraInfoRepository.findAll();
        assertThat(userExtraInfos).hasSize(databaseSizeBeforeUpdate);
        UserExtraInfo testUserExtraInfo = userExtraInfos.get(userExtraInfos.size() - 1);
        assertThat(testUserExtraInfo.getNickname()).isEqualTo(UPDATED_NICKNAME);
        assertThat(testUserExtraInfo.getUserType()).isEqualTo(UPDATED_USER_TYPE);

        // Validate the UserExtraInfo in ElasticSearch
        UserExtraInfo userExtraInfoEs = userExtraInfoSearchRepository.findOne(testUserExtraInfo.getId());
        assertThat(userExtraInfoEs).isEqualToComparingFieldByField(testUserExtraInfo);
    }

    @Test
    @Transactional
    public void deleteUserExtraInfo() throws Exception {
        // Initialize the database
        userExtraInfoRepository.saveAndFlush(userExtraInfo);
        userExtraInfoSearchRepository.save(userExtraInfo);
        int databaseSizeBeforeDelete = userExtraInfoRepository.findAll().size();

        // Get the userExtraInfo
        restUserExtraInfoMockMvc.perform(delete("/api/user-extra-infos/{id}", userExtraInfo.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean userExtraInfoExistsInEs = userExtraInfoSearchRepository.exists(userExtraInfo.getId());
        assertThat(userExtraInfoExistsInEs).isFalse();

        // Validate the database is empty
        List<UserExtraInfo> userExtraInfos = userExtraInfoRepository.findAll();
        assertThat(userExtraInfos).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchUserExtraInfo() throws Exception {
        // Initialize the database
        userExtraInfoRepository.saveAndFlush(userExtraInfo);
        userExtraInfoSearchRepository.save(userExtraInfo);

        // Search the userExtraInfo
        restUserExtraInfoMockMvc.perform(get("/api/_search/user-extra-infos?query=id:" + userExtraInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userExtraInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].nickname").value(hasItem(DEFAULT_NICKNAME.toString())))
            .andExpect(jsonPath("$.[*].userType").value(hasItem(DEFAULT_USER_TYPE.toString())));
    }
}
