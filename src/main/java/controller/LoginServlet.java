package controller;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LoginServlet extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        PrintWriter write = response.getWriter();
        HttpSession session = null;
        if (request.getSession(false) == null) {
            session = request.getSession(true);
        } else {
            session = request.getSession();
        }
        // atao eto ilay verification raha misy verification mdp

        response.setContentType("text/html");
        try {
            String nom = request.getParameter("nom");
            String mdp = request.getParameter("mdp");
            if (nom.equals("Rakoto") && mdp.equals("123") ) {
                session.setAttribute("validation", true);
                response.sendRedirect("make_prevision");
            }
            else {
                response.sendRedirect("index.html");

            }

        } catch (Exception x) {
            write.println(x.getMessage());
        }
    }

}
