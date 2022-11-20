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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Objects;

import static com.nhnacademy.nhnmart.domain.RoleUser.ROLE_USER;

@Controller
@RequestMapping("/cs")
@RequiredArgsConstructor
public class LoginController {
    private final UserRepository userRepository;

    private static final String SESSION = "SESSION";

    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model) {
        HttpSession httpSession = request.getSession(false);

        if (Objects.nonNull(httpSession) && httpSession.getAttribute(SESSION) != null) {
            String session = httpSession.getAttribute(SESSION).toString();
            model.addAttribute("id", session);
            return "redirect:/cs";
        } else {
            return "loginForm";
        }
    }

    @PostMapping("/login")
    public String doLogin(@Valid @ModelAttribute LoginRequest request,
                          BindingResult bindingResult,
                          HttpSession session,
                          ModelMap modelMap) {
        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        checkUserExist(request.getId(), request.getPwd());

        User user = userRepository.getUser(request.getId());
        session.setAttribute(SESSION, user.getRole().toString());
        session.setAttribute("NAME", user.getName());

        modelMap.put("id", session);

        return goToMainPageByUserRole(user);
    }

    private void checkUserExist(String id, String pwd) {
        if (!userRepository.matches(id, pwd)) {
            throw new UserNotFoundException(id);
        }
    }

    private String goToMainPageByUserRole(User user) {
        if (user.getRole().equals(ROLE_USER)) {
            return "redirect:/cs";
        }
        return "redirect:/cs/admin";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (!Objects.isNull(session)) {
            session.removeAttribute(SESSION);
            session.removeAttribute("NAME");
        }
        return "index";
    }
}
