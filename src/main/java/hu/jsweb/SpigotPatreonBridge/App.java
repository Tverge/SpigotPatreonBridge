package hu.jsweb.SpigotPatreonBridge;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.eclipse.jetty.server.Server;

public class App extends JavaPlugin{
    protected Server webServer;
    
	public void onEnable() {
		this.webServer = new Server(8080);
		this.webServer.setHandler(new WebHandler());
        try {
            this.webServer.start();
        } catch (Exception e) {
            getLogger().info("Cannnot bind to port 8080!");
            getServer().dispatchCommand(getServer().getConsoleSender(), "/help");
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
}