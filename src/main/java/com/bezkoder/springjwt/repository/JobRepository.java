package com.bezkoder.springjwt.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.bezkoder.springjwt.models.Jobs;

public interface JobRepository extends JpaRepository<Jobs,Long>{

	@Query("SELECT a FROM Jobs a WHERE a.job_id = :id")
	Jobs getJobById(Long id);

	@Modifying
	@Transactional
	@Query(value="DELETE FROM jobs",nativeQuery=true)
	void deleteAllById();

	@Modifying
	@Transactional
	@Query(value="DELETE FROM jobs WHERE status=?1",nativeQuery=true)
	void deleteAllByStatus(int status_id);

	@Query("SELECT a FROM Jobs a WHERE a.Status = :status_id")
	List<Jobs> getJobByStatusId(int status_id);
	
	@Query("SELECT a FROM Jobs a ")
	List<Jobs> findAllByPrice(Pageable pageable);
}
