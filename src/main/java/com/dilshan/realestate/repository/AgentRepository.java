package com.dilshan.realestate.repository;

import com.dilshan.realestate.model.Agent;
import com.dilshan.realestate.model.enums.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Long> {
    List<Agent> findByIsVerifiedTrue();
    List<Agent> findByIsVerifiedFalse();
    List<Agent> findBySpecialization(Specialization specialization);
    List<Agent> findByIsAvailableTrue();

    @Query("SELECT a FROM Agent a WHERE " +
           "(:keyword IS NULL OR LOWER(a.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(a.serviceAreas) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:spec IS NULL OR a.specialization = :spec) AND " +
           "(:minRating IS NULL OR a.rating >= :minRating)")
    List<Agent> searchAgents(@Param("keyword") String keyword,
                             @Param("spec") Specialization spec,
                             @Param("minRating") Double minRating);
}
