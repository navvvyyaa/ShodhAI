package com.shodh.contest.repository;

import com.shodh.contest.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    
    @Query("SELECT s FROM Submission s WHERE s.contest.id = :contestId AND s.status = 'ACCEPTED' " +
           "ORDER BY s.submittedAt ASC")
    List<Submission> findAcceptedSubmissionsByContestOrderByTime(@Param("contestId") Long contestId);
    
    List<Submission> findByContestIdOrderBySubmittedAtDesc(Long contestId);
}
