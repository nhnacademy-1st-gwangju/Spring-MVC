package springmvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import springmvc.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@Controller
public class LoginController {
    private final UserRepository userRepository;

    public LoginController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String login(@CookieValue(value = "SESSION", required = false) String session,
                        Model model) {
        if (StringUtils.hasText(session)) {
            model.addAttribute("id", session);
            return "loginSuccess";
        } else {
            return "loginForm";
        }
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam("id") String id,
                          @RequestParam("pwd") String pwd,
                          HttpSession session,
                          ModelMap modelMap) {
        if (userRepository.matches(id, pwd)) {
            session.setAttribute("SESSION", "on");

            modelMap.put("id", session.getId());
            return "loginSuccess";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);

        if (!Objects.isNull(session)) {
            session.removeAttribute("SESSION");
        }
        return "index";
    }
}
