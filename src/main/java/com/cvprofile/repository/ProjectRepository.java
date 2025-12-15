package com.cvprofile.repository;

import com.cvprofile.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByProfileId(Long profileId);
    List<Project> findByProfileIdOrderByStartDateDesc(Long profileId);
}
