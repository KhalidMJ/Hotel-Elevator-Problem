import java.util.Arrays;

public class Elevator {
    private final int CAPACITY = 10;
    private final double MAX_WEIGHT = 700; // in kg
    private final int SPEED = 3; // seconds per floor

    private int currentFloor;
    private Passenger[] currentPassengers; // Should we use LinkedList instead?
    private CabButtons cabButtons;
    private boolean doorIsOpen;
    private boolean isMovingDown;
    private boolean isMovingUp;
    private boolean isPaused;

    public Elevator(){
        this.currentFloor = 0;
        this.currentPassengers = new Passenger[this.CAPACITY];
        this.cabButtons = new CabButtons();
        this.doorIsOpen = false;
        this.isMovingUp = false;
        this.isMovingDown = false;
        this.isPaused = true;
    }

    public void moveTo(int level){

    }
    public void pause(){

    }

    public void openDoors(){

    }

    public void  closeDoors(){

    }

    // Setters/Getters -------------------------------------


    public int getCAPACITY() {
        return CAPACITY;
    }

    public double getMAX_WEIGHT() {
        return MAX_WEIGHT;
    }

    public int getSPEED() {
        return SPEED;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public boolean isDoorIsOpen() {
        return doorIsOpen;
    }

    public boolean isMovingUp() {
        return isMovingUp;
    }

    public boolean isMovingDown() {
        return isMovingDown;
    }

    public boolean isPaused() {
        return isPaused;
    }

    @Override
    public String toString() {
        return "This elevator is at " + this.currentFloor;
    }
}
