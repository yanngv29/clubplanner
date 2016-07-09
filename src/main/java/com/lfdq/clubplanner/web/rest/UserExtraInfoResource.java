package com.lfdq.clubplanner.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lfdq.clubplanner.domain.UserExtraInfo;
import com.lfdq.clubplanner.service.UserExtraInfoService;
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
 * REST controller for managing UserExtraInfo.
 */
@RestController
@RequestMapping("/api")
public class UserExtraInfoResource {

    private final Logger log = LoggerFactory.getLogger(UserExtraInfoResource.class);
        
    @Inject
    private UserExtraInfoService userExtraInfoService;
    
    /**
     * POST  /user-extra-infos : Create a new userExtraInfo.
     *
     * @param userExtraInfo the userExtraInfo to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userExtraInfo, or with status 400 (Bad Request) if the userExtraInfo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/user-extra-infos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserExtraInfo> createUserExtraInfo(@RequestBody UserExtraInfo userExtraInfo) throws URISyntaxException {
        log.debug("REST request to save UserExtraInfo : {}", userExtraInfo);
        if (userExtraInfo.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("userExtraInfo", "idexists", "A new userExtraInfo cannot already have an ID")).body(null);
        }
        UserExtraInfo result = userExtraInfoService.save(userExtraInfo);
        return ResponseEntity.created(new URI("/api/user-extra-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("userExtraInfo", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-extra-infos : Updates an existing userExtraInfo.
     *
     * @param userExtraInfo the userExtraInfo to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userExtraInfo,
     * or with status 400 (Bad Request) if the userExtraInfo is not valid,
     * or with status 500 (Internal Server Error) if the userExtraInfo couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/user-extra-infos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserExtraInfo> updateUserExtraInfo(@RequestBody UserExtraInfo userExtraInfo) throws URISyntaxException {
        log.debug("REST request to update UserExtraInfo : {}", userExtraInfo);
        if (userExtraInfo.getId() == null) {
            return createUserExtraInfo(userExtraInfo);
        }
        UserExtraInfo result = userExtraInfoService.save(userExtraInfo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("userExtraInfo", userExtraInfo.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-extra-infos : get all the userExtraInfos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of userExtraInfos in body
     */
    @RequestMapping(value = "/user-extra-infos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<UserExtraInfo> getAllUserExtraInfos() {
        log.debug("REST request to get all UserExtraInfos");
        return userExtraInfoService.findAll();
    }

    /**
     * GET  /user-extra-infos/:id : get the "id" userExtraInfo.
     *
     * @param id the id of the userExtraInfo to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userExtraInfo, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/user-extra-infos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserExtraInfo> getUserExtraInfo(@PathVariable Long id) {
        log.debug("REST request to get UserExtraInfo : {}", id);
        UserExtraInfo userExtraInfo = userExtraInfoService.findOne(id);
        return Optional.ofNullable(userExtraInfo)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /user-extra-infos/:id : delete the "id" userExtraInfo.
     *
     * @param id the id of the userExtraInfo to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/user-extra-infos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteUserExtraInfo(@PathVariable Long id) {
        log.debug("REST request to delete UserExtraInfo : {}", id);
        userExtraInfoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("userExtraInfo", id.toString())).build();
    }

    /**
     * SEARCH  /_search/user-extra-infos?query=:query : search for the userExtraInfo corresponding
     * to the query.
     *
     * @param query the query of the userExtraInfo search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/user-extra-infos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<UserExtraInfo> searchUserExtraInfos(@RequestParam String query) {
        log.debug("REST request to search UserExtraInfos for query {}", query);
        return userExtraInfoService.search(query);
    }


}
