package hu.jsweb.SpigotPatreonBridge;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.eclipse.jetty.server.Server;

public class App extends JavaPlugin{
    protected Server webServer;
    
    public void onEnable() {
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
            this.webServer.stop();
        } catch (Exception e) {
        }
    }
    
    public void onWebRequest(String command) {
        
        final String fcommand = command;
        
        Bukkit.getScheduler().runTask(this, new Runnable() {
            @Override
            public void run() {
                getLogger().info(fcommand);
                getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), fcommand);
                getLogger().info(Bukkit.getServer().getConsoleSender());
            }
        });
    }
}