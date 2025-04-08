package model;


public class Prevision  extends GenericClass{
    private String libelle;
    private double montant;
    public Prevision (int id  ,String libelle , double montant){
        super(id );
        setLibelle(libelle);
        setMontant(montant);
    }
    public Prevision (){
        super();
    }
    
    public String getLibelle() {
        return libelle;
    }
    public double getMontant() {
        return montant;
    }
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
    public void setMontant(double montant) {
        this.montant = montant;
    }
 
    
}