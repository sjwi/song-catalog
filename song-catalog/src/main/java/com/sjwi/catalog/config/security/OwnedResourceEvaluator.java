package com.sjwi.catalog.config.security;

import java.util.function.BiConsumer;

import org.springframework.stereotype.Component;

import com.sjwi.catalog.exception.UnauthorizedException;
import com.sjwi.catalog.model.api.OwnedResource;
import com.sjwi.catalog.model.user.CfUser;
import com.sjwi.catalog.model.user.UserRoles;

@Component
public class OwnedResourceEvaluator implements BiConsumer<OwnedResource, CfUser> {
  @Override
  public void accept(OwnedResource t, CfUser u) {
    Boolean userIsAdmin = u.getAuthorities().stream().map(a -> a.getAuthority()).anyMatch(a -> a.equals(UserRoles.ADMIN.name()));
    if (!userIsAdmin && !u.getUsername().equals(t.getOwner()))
      throw new UnauthorizedException("User does not own this resource and does not have permission to change it.");
  }
}
