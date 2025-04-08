package model;

import java.util.ArrayList;
import java.util.List;

public class Depense  extends GenericClass{
    private int idPrevision;
    private double montant;
    public Depense (int id , int idPrevision  ,double montant){
        super(id );
        setMontant(montant);
        setIdPrevision(idPrevision);

    }
    public Depense (){
        super();
    }
   

    public int getIdPrevision() {
        return idPrevision;
    }
    public void setIdPrevision(int idPrevision) {
        this.idPrevision = idPrevision;
    }
    public double getMontant() {
        return montant;
    }
    public void setMontant(double montant) {
        this.montant = montant;
    }

    public void canDepense (int idPrevision) throws Exception{
        Prevision temp_prevision = new Prevision();
        temp_prevision = (Prevision)temp_prevision.getById(idPrevision);
        double can_depense = temp_prevision.getMontant();
        double depensed = this.getMontant();

        if (this.getMontant() > can_depense){
            throw new Exception("Votre depense depasse la prevision " + temp_prevision.getLibelle() + " " + temp_prevision.getMontant() + "\n" + "montant depense: " + this.getMontant());

        }

        List<Object> all_depenses_ob =  this.findAll();
        for (Object depense_ob : all_depenses_ob) {
            Depense temp_depense = (Depense)depense_ob;
            if(temp_depense.getIdPrevision() == idPrevision) {
                    depensed+= temp_depense.getMontant();
            }
        }
        if (depensed > can_depense) {
            throw new Exception("Vous avez atteint le montant du prevision sur " + temp_prevision.getLibelle() + " " + temp_prevision.getMontant() + "\n" + "montant depense: " + depensed);
        }
       
    }
 
    
}