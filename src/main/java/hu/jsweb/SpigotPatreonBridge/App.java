package hu.jsweb.SpigotPatreonBridge;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.AsyncContext;

import java.io.IOException;
import java.io.PrintWriter;

public class App extends JavaPlugin {
    protected Server webServer;
    protected WebCommandSender commandSender;
    protected static App singletonInstance;
    
    public static App Instance() {
        return singletonInstance;
    }
    
    public static class EmbeddedAsyncServlet extends HttpServlet
    {
        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
            final String fcommand = request.getParameter("command");
            final AsyncContext ctxt = request.startAsync();

            App.Instance().getLogger().info("run command: "+ fcommand);
            
            if (fcommand == null) {
                response.setContentType("text/html; charset=utf-8");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            final WebCommandSender commandSender = App.Instance().getCommandSender();
            commandSender.setServletResponse(response);

            ctxt.start(new Runnable()
            {
                @Override
                public void run()
                {
                    System.out.println("async ctx did run");

                    try {
                        Bukkit.getScheduler().runTask(App.Instance(), new Runnable() {
                            @Override
                            public void run() {

                                System.out.println("dispatch command will run");

                                App.Instance().getServer().dispatchCommand(commandSender, fcommand);

                                System.out.println("dispatch command did run");

                                try {
                                    commandSender.prepareResponse();
                                    ctxt.complete();
                                }
                                catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
    
    private void initServer() throws Exception {
        if (this.webServer != null) {
            return;
        }
        
        App.singletonInstance = this;
        
        this.webServer = new Server(8080);
        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/");
        ServletHolder asyncHolder = context.addServlet(EmbeddedAsyncServlet.class, "/");
        asyncHolder.setAsyncSupported(true);
        this.webServer.setHandler(context);
        this.webServer.start();
        this.webServer.join();
    }

    private void startServer() throws Exception {
        System.out.println("web server starting");

        this.webServer.start();
        this.webServer.join();
    }
    
    private void stopServer() throws Exception {
        System.out.println("web server stopping");
        this.webServer.stop();
    }
    
    public void onEnable() {
        this.commandSender = new WebCommandSender();

        try {
            this.initServer();
            this.startServer();
        } catch (Exception e) {
            getLogger().info("Cannnot bind to port 8080!");
            e.printStackTrace();
        }
        this.getLogger().info("SpigotPatreonBridge Enabled");
    }
    
    public void onDisable() {
        try {
            this.stopServer();
        } catch (Exception e) {
        }
    }

    public WebCommandSender getCommandSender() {
        return this.commandSender;
    }
}