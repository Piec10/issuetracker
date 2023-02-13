package com.piec10.issuetracker.controller.issue;

import com.piec10.issuetracker.controller.request.ProcessFormRequest;
import com.piec10.issuetracker.service.IssueService;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;

import static com.piec10.issuetracker.util.GlobalRolesAndOwnerCheckMethods.isGuest;

public class ProcessIssueFormRequest extends IssueFormRequest implements ProcessFormRequest {

    private BindingResult bindingResult;
    private HttpServletRequest request;

    public ProcessIssueFormRequest(IssueService issueService,
                                   BindingResult bindingResult,
                                   HttpServletRequest request, Model model) {

        super(issueService, model);
        this.bindingResult = bindingResult;
        this.request = request;
    }


    @Override
    public boolean formHasErrors() {
        return bindingResult.hasErrors();
    }

    @Override
    public String whenFormHasErrors() {
        addModelAttributes();
        return toIssueForm();
    }

    @Override
    public boolean isGuestUser() {
        return isGuest(request);
    }

    @Override
    public String whenIsGuestUser() {
        model.addAttribute("guestUserError", "Sorry, Guest user cannot create new issues.");
        return toIssueForm();
    }

    @Override
    public boolean isNew() {
        return formIssue.getId() == 0;
    }

    @Override
    public String createNew() {
        return null;
    }

    @Override
    public String update() {
        return null;
    }
}
