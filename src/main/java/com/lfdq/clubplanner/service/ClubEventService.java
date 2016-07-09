package com.lfdq.clubplanner.service;

import com.lfdq.clubplanner.domain.ClubEvent;
import com.lfdq.clubplanner.repository.ClubEventRepository;
import com.lfdq.clubplanner.repository.search.ClubEventSearchRepository;
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
 * Service Implementation for managing ClubEvent.
 */
@Service
@Transactional
public class ClubEventService {

    private final Logger log = LoggerFactory.getLogger(ClubEventService.class);
    
    @Inject
    private ClubEventRepository clubEventRepository;
    
    @Inject
    private ClubEventSearchRepository clubEventSearchRepository;
    
    /**
     * Save a clubEvent.
     * 
     * @param clubEvent the entity to save
     * @return the persisted entity
     */
    public ClubEvent save(ClubEvent clubEvent) {
        log.debug("Request to save ClubEvent : {}", clubEvent);
        ClubEvent result = clubEventRepository.save(clubEvent);
        clubEventSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the clubEvents.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<ClubEvent> findAll() {
        log.debug("Request to get all ClubEvents");
        List<ClubEvent> result = clubEventRepository.findAllWithEagerRelationships();
        return result;
    }

    /**
     *  Get one clubEvent by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ClubEvent findOne(Long id) {
        log.debug("Request to get ClubEvent : {}", id);
        ClubEvent clubEvent = clubEventRepository.findOneWithEagerRelationships(id);
        return clubEvent;
    }

    /**
     *  Delete the  clubEvent by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ClubEvent : {}", id);
        clubEventRepository.delete(id);
        clubEventSearchRepository.delete(id);
    }

    /**
     * Search for the clubEvent corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ClubEvent> search(String query) {
        log.debug("Request to search ClubEvents for query {}", query);
        return StreamSupport
            .stream(clubEventSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
