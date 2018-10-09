package hu.jsweb.SpigotPatreonBridge;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.eclipse.jetty.server.Server;

public class App extends JavaPlugin{
	public void onEnable() {
		Server server = new Server(8080);
		server.setHandler(new WebHandler());
        try {
            server.start();
        } catch (Exception e) {      	
            getLogger().info("Cannnot bind to port 8080!");
            getServer().dispatchCommand(getServer().getConsoleSender(), "/help");
            e.printStackTrace();
        }
		this.getLogger().info("SpigotPatreonBridge Enabled");
	}
}