/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.sjwi.catalog.model.user.CfUser;
import com.sjwi.catalog.service.UserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;

public class AuthorizationFilter extends BasicAuthenticationFilter {
  UserService userService;

  public AuthorizationFilter(AuthenticationManager authenticationManager, ApplicationContext ctx) {
    super(authenticationManager);
    this.userService = ctx.getBean(UserService.class);
  }

  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws IOException, ServletException {
    String header = request.getHeader("Authorization");
    if (header == null || !header.startsWith("Bearer")) {
      filterChain.doFilter(request, response);
      return;
    }
    CfUser cfUser = getAuthentication(request);
    if (cfUser != null)
      SecurityContextHolder.getContext()
          .setAuthentication(
              new UsernamePasswordAuthenticationToken(cfUser, null, cfUser.getAuthorities()));
    filterChain.doFilter(request, response);
  }

  private CfUser getAuthentication(HttpServletRequest request) {
    String token = request.getHeader("Authorization");
    if (token != null) {
      try {
        String user =
            Jwts.parserBuilder()
                .setSigningKey(System.getenv().get("SONG_SIGNING_KEY").getBytes())
                .build()
                .parseClaimsJws(token.replace("Bearer ", ""))
                .getBody()
                .getSubject();
        if (user != null)
          return userService.loadCfUserByUsername(user);
      } catch (SignatureException e) {
        logger.warn("Unauthorized attempt with token: " + token);
      }
    }
    return null;
  }
}
