package com.lfdq.clubplanner.service;

import com.lfdq.clubplanner.domain.User;
import com.lfdq.clubplanner.domain.UserExtraInfo;
import com.lfdq.clubplanner.domain.enumeration.UserType;
import com.lfdq.clubplanner.repository.UserExtraInfoRepository;
import com.lfdq.clubplanner.repository.search.UserExtraInfoSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing UserExtraInfo.
 */
@Service
@Transactional
public class UserExtraInfoService {

    private final Logger log = LoggerFactory.getLogger(UserExtraInfoService.class);
    
    @Inject
    private UserExtraInfoRepository userExtraInfoRepository;
    
    @Inject
    private UserExtraInfoSearchRepository userExtraInfoSearchRepository;
    
    
    
    /**
     * Save a userExtraInfo.
     * 
     * @param userExtraInfo the entity to save
     * @return the persisted entity
     */
    public UserExtraInfo create(String nickname, UserType userType, User user) {
        UserExtraInfo newUserExtraInfo = new UserExtraInfo();
        newUserExtraInfo.setNickname(nickname);
        newUserExtraInfo.setUserType(userType);
        newUserExtraInfo.setUser(user);
        UserExtraInfo result = save(newUserExtraInfo);
        log.debug("Created Information for User: {}", newUserExtraInfo);
        return result;
    }
    
    /**
     * Save a userExtraInfo.
     * 
     * @param userExtraInfo the entity to save
     * @return the persisted entity
     */
    public UserExtraInfo save(UserExtraInfo userExtraInfo) {
        log.debug("Request to save UserExtraInfo : {}", userExtraInfo);
        UserExtraInfo result = userExtraInfoRepository.save(userExtraInfo);
        userExtraInfoSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the userExtraInfos.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<UserExtraInfo> findAll() {
        log.debug("Request to get all UserExtraInfos");
        List<UserExtraInfo> result = userExtraInfoRepository.findAll();
        return result;
    }

    /**
     *  Get one userExtraInfo by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public UserExtraInfo findOne(Long id) {
        log.debug("Request to get UserExtraInfo : {}", id);
        UserExtraInfo userExtraInfo = userExtraInfoRepository.findOne(id);
        return userExtraInfo;
    }

    /**
     *  Delete the  userExtraInfo by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete UserExtraInfo : {}", id);
        userExtraInfoRepository.delete(id);
        userExtraInfoSearchRepository.delete(id);
    }

    /**
     * Search for the userExtraInfo corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<UserExtraInfo> search(String query) {
        log.debug("Request to search UserExtraInfos for query {}", query);
        return StreamSupport
            .stream(userExtraInfoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
