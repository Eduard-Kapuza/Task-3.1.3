package com.kapuza.springSecurity.controllers;

import com.kapuza.springSecurity.models.Person;
import com.kapuza.springSecurity.models.Role;
import com.kapuza.springSecurity.security.PersonDetails;
import com.kapuza.springSecurity.services.personService.PersonServiceImpl;
import com.kapuza.springSecurity.services.roleService.RoleServiceImpl;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/persons")
public class UserController {

    private final RoleServiceImpl roleServiceImpl;

    private final PersonServiceImpl personServiceImpl;

    @Autowired
    public UserController(RoleServiceImpl roleServiceImpl,
                          PersonServiceImpl personServiceImpl) {
        this.roleServiceImpl = roleServiceImpl;
        this.personServiceImpl = personServiceImpl;

    }

    @GetMapping("/")
    public String userPage(Model model) {
        System.out.println(PersonDetails.getPerson());
        model.addAttribute("person", PersonDetails.getPerson());
        return "/user/personById";
    }

    /**
     * Update(edit)
     */
    @GetMapping("/{id}")
    public String edit(@PathVariable("id") int id, Model model) {
        Person person = personServiceImpl.findById(id);
        List<Role> listRoles = List.of(roleServiceImpl.findByRoleName("ROLE_USER").get());
        model.addAttribute("person", person);
        model.addAttribute("listRoles", listRoles);
        log.info("\u001B[33mОтправляем на view при редактировании. " + person + "\n\u001B[0m");
        return "/user/edit";
    }

    @PutMapping("/")
    public String upDate(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult) {
        //Валидация при редактировании пока отключена
        //personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            log.info("\u001B[31mошибка при редактировании пользователя. " + person + ". " + bindingResult + "\u001B[0m");
            return "/user/edit";
        }
        personServiceImpl.saveOrUpdate(person);
        return "/user/personById";
    }

    @GetMapping("/logout")
    public String logOut() {
        return "redirect:/auth/login";
    }

}