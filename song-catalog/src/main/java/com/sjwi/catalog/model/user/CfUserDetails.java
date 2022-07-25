/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.model.user;

import java.util.Collection;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

public class CfUserDetails implements UserDetails {

  private static final long serialVersionUID = 1L;
  private CfUser user;

  Collection<GrantedAuthority> authorities = null;

  public CfUserDetails(CfUser user) {
    this.user = user;
    this.authorities = user.getAuthorities();
  }

  public User getUser() {
    return user;
  }

  public void setUser(CfUser user) {
    this.user = user;
  }

  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  public void setAuthorities(Set<GrantedAuthority> authorities) {
    this.authorities = authorities;
  }

  public String getPassword() {
    return user.getPassword();
  }

  public String getUsername() {
    return user.getUsername();
  }

  public String getFirstname() {
    return user.getFirstName();
  }

  public boolean isAccountNonExpired() {
    return user.isAccountNonExpired();
  }

  public boolean isAccountNonLocked() {
    return user.isAccountNonLocked();
  }

  public boolean isCredentialsNonExpired() {
    return user.isCredentialsNonExpired();
  }

  public boolean isEnabled() {
    return user.isEnabled();
  }
}
