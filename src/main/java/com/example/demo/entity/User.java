package com.example.demo.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Entity(name = "users")
@Getter
@ToString
public class User implements UserDetails {
    @Id
    @GeneratedValue
    Long userEntryNo;

    @Column
    String email;
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    Date registeredDate;

    @Builder
    public User(Long userEntryNo, String email, Date registeredDate) {
        this.userEntryNo = userEntryNo;
        this.email = email;
        this.registeredDate = registeredDate;
    }

    public User() {

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
