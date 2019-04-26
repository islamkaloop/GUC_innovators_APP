package eg.edu.guc.innovators;

public class Car {

    private int id_number;

    public int getId_number() {
        return id_number;
    }

    public void setId_number(int id_number) {
        this.id_number = id_number;
    }

    private double distance;
    private double velocity;

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public Car(){

    }

    public double getDistance(){
        return distance;
    }

    public double getVelocity(){
        return velocity;
    }
}
