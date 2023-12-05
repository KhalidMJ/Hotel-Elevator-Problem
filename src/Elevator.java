import java.util.ArrayList;

public class Elevator {
    private final int CAPACITY = 10;
    private final double MAX_WEIGHT = 700; // in kg
    private final int SPEED = 2; // seconds per floor

    private Hotel currentHotel; // A reference to the hotel that this elevator belong to
    private int currentFloor;
    private ArrayList<Passenger> currentPassengers;
    private CabButtons cabButtons;

    private ElevatorStatus elevatorStatus;
    private DoorsStatus doorsStatus;
    private double totalCurrentPassengersWeight;

    public Elevator(){
        this.currentFloor = 0;
        this.currentPassengers = new ArrayList<>();
        this.cabButtons = new CabButtons(8);
        this.elevatorStatus = ElevatorStatus.IDLE;
        this.doorsStatus = DoorsStatus.OPEN;
    }
    // Recursive Method to move the elevator to a requested floor
    public void moveTo(int level){
        // Base case: Elevator is already at the wanted level OR There is a passenger who want to get off on the current floor
        if (level == this.currentFloor) {
            pause();
            openDoors();
            currentHotel.getFloors()[currentFloor].elevatorArrival(this); // Notify the floor that the elevator arrived and is ready
            return;
        }
        if (cabButtons.buttonsStatus[this.currentFloor]){ // Stop if the cab button of the current floor is clicked
            pause();
            openDoors();
            currentHotel.getFloors()[currentFloor].elevatorArrival(this);
        }

        if (this.doorsStatus != DoorsStatus.CLOSED){ // Making sure that the elevator won't move with its doors open
            closeDoors();
        }
        // Assertions TODO: change it into an exception
        assert (level >= 0 && level <= 7) : "The elevator is bound to move between 0 and 7 floors only";
        assert this.doorsStatus == DoorsStatus.CLOSED : "The doors are not closed";

        // Check the target floor and start moving the elevator
        if (level > this.currentFloor) {
            this.elevatorStatus = ElevatorStatus.MOVING_UP;
            Simulation.delay(this.SPEED);
            this.currentFloor++;
        } else {
            this.elevatorStatus = ElevatorStatus.MOVING_DOWN;
            Simulation.delay(this.SPEED);
            this.currentFloor--;
        }
        moveTo(level);
    }

    // Method to pause the elevator and update its status
    public void pause(){
        this.elevatorStatus = ElevatorStatus.IDLE;
    }

    // Method to open the doors of the elevator and update the doors status
    public void openDoors(){
        if (this.doorsStatus == DoorsStatus.OPEN) return; // if doors already open, do nothing
        this.doorsStatus = DoorsStatus.OPENING;
        Simulation.delay(1); // It would take the doors 1 seconds to be fully open
        this.doorsStatus = DoorsStatus.OPEN;
    }

    // Method to open the doors of the elevator and update the doors status
    public void closeDoors(){
        if (this.doorsStatus == DoorsStatus.CLOSED) return; // if doors already closed, do nothing
        this.doorsStatus = DoorsStatus.CLOSING;
        Simulation.delay(1); // It would take the doors 1 seconds to be fully closed
        this.doorsStatus = DoorsStatus.CLOSED;
    }
    //  Method to load a passenger to the elevator, it returns true if the loading is completed, false if elevator rejected the passenger
    public boolean loadPassenger(Passenger passenger){
        if ((this.currentPassengers.size() < CAPACITY) && (passenger.getWeight() + this.totalCurrentPassengersWeight < MAX_WEIGHT)){
            currentPassengers.add(passenger);
            totalCurrentPassengersWeight += passenger.getWeight();
            return true;
        }
        return false;
    }

    public void unloadPassenger(Passenger passenger){
        currentPassengers.remove(passenger);
        passenger.hasArrived();
        totalCurrentPassengersWeight -= passenger.getWeight();
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

    public ArrayList<Passenger> getCurrentPassengers(){
        return currentPassengers;
    }

    public ElevatorStatus getElevatorStatus() {
        return elevatorStatus;
    }

    public void setElevatorStatus(ElevatorStatus elevatorStatus) {
        this.elevatorStatus = elevatorStatus;
    }

    public DoorsStatus getDoorsStatus() {
        return doorsStatus;
    }

    public void setDoorsStatus(DoorsStatus doorsStatus) {
        this.doorsStatus = doorsStatus;
    }

    public void setCurrentHotel(Hotel hotel){
        this.currentHotel = hotel;
    }

    @Override
    public String toString() {
        return "This elevator is at " + this.currentFloor + " and it is " + elevatorStatus + ", the doors are " + this.doorsStatus + "\r";
    }
}

// enums for the elevator status and elevator's doors status
enum ElevatorStatus {MOVING_UP, MOVING_DOWN, IDLE}

enum DoorsStatus {OPEN, CLOSED, OPENING, CLOSING}