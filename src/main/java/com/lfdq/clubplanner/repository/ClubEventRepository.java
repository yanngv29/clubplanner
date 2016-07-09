package com.lfdq.clubplanner.repository;

import com.lfdq.clubplanner.domain.ClubEvent;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the ClubEvent entity.
 */
@SuppressWarnings("unused")
public interface ClubEventRepository extends JpaRepository<ClubEvent,Long> {

    @Query("select distinct clubEvent from ClubEvent clubEvent left join fetch clubEvent.registrants")
    List<ClubEvent> findAllWithEagerRelationships();

    @Query("select clubEvent from ClubEvent clubEvent left join fetch clubEvent.registrants where clubEvent.id =:id")
    ClubEvent findOneWithEagerRelationships(@Param("id") Long id);

}
