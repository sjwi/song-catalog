package com.sjwi.cfsongs;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CfSongsWebApplication extends SpringBootServletInitializer
{
    public static void main(String[] args) throws Exception {
    	
        SpringApplication.run(CfSongsWebApplication.class, args);
    }
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(CfSongsWebApplication.class);
    }
    
}