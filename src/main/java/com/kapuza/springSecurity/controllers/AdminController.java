package com.kapuza.springSecurity.controllers;

import com.kapuza.springSecurity.models.Person;
import com.kapuza.springSecurity.models.Role;
import com.kapuza.springSecurity.security.PersonDetails;
import com.kapuza.springSecurity.services.personService.PersonServiceImpl;
import com.kapuza.springSecurity.services.roleService.RoleServiceImpl;
import com.kapuza.springSecurity.util.PersonValidator;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    private final PersonServiceImpl personServiceImpl;

    private final PersonValidator personValidator;

    private final RoleServiceImpl roleServiceImpl;

    @Autowired
    public AdminController(PersonServiceImpl personServiceImpl,
                           PersonValidator personValidator,
                           RoleServiceImpl roleServiceImpl) {
        this.personServiceImpl = personServiceImpl;
        this.personValidator = personValidator;
        this.roleServiceImpl = roleServiceImpl;
    }

    /**
     * ALL users
     */
    @GetMapping("/persons")
    public String adminPage(Model model) {
        List<Person> personList = new ArrayList<>(personServiceImpl.findAll());
        model.addAttribute("persons", personList);
        log.info("\u001B[33mПОКАЗ ВСЕХ ПОЛЬЗОВАТЕЛЕЙ\u001B[0m");
        return "/admin/persons";
    }

    /**
     * select one user
     */
    @GetMapping("/persons/{id}")
    public String userById(@PathVariable("id") int id, Model model) {
        Person person = personServiceImpl.findById(id);
        log.info("\u001B[33mПОКАЗ ВЫБРАННОГО ПОЛЬЗОВАТЕЛЯ " + person + "\u001B[0m");
        model.addAttribute("getPersonById", person);
        return "/admin/personById";
    }

    /**
     * Amount users
     */
    @GetMapping("/persons/amountUsers")
    public String amountUsers(ModelMap model) {
        model.addAttribute("messages", personServiceImpl.findAll().size());
        return "/admin/amountUsers";
    }

    /**
     * Create
     */
    @GetMapping("/persons/createPerson")
    public String addUser(@ModelAttribute("person") Person person) {
        log.info("\u001B[33mКТО СОЗДАЁТ:   " + PersonDetails.getPerson().getUsername() + "; " + PersonDetails.getPerson().getRoles() + "\u001B[0m");
        return "/admin/newPerson";
    }

    @PostMapping("/persons")
    public String saveNewUser(@ModelAttribute("person") @Valid Person person,
                              BindingResult bindingResult,
                              @RequestParam("listRoles[]") String[] listRoles) {

        log.info("\u001B[31mЧто пришло с формы при создании: " + person + "\n, КТО СОЗДАЁТ: " + PersonDetails.getPerson().getUsername() + "; " + PersonDetails.getPerson().getRoles());
        log.info("\u001B[31m\nСписок ролей при создании: " + Arrays.toString(listRoles) + "\u001B[0m");
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            log.info("\u001B[31mОШИБКИ ПРИ СОЗДАНИИ, КТО СОЗДАЁТ: " + PersonDetails.getPerson().getUsername() + "; " + PersonDetails.getPerson().getRoles() + "\u001B[0m");
            return "/admin/newPerson";
        }
        personServiceImpl.save(person, listRoles);
        return "redirect:/admin/persons";
    }

    /**
     * Update(edit)
     */
    @GetMapping("/persons/edit/{id}")
    public String edit(@PathVariable("id") int id, Model model) {
        Person person = personServiceImpl.findById(id);
        List<Role> listRoles = roleServiceImpl.findAll();
        model.addAttribute("person", person);
        model.addAttribute("listRoles", listRoles);
        log.info("\u001B[33mОтправляем на view при редактировании. " + person.getUsername() + ", id: " + person.getId() + ". \n" + "\u001B[0m");
        return "/admin/edit";
    }

    @PutMapping("/persons")
    public String upDate(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult) {
        //Валидация при редактировании пока отключена
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            log.info("\u001B[31mошибка при редактировании пользователя. " + person.getUsername() + ", id: " + person.getId() + ". " + bindingResult + "\u001B[0m");
            return "/admin/edit";
        }
        personServiceImpl.saveOrUpdate(person);
        log.info("\u001B[33mДанные пользователя успешно обновлены. Новые данные: " + person+ ". \n" + "\u001B[0m");
        return "redirect:/admin/persons";
    }

    /**
     * DELETE
     */
    @DeleteMapping("/persons/{id}")
    public String delete(@PathVariable("id") int id) {
        log.info("\u001B[33mУДАЛЕНИЕ: " + personServiceImpl.findById(id) + "\u001B[0m");
        personServiceImpl.delete(personServiceImpl.findById(id));
        return "redirect:/admin/persons/logout";
    }

    /**
     * logOut
     */
    @GetMapping("/persons/logout")
    public String logOut() {
        /**Если какой-то админ удалил всех и решил ещё себя удалить,
         * то тогда выкидываем из системы т.к. не понятно кто должен делать CRUD операции теперь.
         * По итогу у нас в системе нет админов; можно создавать через форму регистрации только юзеров.
         * Можно админа добавить напрямую в БД при работе с БД.
         * Позже починю)))))*/
        if (personServiceImpl.findAll().size()==0){
            return "redirect:/auth/login";
        }
        return "redirect:/admin/persons";
    }
}