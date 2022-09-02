package ba.unsa.etf.rpr.bugtracker.models;

import ba.unsa.etf.rpr.bugtracker.common.enums.Department;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;

public class User implements Serializable{
    SimpleIntegerProperty id = new SimpleIntegerProperty();
    SimpleStringProperty username = new SimpleStringProperty();
    SimpleStringProperty lastname = new SimpleStringProperty();
    SimpleStringProperty firstname = new SimpleStringProperty();
    SimpleStringProperty email = new SimpleStringProperty();
    SimpleStringProperty password = new SimpleStringProperty();
    SimpleObjectProperty<Department> department = new SimpleObjectProperty<>();

    public User() {}

    public User(SimpleIntegerProperty id, SimpleStringProperty username, SimpleStringProperty lastname, SimpleStringProperty firstname, SimpleStringProperty email, SimpleStringProperty password, SimpleObjectProperty<Department> department) {
        this.id = id;
        this.username = username;
        this.lastname = lastname;
        this.firstname = firstname;
        this.email = email;
        this.password = password;
        this.department = department;
    }

    public User(int id, String username, String lastname, String firstname, String email, String password, Department department) {
        setId(id);
        setUsername(username);
        setLastname(lastname);
        setFirstname(firstname);
        setEmail(email);
        setPassword(password);
        setDepartment(department);
    }

    @Override
    public String toString() {
        return lastname.get() + " " + firstname.get() + " : " + username.get();
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getUsername() {
        return username.get();
    }

    public SimpleStringProperty usernameProperty() {
        return username;
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getLastname() {
        return lastname.get();
    }

    public SimpleStringProperty lastnameProperty() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname.set(lastname);
    }

    public String getFirstname() {
        return firstname.get();
    }

    public SimpleStringProperty firstnameProperty() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname.set(firstname);
    }

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getPassword() {
        return password.get();
    }

    public SimpleStringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public Department getDepartment() {
        return department.get();
    }

    public SimpleObjectProperty<Department> departmentProperty() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department.set(department);
    }
}
