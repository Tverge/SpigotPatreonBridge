package hu.jsweb.SpigotPatreonBridge;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bukkit.Bukkit;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import java.util.logging.Logger;

public class WebHandler extends AbstractHandler
{
    App plugin;

    public WebHandler(App plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public void handle (String target, Request baseRequest,  HttpServletRequest request, HttpServletResponse response) 
        throws IOException, ServletException
    {
        this.plugin.onWebRequest(target, baseRequest,  request, response);
    }
}
