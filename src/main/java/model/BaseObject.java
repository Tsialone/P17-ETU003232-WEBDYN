package model;



import java.util.List;

public abstract class BaseObject {
    private int id;
    public BaseObject (int id){
        this.id = id;
    }
    public BaseObject (){

    }
 
    public int getId (){
        return this.id;
    }
    public void setId(int id){
        this.id = id;
    }



    public abstract List<Object> findAll () throws Exception ;
    public abstract void save () throws Exception ;
    public abstract Object getById (int id) throws Exception;

    public abstract void update () throws Exception ;
    public abstract void delete () throws Exception ;
    public abstract List<Object> findAllPaginated(int index , int count) throws Exception;


}
