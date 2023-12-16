
/**
 * Abstract class representing control buttons.
 * @author Ammar
 */
public abstract class ControlButtons {

        /**
         * The number of buttons in the control panel.
         */
        private int buttonsCount;

        /**
         * The status of all the buttons (true if pressed, false if not pressed).
         */
        private boolean[] buttonsStatus;  // status of all the buttons

        /**
         * Constructs a new ControlButtons instance with a specified number of buttons.
         *
         * @param buttonsCount The number of buttons in the control panel.
         */
        public ControlButtons(int buttonsCount){
                this.buttonsCount = buttonsCount;
                this.buttonsStatus = new boolean[buttonsCount];
        }

        /**
         * Clears the status of all buttons (sets them to false).
         */
        public void clearAllButtons(){
                for (int i=0; i<buttonsCount; i++){
                        buttonsStatus[i] = false;
                }
        }

        /**
         * Clears the status of a specific button (sets it to false).
         *
         * @param i The index of the button to clear.
         */
        public void clearButton(int i){
                buttonsStatus[i] = false;
        }

        /**
         * Presses a specific button (sets it to true).
         *
         * @param i The index of the button to press.
         */
        public void pressButton(int i){
                buttonsStatus[i] = true;
        }

        /**
         * Gets the number of buttons in the control panel.
         *
         * @return The number of buttons in the control panel.
         */
        public int getButtonsCount() {
                return buttonsCount;
        }

        /**
         * Gets the status of all buttons.
         *
         * @return An array representing the status of all buttons.
         */
        public boolean[] getButtonsStatus() {
                return buttonsStatus;
        }

        /**
         * Sets the status of all buttons.
         *
         * @param buttonsStatus The array representing the status of all buttons.
         */
        public void setButtonsStatus(boolean[] buttonsStatus) {
                this.buttonsStatus = buttonsStatus;
        }
}



