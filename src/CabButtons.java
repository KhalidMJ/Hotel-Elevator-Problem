public class CabButtons extends ControlButtons{


    public CabButtons(int buttonsCount) {
        super(buttonsCount);
    }


    public void pressButton(int level){
        buttonsStatus[level]= true;
    }



}
