package uz.pdp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import uz.pdp.doa.UserDao;
import uz.pdp.domain.User;

@Controller
@ResponseBody
public class UserController {

    private final UserDao userDao;

    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    @GetMapping("/users/save")
    public String save() {
        final User user = User.builder()
                .email("emil@gmail.com")
                .username("some-name")
                .password("some-password")
                .build();
        userDao.saveUser(user);
        return user.toString();
    }

    @GetMapping("/users/update/{userId}")
    public String update(@PathVariable("userId") Long userId) {
        final User user = User.builder()
                .id(userId)
                .email("emil2@gmail.com")
                .username("some-name2")
                .password("some-password2")
                .build();
        userDao.updateUser(user);
        return user.toString();
    }

    @GetMapping("/users/delete/{userId}")
    public String delete(@PathVariable("userId") Long userId) {
        userDao.deleteUser(userId);
        return "redirect:/";
    }

    @GetMapping("/users/{id}")
    public String getUserById(@PathVariable("id") Long id) {
        User user = userDao.findUserById(id);
        return user.toString();
    }

    @GetMapping("/users/all")
    public String getAllUsers() {
        return userDao.findAllUsers().toString();
    }

    @GetMapping("/users/simple")
    public String getSimpleUsers() {
        final User user = User.builder()
                .email("simple@gmail.com")
                .username("some-simple-name")
                .password("simple-password")
                .build();
        userDao.saveWithSimpleJdbc(user);
        return user.toString();
    }

}
