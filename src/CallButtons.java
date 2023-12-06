/*
 this class is about the up and down buttons (the outside buttons )
 */

public class CallButtons extends ControlButtons implements RequestElevator
{

    public CallButtons(int buttonsCount) {
        super(buttonsCount);
    }



    @Override
    public void requestUp() {
        buttonsStatus[1] = true;

    }

    @Override
    public void requestDown() {
        buttonsStatus[0] = false;
    }
}
