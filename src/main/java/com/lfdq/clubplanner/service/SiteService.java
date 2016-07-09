package com.lfdq.clubplanner.service;

import com.lfdq.clubplanner.domain.Site;
import com.lfdq.clubplanner.repository.SiteRepository;
import com.lfdq.clubplanner.repository.search.SiteSearchRepository;
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
 * Service Implementation for managing Site.
 */
@Service
@Transactional
public class SiteService {

    private final Logger log = LoggerFactory.getLogger(SiteService.class);
    
    @Inject
    private SiteRepository siteRepository;
    
    @Inject
    private SiteSearchRepository siteSearchRepository;
    
    /**
     * Save a site.
     * 
     * @param site the entity to save
     * @return the persisted entity
     */
    public Site save(Site site) {
        log.debug("Request to save Site : {}", site);
        Site result = siteRepository.save(site);
        siteSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the sites.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Site> findAll() {
        log.debug("Request to get all Sites");
        List<Site> result = siteRepository.findAll();
        return result;
    }

    /**
     *  Get one site by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Site findOne(Long id) {
        log.debug("Request to get Site : {}", id);
        Site site = siteRepository.findOne(id);
        return site;
    }

    /**
     *  Delete the  site by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Site : {}", id);
        siteRepository.delete(id);
        siteSearchRepository.delete(id);
    }

    /**
     * Search for the site corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Site> search(String query) {
        log.debug("Request to search Sites for query {}", query);
        return StreamSupport
            .stream(siteSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
