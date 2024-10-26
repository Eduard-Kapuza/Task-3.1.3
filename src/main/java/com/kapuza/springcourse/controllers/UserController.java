package com.kapuza.springcourse.controllers;

import com.kapuza.springcourse.model.User;
import com.kapuza.springcourse.services.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/users")
public class UserController {

    private final UserServiceImpl userServiceImpl;

    @Autowired
    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    //Get user or users
    @GetMapping("/")
    public String getAllUsers(Model model) {
        model.addAttribute("listUsers", userServiceImpl.getAllUsers());
        return "users";
    }

    @GetMapping("/{id}")
    public String userById(@PathVariable("id") int id, Model model) {
        User user = userServiceImpl.getUserById(id);
        System.out.println(user);
        model.addAttribute("getUserById", user);
        return "userById";
    }

    @GetMapping("/getAmountUsers")
    public String showAmountUsers(ModelMap model) {
        model.addAttribute("messages", userServiceImpl.getAmountUsers());
        return "getAmountUsers";
    }

    //Create
    @GetMapping("/newUser")
    public String addUser(@ModelAttribute("user") User user) {
        return "/newUser";
    }
    @PostMapping("/")
    public String createUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/newUser";
        }
        userServiceImpl.saveUser(user);
        return "redirect:/users/";
    }

    //Update(edit)
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("user", userServiceImpl.getUserById(id));
        return "/edit";
    }

    @PutMapping("/{id}")
    public String upDate(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, @PathVariable("id") int id) {
        if(bindingResult.hasErrors()){
            return "/edit";
        }
        userServiceImpl.updateUser(user);
        return "redirect:/users/";
    }

    // DELETE
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        userServiceImpl.deleteUser(userServiceImpl.getUserById(id));
        return "redirect:/users/";
    }
}