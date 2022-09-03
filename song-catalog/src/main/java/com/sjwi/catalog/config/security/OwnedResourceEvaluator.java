/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.config.security;

import com.sjwi.catalog.exception.UnauthorizedException;
import com.sjwi.catalog.model.api.OwnedResource;
import com.sjwi.catalog.model.user.UserRoles;
import java.security.Principal;
import java.util.function.BiConsumer;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class OwnedResourceEvaluator implements BiConsumer<OwnedResource, Principal> {
  @Override
  public void accept(OwnedResource t, Principal p) {
    UsernamePasswordAuthenticationToken u = null;
    try {
      u = (UsernamePasswordAuthenticationToken) p;
    } catch (Exception e) {
      throw new UnauthorizedException("Invalid token");
    }
    Boolean userIsAdmin =
        u.getAuthorities().stream()
            .map(a -> a.getAuthority())
            .anyMatch(a -> a.equals(UserRoles.ADMIN.name()));
    if (!userIsAdmin && !u.getName().equals(t.getOwner()))
      throw new UnauthorizedException(
          "User does not own this resource and does not have permission to change it.");
  }
}
