package com.cvprofile.repository;

import com.cvprofile.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByProfileId(Long profileId);
    List<Event> findByProfileIdOrderByStartDateDesc(Long profileId);
}
