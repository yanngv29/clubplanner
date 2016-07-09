package com.lfdq.clubplanner.service;

import com.lfdq.clubplanner.domain.Club;
import com.lfdq.clubplanner.repository.ClubRepository;
import com.lfdq.clubplanner.repository.search.ClubSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Club.
 */
@Service
@Transactional
public class ClubService {

    private final Logger log = LoggerFactory.getLogger(ClubService.class);
    
    @Inject
    private ClubRepository clubRepository;
    
    @Inject
    private ClubSearchRepository clubSearchRepository;
    
    /**
     * Save a club.
     * 
     * @param club the entity to save
     * @return the persisted entity
     */
    public Club save(Club club) {
        log.debug("Request to save Club : {}", club);
        Club result = clubRepository.save(club);
        clubSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the clubs.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Club> findAll(Pageable pageable) {
        log.debug("Request to get all Clubs");
        Page<Club> result = clubRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one club by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Club findOne(Long id) {
        log.debug("Request to get Club : {}", id);
        Club club = clubRepository.findOne(id);
        return club;
    }

    /**
     *  Delete the  club by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Club : {}", id);
        clubRepository.delete(id);
        clubSearchRepository.delete(id);
    }

    /**
     * Search for the club corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Club> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Clubs for query {}", query);
        return clubSearchRepository.search(queryStringQuery(query), pageable);
    }
}
