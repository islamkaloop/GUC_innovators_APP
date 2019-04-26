package eg.edu.guc.innovators;

public class DriverState{

    private boolean awake;
    private boolean notFocused;

    public void setAwake(boolean awake) {
        this.awake = awake;
    }

    public void setNotFocused(boolean notFocused) {
        this.notFocused = notFocused;
    }

    public DriverState(){

    }

    public boolean isAwake(){
        return awake;
    }

    public boolean isNotFocused(){
        return notFocused;
    }
}
