
// ControlButtons class has these attributes:
public abstract class ControlButtons {
        private int buttonsCount;
        private boolean[] buttonsStatus;  // status of all the buttons

        // Constructor
        public ControlButtons(int buttonsCount){
                this.buttonsCount = buttonsCount;
                this.buttonsStatus = new boolean[buttonsCount];
        }

        // this method will clear all buttons
        public void clearAllButtons(){
                for (int i=0; i < buttonsCount; i++){
                        buttonsStatus[i] = false;
                }
        }

        // this method will clear a button
        public void clearButton(int i){
                buttonsStatus[i] = false;
        }

        // this method for pressing a button
        public void pressButton(int i){
                buttonsStatus[i] = true;
        }

        // this method return the number of control buttons
        public int getButtonsCount() {
                return buttonsCount;
        }

        // this method will return the buttons' status
        public boolean[] getButtonsStatus() {
                return buttonsStatus;
        }

        // this method will set the buttons' status
        public void setButtonsStatus(boolean[] buttonsStatus) {
                this.buttonsStatus = buttonsStatus;
        }
}