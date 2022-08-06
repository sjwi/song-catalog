/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.config.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sjwi.catalog.model.user.CfUser;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.java.Log;

@Log
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
  private AuthenticationManager authenticationManager;

  public AuthenticationFilter(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
    setFilterProcessesUrl("/login");
  }

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    try {
      Map<String, String> data = new ObjectMapper().readValue(request.getInputStream(), Map.class);
      String username = data.get("username");
      String password = data.get("password");
      Authentication auth =
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>()));
      return auth;
    } catch (IOException e) {
      throw new RuntimeException("Could not read request" + e);
    }
  }

  protected void successfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain,
      Authentication authentication) {
    SecretKey key = Keys.hmacShaKeyFor(System.getenv().get("SONG_SIGNING_KEY").getBytes());
    CfUser user = (CfUser) authentication.getPrincipal();
    String token =
        Jwts.builder()
            .setSubject(user.getUsername())
            .setExpiration(new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000 * 12))
            .claim("scope", user.getAuthorities().stream()
                        .map(a -> a.getAuthority())
                        .collect(Collectors.joining(" ")))
            .claim("firstName", user.getFirstName())
            .claim("lastName", user.getLastName())
            .claim("email", user.getEmail())
            .signWith(key)
            .compact();
    response.addHeader("Authorization", "Bearer " + token);
  }
}
