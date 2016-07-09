package com.lfdq.clubplanner.repository;

import com.lfdq.clubplanner.domain.Club;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Club entity.
 */
@SuppressWarnings("unused")
public interface ClubRepository extends JpaRepository<Club,Long> {

}
