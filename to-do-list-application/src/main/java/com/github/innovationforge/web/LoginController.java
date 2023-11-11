package com.github.innovationforge.web;

import lombok.RequiredArgsConstructor;

import com.github.innovationforge.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session) {
        // Use your UserService to authenticate the user
        // For simplicity, assume a UserService class with a method authenticate(username, password)
        if (userService.authenticate(username, password)) {
            // If authentication is successful, store user information in the session
            session.setAttribute("username", username);
            return "redirect:/dashboard"; // Redirect to a dashboard page after login
        } else {
            // If authentication fails, show the login form with an error message
            return "redirect:/login?error";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // Invalidate the session and redirect to the login page
        session.invalidate();
        return "redirect:/login";
    }
}
