package com.piec10.issuetracker.controller.issue;

import com.piec10.issuetracker.service.IssueService;
import org.springframework.ui.Model;

public abstract class IssueFormRequest extends IssueRequest {

    protected IssueService issueService;
    protected Model model;
}
