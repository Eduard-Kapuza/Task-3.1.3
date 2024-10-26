package com.kapuza.springSecurity.models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "role3_1_2")
public class Role {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "role_name")
    private String roleName;

    @ManyToMany
    @JoinTable(name = "person3_1_2_role3_1_2",
            joinColumns = @JoinColumn(name = "role3_1_2_id"),
            inverseJoinColumns = @JoinColumn(name = "person3_1_2_id"))
    private List<Person> owner = new ArrayList<>();


    public Role() {
    }

    public Role(String roleName) {
        this.roleName = roleName;
        this.owner = new ArrayList<>();
    }


    public Role(String roleName, List<Person> personList) {
        this.roleName = roleName;
        this.owner = personList;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String role) {
        this.roleName = role;
    }

    public List<Person> getOwner() {
        return owner;
    }

    public void setOwner(List<Person> owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(roleName, role.roleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleName);
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", role='" + roleName + '\'' +
                '}';
    }
}