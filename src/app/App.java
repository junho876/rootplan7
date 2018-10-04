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
        
        

        File additionWebInfClasses = new File("target/classes");
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes", additionWebInfClasses.getAbsolutePath(), "/"));
        ctx.setResources(resources);
 
        tomcat.start();
        tomcat.getServer().await();
    }
    
    
}


