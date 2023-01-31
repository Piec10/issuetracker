package com.piec10.issuetracker;

import com.piec10.issuetracker.controller.IssueRequestFactory;
import com.piec10.issuetracker.controller.IssueRequestFactoryImpl;
import com.piec10.issuetracker.dao.IssueRepository;
import com.piec10.issuetracker.service.IssueService;
import com.piec10.issuetracker.service.IssueServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class IssueTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(IssueTrackerApplication.class, args);
	}
}
