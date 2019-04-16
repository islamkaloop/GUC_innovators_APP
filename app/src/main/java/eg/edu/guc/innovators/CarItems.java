package eg.edu.guc.innovators;

public class CarItems {
    private String id;
    private String destance;
    private String velocity;

    public CarItems(String id, String destance, String velocity) {
        this.id = id;
        this.destance = destance;
        this.velocity = velocity;
    }

    public String getId() {
        return id;
    }

    public String getDestance() {
        return destance;
    }

    public String getVelocity() {
        return velocity;
    }
}
