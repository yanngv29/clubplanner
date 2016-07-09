package com.lfdq.clubplanner.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lfdq.clubplanner.domain.ClubEvent;
import com.lfdq.clubplanner.service.ClubEventService;
import com.lfdq.clubplanner.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing ClubEvent.
 */
@RestController
@RequestMapping("/api")
public class ClubEventResource {

    private final Logger log = LoggerFactory.getLogger(ClubEventResource.class);
        
    @Inject
    private ClubEventService clubEventService;
    
    /**
     * POST  /club-events : Create a new clubEvent.
     *
     * @param clubEvent the clubEvent to create
     * @return the ResponseEntity with status 201 (Created) and with body the new clubEvent, or with status 400 (Bad Request) if the clubEvent has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/club-events",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ClubEvent> createClubEvent(@RequestBody ClubEvent clubEvent) throws URISyntaxException {
        log.debug("REST request to save ClubEvent : {}", clubEvent);
        if (clubEvent.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("clubEvent", "idexists", "A new clubEvent cannot already have an ID")).body(null);
        }
        ClubEvent result = clubEventService.save(clubEvent);
        return ResponseEntity.created(new URI("/api/club-events/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("clubEvent", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /club-events : Updates an existing clubEvent.
     *
     * @param clubEvent the clubEvent to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated clubEvent,
     * or with status 400 (Bad Request) if the clubEvent is not valid,
     * or with status 500 (Internal Server Error) if the clubEvent couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/club-events",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ClubEvent> updateClubEvent(@RequestBody ClubEvent clubEvent) throws URISyntaxException {
        log.debug("REST request to update ClubEvent : {}", clubEvent);
        if (clubEvent.getId() == null) {
            return createClubEvent(clubEvent);
        }
        ClubEvent result = clubEventService.save(clubEvent);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("clubEvent", clubEvent.getId().toString()))
            .body(result);
    }

    /**
     * GET  /club-events : get all the clubEvents.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of clubEvents in body
     */
    @RequestMapping(value = "/club-events",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ClubEvent> getAllClubEvents() {
        log.debug("REST request to get all ClubEvents");
        return clubEventService.findAll();
    }

    /**
     * GET  /club-events/:id : get the "id" clubEvent.
     *
     * @param id the id of the clubEvent to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the clubEvent, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/club-events/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ClubEvent> getClubEvent(@PathVariable Long id) {
        log.debug("REST request to get ClubEvent : {}", id);
        ClubEvent clubEvent = clubEventService.findOne(id);
        return Optional.ofNullable(clubEvent)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /club-events/:id : delete the "id" clubEvent.
     *
     * @param id the id of the clubEvent to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/club-events/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteClubEvent(@PathVariable Long id) {
        log.debug("REST request to delete ClubEvent : {}", id);
        clubEventService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("clubEvent", id.toString())).build();
    }

    /**
     * SEARCH  /_search/club-events?query=:query : search for the clubEvent corresponding
     * to the query.
     *
     * @param query the query of the clubEvent search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/club-events",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ClubEvent> searchClubEvents(@RequestParam String query) {
        log.debug("REST request to search ClubEvents for query {}", query);
        return clubEventService.search(query);
    }


}
