/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.java.Log;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
    SecretKey key = Keys.hmacShaKeyFor(System.getenv().get("SONG_CATALOG_SIGNING_KEY").getBytes());
    String token =
        Jwts.builder()
            .setSubject(((User) authentication.getPrincipal()).getUsername())
            .setExpiration(new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000 * 12))
            .signWith(key)
            .compact();
    response.addHeader("Authorization", "Bearer " + token);
  }
}
