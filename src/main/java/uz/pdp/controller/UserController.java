package uz.pdp.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import uz.pdp.doa.UserDao;
import uz.pdp.domain.UserEntity;

@Controller
public class UserController {

    private final UserDao userDao;

    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    @GetMapping("/signup")
    public String pageSignUp(){
        return "signup";
    }

    @GetMapping("/login")
    public String pageLogin(){
        return "login";
    }

    @PostMapping("/signup")
    public String signUp(@ModelAttribute("user") UserEntity user){
        userDao.saveUser(user);
        return "redirect:/home";
    }

    @GetMapping("/user")
    @ResponseBody
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String userPage(){
        return "USER_PAGE";
    }
    @GetMapping("/admin")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public String adminPage(){
        return "ADMIN_PAGE";
    }

    @GetMapping("/super-admin")
    @ResponseBody
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String superAdminPage(){
        return "SUPER_ADMIN_PAGE";
    }

}
