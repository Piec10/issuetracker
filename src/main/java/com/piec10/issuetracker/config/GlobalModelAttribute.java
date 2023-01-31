package com.piec10.issuetracker.config;

import com.piec10.issuetracker.controller.issue.IssueController;
import com.piec10.issuetracker.util.Priorities;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(assignableTypes = {IssueController.class})
public class GlobalModelAttribute {

    @Bean
    public Priorities priorities(){
        return new Priorities();
    }

    @ModelAttribute("priorities")
    public Priorities setPrioritiesAttribute(){
        return priorities();
    }
}
