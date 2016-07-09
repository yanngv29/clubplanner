package com.lfdq.clubplanner.repository;

import com.lfdq.clubplanner.domain.ClubEvent;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ClubEvent entity.
 */
@SuppressWarnings("unused")
public interface ClubEventRepository extends JpaRepository<ClubEvent,Long> {

}
