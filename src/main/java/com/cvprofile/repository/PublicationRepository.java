package com.cvprofile.repository;

import com.cvprofile.entity.Publication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PublicationRepository extends JpaRepository<Publication, Long> {
    List<Publication> findByProfileId(Long profileId);
    List<Publication> findByProfileIdOrderByPublishedDateDesc(Long profileId);
}
