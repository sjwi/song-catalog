/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.aspect;

import com.sjwi.catalog.config.ServletConstants;
import java.io.IOException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class LandingPageSessionInitializer {

  @Before("@annotation(com.sjwi.catalog.aspect.ServletInitializerAspect)")
  public void servletInitializer(JoinPoint joinPoint) throws IOException {
    if (!ServletConstants.IS_INITIALIZED) ServletConstants.initializeServletConstants();
  }
}
