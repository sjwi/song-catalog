package com.sjwi.catalog.config.security;

import java.security.Principal;
import java.util.function.BiConsumer;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import com.sjwi.catalog.exception.UnauthorizedException;
import com.sjwi.catalog.model.api.OwnedResource;
import com.sjwi.catalog.model.user.UserRoles;

@Component
public class OwnedResourceEvaluator implements BiConsumer<OwnedResource, Principal> {
  @Override
  public void accept(OwnedResource t, Principal p) {
    try {
      UsernamePasswordAuthenticationToken u = (UsernamePasswordAuthenticationToken) p;
      Boolean userIsAdmin = u.getAuthorities().stream().map(a -> a.getAuthority()).anyMatch(a -> a.equals(UserRoles.ADMIN.name()));
      if (!userIsAdmin && !u.getName().equals(t.getOwner()))
        throw new UnauthorizedException("User does not own this resource and does not have permission to change it.");
    } catch (Exception e) {
        throw new UnauthorizedException("Invalid token");
    }
  }
}
