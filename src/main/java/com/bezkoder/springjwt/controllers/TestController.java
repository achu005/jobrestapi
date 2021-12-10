package com.bezkoder.springjwt.controllers;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bezkoder.springjwt.models.Jobs;
import com.bezkoder.springjwt.payload.response.MessageResponse;
import com.bezkoder.springjwt.payload.response.NotFoundException;
import com.bezkoder.springjwt.repository.JobRepository;
import com.bezkoder.springjwt.security.services.JobsService;
import com.bezkoder.springjwt.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
	
	private static final int PAGE_SIZE = 10;
	@Autowired
	JobRepository jobrepo;
	
	@Autowired
	JobsService jobsservice;
	
	@GetMapping("/all")
	public String allAccess() {
		return "Public Content.";
	}
	
	@GetMapping("/user")
	@PreAuthorize("hasRole('USER')")
	public String userAccess() {
		return "User Content.";
	}

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String adminAccess() {
		return "Admin Board.";
	}
	
	//get job by job id
	@GetMapping("/getjob/{job_id}")
	@PreAuthorize("hasRole('ADMIN') OR hasRole('USER')")
	public ResponseEntity<Jobs> get(@PathVariable Long job_id) {
		try {		
				Jobs Jobs = jobrepo.getJobById(job_id);
				return new ResponseEntity<Jobs>(Jobs,HttpStatus.OK);
			}
		catch(NoSuchElementException e) {
				return new ResponseEntity<Jobs>(HttpStatus.NOT_FOUND);
			}	
		
	}
	
	
	// Update job status by id
    @PutMapping("/modify_status/{job_id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateNote(@PathVariable(value = "job_id") Long bookId,
                           @Valid @RequestBody Jobs bookDetails) throws NotFoundException {

    	try {
    		Jobs book = jobrepo.findById(bookId)
			                .orElseThrow(() -> new NotFoundException(bookId));		
			    	book.setStatus(bookDetails.getStatus());
			    	book.setUpdated_date(LocalDateTime.now());			
			    	jobrepo.save(book);
			
			return ResponseEntity.ok(new MessageResponse("Job Status Updated successfully"));
    	}catch (Exception e) {
	    	return ResponseEntity.ok(new MessageResponse("Job Status could not be updated"));
	    }
    }
    
    
    // kill a job by id
    @PutMapping("/kill_job/{job_id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> KillJob(@PathVariable(value = "job_id") Long bookId) throws NotFoundException {

    	try {
    		Jobs book = jobrepo.findById(bookId)
			                .orElseThrow(() -> new NotFoundException(bookId));		
			    	book.setStatus(3);//kill a job
			    	book.setUpdated_date(LocalDateTime.now());			
			    	jobrepo.save(book);
			
			return ResponseEntity.ok(new MessageResponse("Job Killed"));
    	}catch (Exception e) {
	    	return ResponseEntity.ok(new MessageResponse("Job could not be killed"));
	    }
    }
    
    // rerun a job by id
    @PutMapping("/rerun_job/{job_id}")
    @PreAuthorize("hasRole('ADMIN') OR hasRole('USER')")
    public ResponseEntity<?> RErunJob(@PathVariable(value = "job_id") Long bookId) throws NotFoundException {

    	try {
    		Jobs book = jobrepo.findById(bookId)
			                .orElseThrow(() -> new NotFoundException(bookId));		
			    	book.setStatus(2);//rerun a job
			    	book.setUpdated_date(LocalDateTime.now());			
			    	jobrepo.save(book);
			
			return ResponseEntity.ok(new MessageResponse("Job has been rerun"));
    	}catch (Exception e) {
	    	return ResponseEntity.ok(new MessageResponse("Job could not be rerun"));
	    }
    }
    
    //delete all jobs simultaneously
    @DeleteMapping("/delete-alljobs")
	public ResponseEntity<?> deleteTutorial() {
	    try {
	    	jobrepo.deleteAllById();
	    	return ResponseEntity.ok(new MessageResponse("Jobs Deleted successfully"));
	    } catch (Exception e) {
	    	return ResponseEntity.ok(new MessageResponse("Jobs could not be deleted"));
	    }
	 }
    
    //delete all jobs simultaneously-where status is input
    @DeleteMapping("/delete-alljobs/{status_id}")
    @PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteTutorial(@PathVariable int status_id) {
	    try {
	    	jobrepo.deleteAllByStatus(status_id);
	    	return ResponseEntity.ok(new MessageResponse("Jobs Deleted successfully"));
	    } catch (Exception e) {
	    	return ResponseEntity.ok(new MessageResponse("Jobs could not be deleted"));
	    }
	 }
    
    
    
//    //get job by status id
//  	@GetMapping("/getjob_by_statusid/{status_id}")
//  	@PreAuthorize("hasRole('ADMIN') OR hasRole('USER')")
//  	public Object getStatus(@PathVariable int status_id) {
//  		try {		
//  				List<Jobs> jobs = jobrepo.getJobByStatusId(status_id);
//  				return jobs;
//  			}
//  		catch(NoSuchElementException e) {
//  				return new ResponseEntity<Jobs>(HttpStatus.NOT_FOUND);
//  			}	
//  		
//  	}
    
    //http://localhost:8080/employees?pageSize=5&pageNo=1
    @GetMapping("/alljobs")
    public ResponseEntity<List<Jobs>> getAllEmployees(
                        @RequestParam(defaultValue = "0") Integer pageNo, 
                        @RequestParam(defaultValue = "10") Integer pageSize,
                        @RequestParam(defaultValue = "0") Integer Status,
                        @RequestParam(defaultValue = "true") Boolean IsActiveFlag,
                        @RequestParam(defaultValue = "") LocalDateTime startDate,
                        @RequestParam(defaultValue = "") LocalDateTime endDate
                        ) 
    {
        List<Jobs> list = jobsservice.getAllEmployees(pageNo, pageSize);
        
        
        //filter on activeflag
        if(IsActiveFlag==true) {
        	list = list.stream()
        		    .filter(p -> p.isIsActiveFlag()==true).collect(Collectors.toList());
        }
        else if(IsActiveFlag==false) {
        	list = list.stream()
        		    .filter(p -> p.isIsActiveFlag()==false).collect(Collectors.toList());
        }
        
      //filter on status
        if(Status!=0) {
        	list = list.stream()
        		    .filter(p -> p.getStatus()==Status).collect(Collectors.toList());
        }
        
        //
        if(startDate!=null) {
        	 list = list.stream()
                     .filter(memberSkill -> memberSkill.getStart_date().isAfter(startDate) || memberSkill.getStart_date().isEqual(startDate))
                     .collect(Collectors.toList());
        }
        if(endDate!=null) {
       	 list = list.stream()
                    .filter(memberSkill -> memberSkill.getEnd_date().isBefore(endDate) || memberSkill.getStart_date().isEqual(endDate))
                    .collect(Collectors.toList());
       }
        
        if(startDate!=null && endDate!=null) {
       	 list = list.stream()
                 .filter(memberSkill -> memberSkill.getStart_date().isAfter(startDate) || memberSkill.getStart_date().isEqual(startDate))
                 .filter(memberSkill -> memberSkill.getEnd_date().isBefore(endDate) || memberSkill.getStart_date().isEqual(endDate))
                 .collect(Collectors.toList());
        }
        
        
        return new ResponseEntity<List<Jobs>>(list, new HttpHeaders(), HttpStatus.OK); 
    }
}
