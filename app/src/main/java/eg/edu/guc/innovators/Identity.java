package eg.edu.guc.innovators;

abstract public class Identity {

    private int id_number;

    public Identity(){

    }
    public Identity(int id_number){
        this.id_number = id_number;
    }

    public int getId_number(){
        return id_number;
    }


}
