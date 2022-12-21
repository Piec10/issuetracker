package com.piec10.issuetracker.controller;

import com.piec10.issuetracker.entity.User;
import com.piec10.issuetracker.form.FormPasswordChange;
import com.piec10.issuetracker.form.FormUser;
import com.piec10.issuetracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

import static com.piec10.issuetracker.config.GlobalRolesAndOwnerCheckMethods.*;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    UserService userService;


//    private Logger logger = Logger.getLogger(getClass().getName());

    @GetMapping("/")
    public String getDashboard(){

        return "redirect:/dashboard/projects";
    }

    @GetMapping("/profile")
    public String getProfile(Principal principal, Model model){

        User currentUser =  userService.findByUsername(principal.getName());

        model.addAttribute("user", currentUser);

        return "dashboard/profile";
    }

    @GetMapping("/changePassword")
    public String showChangePassword(@RequestParam("userId") String userId, HttpServletRequest request, Model model) {

        if(isGuest(request)) return "redirect:/access-denied";

        User user = userService.findByUsername(userId);

        if(user == null) return "redirect:/dashboard/projects";

        if(isAdminOrOwner(user,request)){

            FormPasswordChange formPasswordChange = new FormPasswordChange();
            formPasswordChange.setUsername(user.getUsername());

            model.addAttribute("formPassword", formPasswordChange);

            return "dashboard/password-change";
        }
        else return "redirect:/access-denied";
    }

    @PostMapping("/processPasswordChange")
    public String processPasswordChange(@Valid @ModelAttribute("formPassword") FormPasswordChange formPassword,
                                        BindingResult theBindingResult,
                                        Model model) {

        // form validation
        if (theBindingResult.hasErrors()){

            return "dashboard/password-change";
        }

        return "redirect:/dashboard/projects";
    }
}
