package com.sjwi.catalog.config;

import static com.sjwi.catalog.model.security.StoredCookieToken.STORED_COOKIE_TOKEN_KEY;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sjwi.catalog.service.TokenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;

@Configuration
@EnableWebSecurity
@EnableAsync
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
    @Qualifier("userDetailsService")
    private UserDetailsService userDetailsService;
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth)
	  throws Exception {
	    auth.userDetailsService(userDetailsService);
	}
	
	@Override
    protected void configure(HttpSecurity httpSecurity)
      throws Exception {
        httpSecurity.authorizeRequests()
        	.antMatchers("/images/favicon.png").permitAll()
        	.antMatchers("/login").permitAll()
        	.antMatchers("/password-reset").permitAll()
        	.antMatchers("/request-account").permitAll()
        	.antMatchers("/reset-password").permitAll()
        	.antMatchers("/file/send").permitAll()
        	.antMatchers("/log-user-action").permitAll()
        	.antMatchers("/enroll").permitAll()
        	.antMatchers("/favicon.ico").permitAll()
        	.antMatchers(HttpMethod.GET, "/addressbook*").access("hasAuthority('USER')")
        	.antMatchers(HttpMethod.GET, "/user*").access("hasAuthority('USER')")
        	.antMatchers(HttpMethod.GET, "/setlist/email*").access("hasAuthority('USER')")
        	.antMatchers(HttpMethod.GET, "/**").permitAll()
        	.antMatchers(HttpMethod.POST, "/**").access("hasAuthority('USER')")
        	.antMatchers(HttpMethod.DELETE, "/**").access("hasAuthority('USER')")
        	.and().exceptionHandling()
            .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
            .and().requestCache().requestCache(requestCache())
        	.and().logout()
        	.deleteCookies(STORED_COOKIE_TOKEN_KEY)
        	.logoutSuccessHandler(new CustomLogoutSuccessHandler())
        	.and().headers()
			.frameOptions().sameOrigin()
			.httpStrictTransportSecurity().disable()
			.and().csrf().disable(); //Required for AJAX requests to be authorized
    }
	
	@Bean
	public SpringSecurityDialect securityDialect() {
	    return new SpringSecurityDialect();
	}
	@Bean
	public RequestCache requestCache() {
	   return new HttpSessionRequestCache();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}

	@Bean
	public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
			StrictHttpFirewall firewall = new StrictHttpFirewall();
			firewall.setAllowUrlEncodedPercent(true);
			return firewall;
	}

	@Autowired
	TokenService tokenService;
	
	private class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
		@Override
		public void onLogoutSuccess(HttpServletRequest request,
				HttpServletResponse response, Authentication authentication)
				throws IOException, ServletException {
			if (Arrays.stream(request.getCookies()).anyMatch(c -> STORED_COOKIE_TOKEN_KEY.equals(c.getName()))) {
				tokenService.deleteCookieToken(Arrays.stream(request.getCookies()).filter(c -> STORED_COOKIE_TOKEN_KEY.equals(c.getName())).findFirst().orElse(null));
			}
			response.setStatus(HttpStatus.OK.value());
			response.sendRedirect(request.getContextPath() + "/");
		}	
	}
}
