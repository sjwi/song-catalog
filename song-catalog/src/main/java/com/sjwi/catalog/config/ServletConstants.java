package com.sjwi.catalog.config;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.Query;
import javax.management.QueryExp;
import javax.management.ReflectionException;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServletConstants {

    public static String SCHEME;
    public static String SERVER_NAME;
    public static String SERVER_PORT;
    public static String CONTEXT_PATH;
    public static String BASE_URL;
    public static String FULL_URL;
    
    @Autowired
    public ServletContext context;

    @PostConstruct
    public void initializeServletConstants() throws MalformedObjectNameException,
            NullPointerException, UnknownHostException, AttributeNotFoundException,
            InstanceNotFoundException, MBeanException, ReflectionException {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        QueryExp subQuery1 = Query.match(Query.attr("protocol"), Query.value("HTTP/1.1"));
        QueryExp subQuery2 = Query.anySubString(Query.attr("protocol"), Query.value("Http11"));
        QueryExp query = Query.or(subQuery1, subQuery2);
        Set<ObjectName> objs = mbs.queryNames(new ObjectName("*:type=Connector,*"), query);
        String hostname = InetAddress.getLocalHost().getHostName();
        ObjectName obj = objs.iterator().next();
        String scheme = mbs.getAttribute(obj, "scheme").toString();
        String port = obj.getKeyProperty("port");
        String host = InetAddress.getAllByName(hostname)[0].getHostAddress();
        SCHEME = scheme;
        SERVER_NAME = host;
        SERVER_PORT = port;
        CONTEXT_PATH = context.getContextPath();
        BASE_URL = scheme + "://" + host + ":" + port;
        FULL_URL = CONTEXT_PATH.isBlank()? BASE_URL: BASE_URL + "/" + CONTEXT_PATH;
    }
}