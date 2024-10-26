package com.kapuza.springSecurity.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "person3_1_2")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Имя не должно быть пустым")
    @Size(min = 2, max = 100, message = "Имя должно быть быть от 2 до 100 символов")
    @Column(name = "username")
    private String username;

    @Min(value = 0, message = "Год рождения должен быть больше, чем 0")
    @Column(name = "year_of_birth")
    private int yearOfBirth;

    @Column(name = "password")
    private String password;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinTable(name = "person3_1_2_role3_1_2",
            joinColumns = @JoinColumn(name = "person3_1_2_id"),
            inverseJoinColumns = @JoinColumn(name = "role3_1_2_id"))
    private List<Role> roles = new ArrayList<>();


    public Person() {
    }

    public Person(String username, int yearOfBirth) {
        this.username = username;
        this.yearOfBirth = yearOfBirth;
    }


    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public void setRoles() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(int yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void addRole(Role role) {
        roles.add(role);
    }

    public void addRole(List<Role> roles1) {
        this.roles.addAll(roles1);
    }

    public List<Role> getRoles() {
        return this.roles;
    }

    public Role[] getRolesString() {

        Role[] roles1 = new Role[roles.size()];
        for (int i = 0; i < roles1.length; i++) {
            roles1[i] = roles.get(i);
        }

        return roles1;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", yearOfBirth=" + yearOfBirth +
                ", password='" + password + '\'' + "\n" +
                ", roles=" + roles +
                "}\n";
    }
}