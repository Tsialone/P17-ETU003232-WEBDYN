package controller;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Etat;

import java.util.ArrayList;
import java.util.List;

public class EtatServlet  extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // PrintWriter write = response.getWriter();
       

        // response.setContentType("text/html");
        // try {
        //     String libelle = request.getParameter("libelle");
        //     double montant =  Double.parseDouble( request.getParameter("montant"));

        //     Prevision prev = new Prevision();
        //     int last_id = 0;
        //     List<Object>all_prev = prev.findAll();
        //     for (Object ob_prev : all_prev) {
        //         Prevision temp_prev = (Prevision)ob_prev;
        //         if (last_id < temp_prev.getId()){
        //                 last_id = temp_prev.getId();
        //         }
        //     }
        //     last_id++;
        //     prev = new Prevision(last_id ,  libelle , montant );
        //     prev.save();
        //     response.sendRedirect("index.html");
        // } catch (Exception x) {
        //     write.println(x.getMessage());
        // }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        PrintWriter write = response.getWriter();
        try {
            Etat temp_etat = new Etat();
            List<Object> all_etat_ob = temp_etat.findAll();
            List<Etat> all_etat = new ArrayList<>();
            for (Object etat_obj : all_etat_ob) {
                all_etat.add((Etat) etat_obj);
            }
            request.setAttribute("all_etat", all_etat);
            RequestDispatcher dispat = request.getRequestDispatcher("etat.jsp");
            dispat.forward(request, response);
        } catch (Exception x) {
            write.println(x.getMessage());
        }
    }


}
