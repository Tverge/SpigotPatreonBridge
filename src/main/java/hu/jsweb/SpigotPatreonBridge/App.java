package hu.jsweb.SpigotPatreonBridge;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.eclipse.jetty.server.Server;

import org.eclipse.jetty.server.Request;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class App extends JavaPlugin {
    protected Server webServer;
    protected WebCommandSender commandSender;
    
    public void onEnable() {
        this.commandSender = new WebCommandSender();

        this.webServer = new Server(8080);
        this.webServer.setHandler(new WebHandler(this));

        try {
            this.webServer.start();
        } catch (Exception e) {
            getLogger().info("Cannnot bind to port 8080!");
            e.printStackTrace();
        }
        this.getLogger().info("SpigotPatreonBridge Enabled");
    }
    
    public void onDisable() {
        try {
            System.out.println("onDisable");
            this.webServer.stop();
        } catch (Exception e) {
        }
    }

    public WebCommandSender getCommandSender() {
        return this.commandSender;
    }
    
    public void onWebRequest(String target, Request baseRequest,  HttpServletRequest request, HttpServletResponse response)
        throws IOException
    {
        getLogger().info(baseRequest.getMethod());
        String method = baseRequest.getMethod();
        final String fcommand = request.getParameter("command");
        
        if (method != "POST") {
            response.setContentType("text/html; charset=utf-8");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            baseRequest.setHandled(true);
            return;
        }
        
        if (fcommand == null) {
            response.setContentType("text/html; charset=utf-8");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            baseRequest.setHandled(true);
            return;
        }

        final WebCommandSender commandSender = this.getCommandSender();
        commandSender.setRequestResponse(baseRequest, response);
        
        Bukkit.getScheduler().runTask(this, new Runnable() {
            @Override
            public void run() {
                
                System.out.println("help");
                
                getLogger().info(fcommand);
                getServer().dispatchCommand(commandSender, fcommand);
                try {
                    commandSender.send();
                }
                catch (IOException e) {
                    
                }
            }
        });
    }
}