package hu.jsweb.SpigotPatreonBridge;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

import org.eclipse.jetty.server.Request;
import org.bukkit.command.CommandSender;
import javax.servlet.http.HttpServletResponse;

import java.util.*;
import java.io.IOException;
import java.io.PrintWriter;

import com.google.gson.Gson;

public class WebCommandSender implements CommandSender
{
    private HttpServletResponse response;
    private ArrayList<String> messagesToSend;
    
    private final PermissibleBase perm = new PermissibleBase(this);
    
    public void setServletResponse(HttpServletResponse response) {
        this.response = response;
        this.messagesToSend = new ArrayList<String>();
    }
    
    public void sendMessage(String message) {
        this.messagesToSend.add(message);
    }
    
    public void sendMessage(String[] messages) {
        for (String message : messages) {
            sendMessage(message);
        }
    }
    
    public String getName() {
        return "WCONSOLE";
    }
    
    public Server getServer() {
        return Bukkit.getServer();
    }
    
    public void prepareResponse() 
        throws IOException
    {
        Gson gson = new Gson();
        
        this.response.setContentType("application/json");
        this.response.setStatus(HttpServletResponse.SC_OK);

        PrintWriter responseWriter = this.response.getWriter();
        responseWriter.println(gson.toJson(this.messagesToSend));
        
        System.out.println(gson.toJson(this.messagesToSend));
    }

    public CommandSender.Spigot spigot() {
        return new CommandSender.Spigot();
    }
    // Spigot end

    public boolean isOp() {
        return true;
    }

    public void setOp(boolean value) {
        throw new UnsupportedOperationException("Cannot change operator status of server console");
    }

    public boolean isPermissionSet(String name) {
        return perm.isPermissionSet(name);
    }

    public boolean isPermissionSet(Permission perm) {
        return this.perm.isPermissionSet(perm);
    }

    public boolean hasPermission(String name) {
        return perm.hasPermission(name);
    }

    public boolean hasPermission(Permission perm) {
        return this.perm.hasPermission(perm);
    }

    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        return perm.addAttachment(plugin, name, value);
    }

    public PermissionAttachment addAttachment(Plugin plugin) {
        return perm.addAttachment(plugin);
    }

    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        return perm.addAttachment(plugin, name, value, ticks);
    }

    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        return perm.addAttachment(plugin, ticks);
    }

    public void removeAttachment(PermissionAttachment attachment) {
        perm.removeAttachment(attachment);
    }

    public void recalculatePermissions() {
        perm.recalculatePermissions();
    }

    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return perm.getEffectivePermissions();
    }

    public boolean isPlayer() {
        return false;
    }
}
