package com.lfdq.clubplanner.repository.search;

import com.lfdq.clubplanner.domain.Club;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Club entity.
 */
public interface ClubSearchRepository extends ElasticsearchRepository<Club, Long> {
}
