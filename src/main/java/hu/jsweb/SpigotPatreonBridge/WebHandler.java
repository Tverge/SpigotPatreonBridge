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
    final String greeting;
    final String body;

    public WebHandler()
    {
        this("Hello World");
    }

    public WebHandler( String greeting )
    {
        this(greeting, null);
    }

    public WebHandler( String greeting, String body )
    {
        this.greeting = greeting;
        this.body = body;
    }

    @Override
    public void handle( String target, Request baseRequest,  HttpServletRequest request, HttpServletResponse response ) 
    		throws IOException, ServletException
    {
        response.setContentType("text/html; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        String val = request.getParameter("foo");
        PrintWriter out = response.getWriter();

        //HTTP print
        out.println (val);
        //CONSOLE print
        System.console().writer().println(val);
        System.out.println(val);

        baseRequest.setHandled(true);
    }
}
