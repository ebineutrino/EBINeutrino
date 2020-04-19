package Run.webServer

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

final String resourcePath = System.getProperty("user.dir") + File.separator + "resources" + File.separator;

Tomcat tomcat = new Tomcat();
tomcat.setPort(8080);
tomcat.getConnector().setProperty("URIEncoding", "UTF-8");
tomcat.getConnector().setProperty("connectionTimeout", "60000");
tomcat.getConnector().setProperty("maxKeepAliveRequests", "-1");
tomcat.getConnector().setAttribute("maxThreads", "1000");

tomcat.setBaseDir(resourcePath);
StandardContext ctx = (StandardContext) tomcat.addWebapp("/", resourcePath+"web");
WebResourceRoot resources = new StandardRoot(ctx);
resources.addPreResources(new DirResourceSet(resources, "/reports", resourcePath+"reports", "/"));
resources.addPreResources(new DirResourceSet(resources, "/images", resourcePath+"images", "/"));

ctx.setResources(resources);

Tomcat.addServlet(ctx, "ebi", new HttpServlet() {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain");
        try (Writer writer = response.getWriter()) {
            writer.write("Hello, Embedded World from Blue Lotus Software!");
            writer.flush();
        }
    }
});
ctx.addServletMappingDecoded("/ebi", "ebi");
tomcat.start();
//tomcat.getServer().await();
//println "hallo world: "+resourcePath;