public class CallButtons extends ControlButtons implements RequestElevator {

    public CallButtons(int buttonsCount) {
        super(buttonsCount);
    }

    // this method will press the up button
    @Override
    public void requestUp() {
        pressButton(0);
    }

    // this method will press the up button
    @Override
    public void requestDown() {
        pressButton(1);
    }

    // this method will return true if any button is pressed
    public boolean isPressed(){
        return getButtonsStatus()[0] || getButtonsStatus()[1];
    }
}
