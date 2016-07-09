package com.lfdq.clubplanner.repository.search;

import com.lfdq.clubplanner.domain.UserExtraInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the UserExtraInfo entity.
 */
public interface UserExtraInfoSearchRepository extends ElasticsearchRepository<UserExtraInfo, Long> {
}
