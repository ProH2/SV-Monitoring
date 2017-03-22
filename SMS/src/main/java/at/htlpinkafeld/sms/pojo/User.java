/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.htlpinkafeld.sms.pojo;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Martin Six
 */
public class User implements Serializable, IPojo {

    private Integer userNr;
    private String username;
    private String password;
    private String name;
    private String email;
    private String phoneNr;
    private boolean disabled;

    public User() {
        disabled=false;
    }

    @Deprecated
    public User(Integer userNr, String username, String password, String name, String email, String phoneNr, boolean disabled) {
        this.userNr = userNr;
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phoneNr = phoneNr;
        this.disabled = disabled;
    }

    public User(String username, String password, String name, String email, String phoneNr) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phoneNr = phoneNr;
        disabled=false;
    }

    @Override
    public Integer getId() {
        return userNr;
    }

    @Override
    public void setId(Integer userNr) {
        this.userNr = userNr;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNr() {
        return phoneNr;
    }

    public void setPhoneNr(String phoneNr) {
        this.phoneNr = phoneNr;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.userNr);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        if (!Objects.equals(this.userNr, other.userNr)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "User{" + "userNr=" + userNr + ", username=" + username + ", password=" + password + ", name=" + name + ", email=" + email + ", disabled=" + disabled + '}';
    }

 



}
