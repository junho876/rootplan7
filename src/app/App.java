package app;

import java.io.File;

import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

public class App {
    public static void main(String[] args) throws Exception {
        String webappDirLocation = "WebContent/";
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8090);
        Connector connector = tomcat.getConnector();
        connector.setURIEncoding("UTF-8");
        StandardContext ctx = (StandardContext) tomcat.addWebapp("/", new File(webappDirLocation).getAbsolutePath());
        
        
        // Declare an alternative location for your "WEB-INF/classes" dir
        // Servlet 3.0 어노테이션 작동
        // 참고 : http://stackoverflow.com/questions/11669507/embedded-tomcat-7-servlet-3-0-annotations-not-working
        File additionWebInfClasses = new File("target/classes");
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes", additionWebInfClasses.getAbsolutePath(), "/"));
        ctx.setResources(resources);
 
        tomcat.start();
        tomcat.getServer().await();
    }
    
    
}


