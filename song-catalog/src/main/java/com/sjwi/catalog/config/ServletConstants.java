/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class ServletConstants {

  public static String SCHEME;
  public static String SERVER_NAME;
  public static String SERVER_PORT;
  public static String CONTEXT_PATH;
  public static String BASE_URL;
  public static String FULL_URL;
  public static boolean IS_INITIALIZED = false;

  private static final List<String> IGNORE_PORT_LIST =
      new ArrayList<String>(Arrays.asList("80", "443", "8080", "8443", "8002"));

  public static void initializeServletConstants() {
    HttpServletRequest request =
        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    SERVER_NAME = request.getServerName();
    SCHEME =
        SERVER_NAME.contains(".com") || SERVER_NAME.contains(".app")
            ? "https"
            : request.getScheme();
    SERVER_PORT = String.valueOf(request.getServerPort());
    CONTEXT_PATH = request.getContextPath().replaceAll("/", "");
    BASE_URL = SCHEME + "://" + SERVER_NAME;
    if (!IGNORE_PORT_LIST.contains(SERVER_PORT)) BASE_URL += ":" + SERVER_PORT;
    FULL_URL = CONTEXT_PATH.trim().isEmpty() ? BASE_URL : BASE_URL + "/" + CONTEXT_PATH;
    IS_INITIALIZED = !InetAddressValidator.getInstance().isValid(SERVER_NAME);
  }
}
