package com.lfdq.clubplanner.service;

import com.lfdq.clubplanner.domain.Team;
import com.lfdq.clubplanner.repository.TeamRepository;
import com.lfdq.clubplanner.repository.search.TeamSearchRepository;
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
 * Service Implementation for managing Team.
 */
@Service
@Transactional
public class TeamService {

    private final Logger log = LoggerFactory.getLogger(TeamService.class);
    
    @Inject
    private TeamRepository teamRepository;
    
    @Inject
    private TeamSearchRepository teamSearchRepository;
    
    /**
     * Save a team.
     * 
     * @param team the entity to save
     * @return the persisted entity
     */
    public Team save(Team team) {
        log.debug("Request to save Team : {}", team);
        Team result = teamRepository.save(team);
        teamSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the teams.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Team> findAll() {
        log.debug("Request to get all Teams");
        List<Team> result = teamRepository.findAll();
        return result;
    }

    /**
     *  Get one team by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Team findOne(Long id) {
        log.debug("Request to get Team : {}", id);
        Team team = teamRepository.findOne(id);
        return team;
    }

    /**
     *  Delete the  team by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Team : {}", id);
        teamRepository.delete(id);
        teamSearchRepository.delete(id);
    }

    /**
     * Search for the team corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Team> search(String query) {
        log.debug("Request to search Teams for query {}", query);
        return StreamSupport
            .stream(teamSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
