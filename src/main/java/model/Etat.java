package model;

public class Etat extends GenericClass {
    private int idPrevision;
    private double depense;
    private double reste;

    public Etat(int id, int idPrevision, double depense, double reste) {
        super(id);
        setDepense(depense);
        setIdPrevision(idPrevision);
        setReste(reste);
    }

    public Etat() {
        super();
    }

    public void setReste(double reste) {
        this.reste = reste;
    }

        public void setDepense(double depense) {
            this.depense = depense;
        }
        public double getDepense() {
            return depense;
        }

    public void setIdPrevision(int idPrevision) {
        this.idPrevision = idPrevision;
    }

  
    public int getIdPrevision() {
        return idPrevision;
    }

    public double getReste() {
        return reste;
    }


}