package com.nhnacademy.nhnmart.controller;

import com.nhnacademy.nhnmart.domain.User;
import com.nhnacademy.nhnmart.domain.dto.LoginRequest;
import com.nhnacademy.nhnmart.exception.UserNotFoundException;
import com.nhnacademy.nhnmart.exception.ValidationFailedException;
import com.nhnacademy.nhnmart.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Objects;

import static com.nhnacademy.nhnmart.domain.RoleUser.ROLE_USER;

@Controller
@RequestMapping("/cs")
@RequiredArgsConstructor
public class LoginController {
    private final UserRepository userRepository;

    @GetMapping("/login")
    public String login(@CookieValue(value = "SESSION", required = false) String session,
                        Model model) {
        if (StringUtils.hasText(session)) {
            model.addAttribute("id", session);
            return "index";
        } else {
            return "loginForm";
        }
    }

    @PostMapping("/login")
    public String doLogin(@Valid @ModelAttribute LoginRequest request,
                          HttpSession session,
                          ModelMap modelMap,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        checkUserExist(request.getId(), request.getPwd());

        User user = userRepository.getUser(request.getId());
        session.setAttribute("SESSION", user.getRole().toString());
        session.setAttribute("NAME", user.getName());

        modelMap.put("id", session);

        return goMainPage(user);
    }

    private void checkUserExist(String id, String pwd) {
        if (!userRepository.matches(id, pwd)) {
            throw new UserNotFoundException(id);
        }
    }

    private String goMainPage(User user) {
        if (user.getRole().equals(ROLE_USER)) {
            return "redirect:/cs";
        }
        return "redirect:/cs/admin";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);

        if (!Objects.isNull(session)) {
            session.removeAttribute("SESSION");
            session.removeAttribute("NAME");
        }
        return "index";
    }
}
