package com.lfdq.clubplanner.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lfdq.clubplanner.domain.Site;
import com.lfdq.clubplanner.service.SiteService;
import com.lfdq.clubplanner.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Site.
 */
@RestController
@RequestMapping("/api")
public class SiteResource {

    private final Logger log = LoggerFactory.getLogger(SiteResource.class);
        
    @Inject
    private SiteService siteService;
    
    /**
     * POST  /sites : Create a new site.
     *
     * @param site the site to create
     * @return the ResponseEntity with status 201 (Created) and with body the new site, or with status 400 (Bad Request) if the site has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/sites",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Site> createSite(@Valid @RequestBody Site site) throws URISyntaxException {
        log.debug("REST request to save Site : {}", site);
        if (site.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("site", "idexists", "A new site cannot already have an ID")).body(null);
        }
        Site result = siteService.save(site);
        return ResponseEntity.created(new URI("/api/sites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("site", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sites : Updates an existing site.
     *
     * @param site the site to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated site,
     * or with status 400 (Bad Request) if the site is not valid,
     * or with status 500 (Internal Server Error) if the site couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/sites",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Site> updateSite(@Valid @RequestBody Site site) throws URISyntaxException {
        log.debug("REST request to update Site : {}", site);
        if (site.getId() == null) {
            return createSite(site);
        }
        Site result = siteService.save(site);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("site", site.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sites : get all the sites.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of sites in body
     */
    @RequestMapping(value = "/sites",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Site> getAllSites() {
        log.debug("REST request to get all Sites");
        return siteService.findAll();
    }

    /**
     * GET  /sites/:id : get the "id" site.
     *
     * @param id the id of the site to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the site, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/sites/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Site> getSite(@PathVariable Long id) {
        log.debug("REST request to get Site : {}", id);
        Site site = siteService.findOne(id);
        return Optional.ofNullable(site)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /sites/:id : delete the "id" site.
     *
     * @param id the id of the site to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/sites/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSite(@PathVariable Long id) {
        log.debug("REST request to delete Site : {}", id);
        siteService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("site", id.toString())).build();
    }

    /**
     * SEARCH  /_search/sites?query=:query : search for the site corresponding
     * to the query.
     *
     * @param query the query of the site search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/sites",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Site> searchSites(@RequestParam String query) {
        log.debug("REST request to search Sites for query {}", query);
        return siteService.search(query);
    }


}
