/*
 this class is about the up and down buttons (the outside buttons )
 */
/**
 * Represents the up and down buttons (outside buttons) for requesting elevators.
 * @author Ammar
 */
public class CallButtons extends ControlButtons implements RequestElevator
{
    /**
     * Constructs a new CallButtons instance with a specified number of buttons.
     *
     * @param buttonsCount The number of buttons in the call panel.
     */
    public CallButtons(int buttonsCount) {
        super(buttonsCount);
    }

    /**
     * Presses the up button to request an elevator going up.
     */
    @Override
    public void requestUp() {
        pressButton(0);
    }


    /**
     * Presses the down button to request an elevator going down.
     */
    @Override
    public void requestDown() {
        pressButton(1);
    }

    /**
     * Checks if any of the call buttons (up or down) are pressed.
     *
     * @return True if any of the call buttons are pressed, false otherwise.
     */
    public boolean isPressed(){
        return getButtonsStatus()[0] || getButtonsStatus()[1];
    }
}
