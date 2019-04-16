package eg.edu.guc.innovators;

public class Indicators {

    private boolean left;
    private boolean right;

    public Indicators( boolean left, boolean right){
        this.left = left;
        this.right = right;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isRight() {
        return right;
    }
}
