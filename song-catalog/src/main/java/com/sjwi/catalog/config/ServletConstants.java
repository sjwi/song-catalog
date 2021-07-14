package com.sjwi.catalog.config;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${server.port}")
    private int containerPort;

    private static final String DEPLOYMENT_SERVER = "stephenky.com";

    private static final List<String> IGNORE_PORT_LIST = new ArrayList<String>(Arrays.asList("80","443","8080","8443"));

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
        String scheme = "http";
        String port = String.valueOf(containerPort);
        String host = "localhost";
        if (objs.iterator().hasNext()) {
            ObjectName obj = objs.iterator().next();
            scheme = mbs.getAttribute(obj, "scheme").toString();
            port = obj.getKeyProperty("port");
            host = InetAddress.getAllByName(hostname)[0].getHostAddress();
        } 
        if ("192.168.1.45".equals(host))
            SERVER_NAME = "localhost";
        else if ("127.0.1.1".equals(host))
            SERVER_NAME = DEPLOYMENT_SERVER;
        else
            SERVER_NAME = host;
        SCHEME = host.contains(".com")? "https" : scheme;
        SERVER_PORT = port;
        CONTEXT_PATH = context.getContextPath().toString().replaceAll("/","");
        BASE_URL = scheme + "://" + SERVER_NAME;
        if (!IGNORE_PORT_LIST.contains(port))
            BASE_URL += ":" + port;
        FULL_URL = CONTEXT_PATH.isBlank()? BASE_URL: BASE_URL + "/" + CONTEXT_PATH;
    }
}