package controller;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Etat;
import model.Prevision;

import java.util.List;

public class PrevisionFormServlet extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        PrintWriter write = response.getWriter();
       

        response.setContentType("text/html");
        try {
            String libelle = request.getParameter("libelle");
            double montant =  Double.parseDouble( request.getParameter("montant"));

            Prevision prev = new Prevision();
            int last_id = 0;
            List<Object>all_prev = prev.findAll();
            for (Object ob_prev : all_prev) {
                Prevision temp_prev = (Prevision)ob_prev;
                if (last_id < temp_prev.getId()){
                        last_id = temp_prev.getId();
                }
            }
            last_id++;
            prev = new Prevision(last_id ,  libelle , montant );
            prev.save();

            Etat etat = new Etat();
            int etat_last_id = 0;
            List<Object>all_etat = etat.findAll();
            for (Object ob_etat : all_etat) {
                Etat temp_etat = (Etat)ob_etat;
                if (etat_last_id < temp_etat.getId()){
                    etat_last_id = temp_etat.getId();
                }
            }
            etat_last_id++;
            etat = new Etat(etat_last_id, prev.getId(), 0, prev.getMontant());
            etat.save();
            response.sendRedirect("prevision.html");
        } catch (Exception x) {
            write.println(x.getMessage());
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        PrintWriter write = response.getWriter();
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("validation") == null) {
            response.sendRedirect("index.html");
        }
        try {
            response.sendRedirect("prevision.html");
        } catch (Exception x) {
            write.println(x.getMessage());
        }
    }
}
