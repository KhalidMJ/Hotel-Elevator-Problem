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
        pressButton(0);
    }

    @Override
    public void requestDown() {
        pressButton(1);
    }

    public boolean isPressed(){
        return getButtonsStatus()[0] || getButtonsStatus()[1];
    }
}
