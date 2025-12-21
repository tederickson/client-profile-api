package com.erickson.client_profile_api.repository;

import com.erickson.client_profile_api.model.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfileEntity, Long> {
}
