package com.bezkoder.springjwt.security.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.bezkoder.springjwt.models.Jobs;
import com.bezkoder.springjwt.repository.JobRepository;

@Service
public class JobsService {

	@Autowired
    JobRepository repository;
     
    public List<Jobs> getAllEmployees(Integer pageNo, Integer pageSize)
    {
        Pageable paging = PageRequest.of(pageNo, pageSize);
 
        Page<Jobs> pagedResult = repository.findAll(paging);
         
        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<Jobs>();
        }
    }
}
