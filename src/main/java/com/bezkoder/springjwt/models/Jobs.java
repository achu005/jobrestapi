package com.bezkoder.springjwt.models;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Entity
@Table(	name = "jobs")
public class Jobs {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long job_id;
	private String description;
	private int Status;
	private LocalDateTime StartDate;
	private LocalDateTime End_date;
	private LocalDateTime Updated_date;
	private boolean IsActiveFlag;
	
	@ElementCollection
    @CollectionTable(name = "job_errors", joinColumns = @JoinColumn(name = "job_id"))
    private List<Integer> errors;
	
	
	public Jobs() {
		super();
		// TODO Auto-generated constructor stub
	}



	public Jobs(String description, int status, LocalDateTime start_date, LocalDateTime end_date,
			LocalDateTime updated_date, boolean isActiveFlag, List<Integer> errors) {
		super();
		this.description = description;
		Status = status;
		StartDate = start_date;
		End_date = end_date;
		Updated_date = updated_date;
		IsActiveFlag = isActiveFlag;
		this.errors = errors;
	}



	public Long getJob_id() {
		return job_id;
	}



	public void setJob_id(Long job_id) {
		this.job_id = job_id;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	public int getStatus() {
		return Status;
	}



	public void setStatus(int status) {
		Status = status;
	}



	public LocalDateTime getStart_date() {
		return StartDate;
	}



	public void setStart_date(LocalDateTime start_date) {
		StartDate = start_date;
	}



	public LocalDateTime getEnd_date() {
		return End_date;
	}



	public void setEnd_date(LocalDateTime end_date) {
		End_date = end_date;
	}



	public LocalDateTime getUpdated_date() {
		return Updated_date;
	}



	public void setUpdated_date(LocalDateTime updated_date) {
		Updated_date = updated_date;
	}



	public boolean isIsActiveFlag() {
		return IsActiveFlag;
	}



	public void setIsActiveFlag(boolean isActiveFlag) {
		IsActiveFlag = isActiveFlag;
	}



	public List<Integer> getErrors() {
		return errors;
	}



	public void setErrors(List<Integer> errors) {
		this.errors = errors;
	}
	
	
	
	
	
}
