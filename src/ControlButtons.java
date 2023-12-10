
// ControlButtons class has these attributes:
public abstract class ControlButtons {
        private int buttonsCount;
        private boolean[] buttonsStatus;  // status of all the buttons


        public ControlButtons(int buttonsCount){
                this.buttonsCount = buttonsCount;
                this.buttonsStatus = new boolean[buttonsCount];
        }
        // this method to clear all buttons
        public void clearAllButtons(){
                for (int i=0; i<buttonsCount; i++){
                        buttonsStatus[i] = false;
                }
        }
        // this method to clear a button
        public void clearButton(int i){
                buttonsStatus[i] = false;
        }

        // this method return the number of control buttons
        public int getButtonsCount() {
            return buttonsCount;
        }

        // this method will return the buttons' status
        public boolean[] getButtonsStatus() {
            return buttonsStatus;
        }

        public void pressButton(int i){
                buttonsStatus[i] = true;
        }
}



