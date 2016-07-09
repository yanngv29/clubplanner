package com.lfdq.clubplanner.repository.search;

import com.lfdq.clubplanner.domain.ClubEvent;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ClubEvent entity.
 */
public interface ClubEventSearchRepository extends ElasticsearchRepository<ClubEvent, Long> {
}
