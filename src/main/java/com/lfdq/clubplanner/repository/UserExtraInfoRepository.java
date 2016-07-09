package com.lfdq.clubplanner.repository;

import com.lfdq.clubplanner.domain.UserExtraInfo;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the UserExtraInfo entity.
 */
@SuppressWarnings("unused")
public interface UserExtraInfoRepository extends JpaRepository<UserExtraInfo,Long> {

}
