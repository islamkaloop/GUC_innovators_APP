package eg.edu.guc.innovators;

public class DriverState{

    private boolean awake;
    private boolean notFocused;

    public DriverState( boolean awake, boolean notFocused){
        this.awake = awake;
        this.notFocused = notFocused;
    }

    public boolean isAwake(){
        return awake;
    }

    public boolean isNotFocused(){
        return notFocused;
    }
}
