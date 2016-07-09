package com.lfdq.clubplanner.repository;

import com.lfdq.clubplanner.domain.Site;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Site entity.
 */
@SuppressWarnings("unused")
public interface SiteRepository extends JpaRepository<Site,Long> {

}
