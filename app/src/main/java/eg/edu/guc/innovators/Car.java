package eg.edu.guc.innovators;

public class Car extends Identity{

    private double distance;
    private double velocity;

    public Car(int id_number, double distance, double velocity){
        super(id_number);
        this.distance = distance;
        this.velocity = velocity;
    }

    public double getDistance(){
        return distance;
    }

    public double getVelocity(){
        return velocity;
    }
}
