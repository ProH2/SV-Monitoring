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
public class User implements Serializable {

    private Integer userNr;
    private String username;
    private String password;
    private String name;
    private String email;
    private String phoneNr;

    public User() {
    }

    @Deprecated
    public User(Integer userNr, String username, String password, String name, String email, String phoneNr) {
        this.userNr = userNr;
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phoneNr = phoneNr;
    }

    public User(String username, String password, String name, String email, String phoneNr) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phoneNr = phoneNr;
    }

    public Integer getUserNr() {
        return userNr;
    }

    public void setUserNr(Integer userNr) {
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
        return username;
    }

}