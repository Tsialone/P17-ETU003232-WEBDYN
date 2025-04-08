package controller;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Depense;
import model.Etat;
import model.Prevision;

import java.util.ArrayList;
import java.util.List;

public class DepenseFormServlet extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        PrintWriter write = response.getWriter();
        response.setContentType("text/html");
        try {

            Integer idPrevision = Integer.parseInt(request.getParameter("idPrevision"));
            double montant = Double.parseDouble(request.getParameter("montant"));
            Prevision temp = new Prevision();
            temp = (Prevision)temp.getById(idPrevision);
            Depense dep = new Depense();
            int last_id = 0;
            List<Object> all_dep = dep.findAll();
            for (Object ob_dep : all_dep) {
                Depense temp_prev = (Depense) ob_dep;
                if (last_id < temp_prev.getId()) {
                    last_id = temp_prev.getId();
                }
            }
            last_id++;
            dep = new Depense(last_id, idPrevision, montant);
            dep.canDepense(idPrevision);
            dep.save();

            Etat etat = new Etat();
            List<Object> all_etat = etat.findAll();
            for (Object ob_etat : all_etat) {
                Etat temp_etat = (Etat) ob_etat;
                if (temp_etat.getIdPrevision() == idPrevision) {
                    double newdepense = temp_etat.getDepense() + dep.getMontant();
                    temp_etat.setDepense(newdepense);
                    temp_etat.setReste(temp.getMontant() - newdepense);
                    temp_etat.update();
                    break;
                }
            }

            
            response.sendRedirect("depense_form");

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
            Prevision temp_prevision = new Prevision();
            List<Object> all_prevision_ob = temp_prevision.findAll();
            List<Prevision> all_prevision = new ArrayList<>();
            for (Object departement_obj : all_prevision_ob) {
                all_prevision.add((Prevision) departement_obj);
            }
            request.setAttribute("all_prevision", all_prevision);
            RequestDispatcher dispat = request.getRequestDispatcher("depense.jsp");
            dispat.forward(request, response);
        } catch (Exception x) {
            write.println(x.getMessage());
        }
    }
}
