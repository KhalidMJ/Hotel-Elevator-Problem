import java.util.ArrayList;
import java.util.Iterator;

public class Elevator {
    private final int CAPACITY = 10; // in passengers
    private final double MAX_WEIGHT = 900; // in kg
    private final double SPEED = 1.5; // seconds per floor

    private Hotel currentHotel; // A reference to the hotel that this elevator belong to
    private int currentFloor; // The current floor of the elevator
    private ArrayList<Passenger> currentPassengers; // The passengers who are currently in the elevator
    private CabButtons cabButtons; // The buttons inside the elevator

    private ElevatorStatus elevatorStatus; // The status of the elevator
    private DoorsStatus doorsStatus; // The status of the doors
    private double totalCurrentPassengersWeight; // The total weight of the passengers who are currently in the elevator

    // Constructor
    public Elevator(){
        this.currentFloor = 0;
        this.currentPassengers = new ArrayList<>();
        this.cabButtons = new CabButtons(8);
        this.elevatorStatus = ElevatorStatus.IDLE;
        this.doorsStatus = DoorsStatus.CLOSED;
    }

    // Recursive Method to move the elevator to a requested floor, it will check every floor on the way for requests.
    public synchronized void moveToAndScan(int level){
        // Base case: Elevator is already at the wanted level OR There is a passenger who want to get off on the current floor
        Floor servicedFloor = currentHotel.getFloors()[currentFloor];
        if (level == this.currentFloor) {
            pause();
            servicedFloor.elevatorArrival(this);
            return;
        }
        // Stop if the cab button or the call button of the current floor is clicked
        if (cabButtons.getButtonsStatus()[this.currentFloor] || getCallButtonOfFloor(this.currentFloor).isPressed()){
            pause();
            servicedFloor.elevatorArrival(this);
        }

        // Check the target floor and start moving the elevator, simulate the movement by delaying the thread.
        if (level > this.currentFloor) {
            moveUp();
        } else {
            moveDown();
        }
        moveToAndScan(level); // Recursive call
    }

    // Method to move the elevator up one floor
    public synchronized void moveUp(){
        this.elevatorStatus = ElevatorStatus.MOVING_UP;
        Simulation.delay(this.SPEED);
        this.currentFloor++;
    }

    // Method to move the elevator down one floor
    public synchronized void moveDown(){
        this.elevatorStatus = ElevatorStatus.MOVING_DOWN;
        Simulation.delay(this.SPEED);
        this.currentFloor--;
    }

    // Method to pause the elevator and update its status
    public void pause(){
        this.elevatorStatus = ElevatorStatus.IDLE;
    }

    // Method to open the doors of the elevator and update the doors status
    public void openDoors(){
        // if doors already open, do nothing
        if (this.doorsStatus == DoorsStatus.OPEN){
            return;
        }
        // update the doors status, and simulate the opening of the doors
        this.doorsStatus = DoorsStatus.OPENING;
        Simulation.delay(1.5); // It would take the doors 2 seconds to be fully open
        this.doorsStatus = DoorsStatus.OPEN;
    }

    // Method to open the doors of the elevator and update the doors status
    public void closeDoors(){
        // if doors already closed, do nothing
        if (this.doorsStatus == DoorsStatus.CLOSED){
            return;
        }
        // update the doors status, and simulate the closing of the doors
        this.doorsStatus = DoorsStatus.CLOSING;
        Simulation.delay(1.5); // It would take the doors 1 seconds to be fully closed
        this.doorsStatus = DoorsStatus.CLOSED;
    }


    // Method to check if there are requests above the current floor
    public boolean hasRequestsAbove(){
        for (int i = this.currentFloor + 1; i < this.currentHotel.getFloors().length; i++){
            if (this.cabButtons.getButtonsStatus()[i] || getCallButtonOfFloor(i).isPressed()){
                return true;
            }
        }
        return false;
    }

    // Method to check if there are requests below the current floor
    public boolean hasRequestsBelow(){
        for (int i = this.currentFloor - 1; i >= 0; i--){
            if (this.cabButtons.getButtonsStatus()[i] || getCallButtonOfFloor(i).isPressed()){
                return true;
            }
        }
        return false;
    }

    //  Method to load a passenger to the elevator, it returns true if the loading is completed, false if elevator rejected the passenger
    public boolean checkPassenger(Passenger passenger){
        if ((this.currentPassengers.size() < CAPACITY) && (passenger.getWeight() + this.totalCurrentPassengersWeight < MAX_WEIGHT)){
            return true;
        }
        return false;
    }

    // Method to unload passengers who have reached their destination floor
    public synchronized void unloadPassengers(){
        // Loop through the current passengers and remove those who have reached their destination floor
        Iterator<Passenger> it = currentPassengers.iterator();
        while (it.hasNext()){
            Passenger passenger = it.next();
            if (passenger.getDestinationFloor() == currentFloor){
                passenger.exitElevator();
                totalCurrentPassengersWeight -= passenger.getWeight();
                it.remove();
            }
        }
    }

    // Method to load passengers who are waiting on the floor
    public synchronized void loadPassengers(ArrayList<Passenger> waitingPassengers) {
        // saving a copy of the passengers who are loaded into the elevator
        ArrayList<Passenger> loadedPassengers = new ArrayList<>();
        // Loop through the waiting passengers and load those who are allowed to enter the elevator
        for (Passenger passenger : waitingPassengers){
            if (checkPassenger(passenger)){
                passenger.enterElevator();
                currentPassengers.add(passenger);
                totalCurrentPassengersWeight += passenger.getWeight();
                this.cabButtons.pressButton(passenger.getDestinationFloor());
                loadedPassengers.add(passenger);
            }
        }
        // Remove the loaded passengers from the waiting passengers list
        waitingPassengers.removeAll(loadedPassengers);
    }

    // Method to start the elevator
    public void start(String algorithm){
        if (algorithm.equals("SCAN")){
            SCAN();
        } else if (algorithm.equals("C-LOOK")){
            C_LOOK();
        }
    }

    // Method to move the elevator up and down between the top and bottom floors, and check for passengers on each floor.
    public void SCAN(){
        do {
            // Move to the top floor
            moveToAndScan(this.currentHotel.getFloors().length - 1);
            // Move to the bottom floor
            moveToAndScan(0);
        } while (!Simulation.isSimEnded());
    }

    public void C_LOOK(){
        do {
            // move up once until there are no requests above
            while (hasRequestsAbove()){
                if (cabButtons.getButtonsStatus()[this.currentFloor] || getCallButtonOfFloor(this.currentFloor).isPressedUp()){
                    pause();
                    currentHotel.getFloors()[this.currentFloor].elevatorArrival(this);
                }
                moveUp();
            }
            // move down once until there are no requests below
            while (hasRequestsBelow()){
                if (cabButtons.getButtonsStatus()[this.currentFloor] || getCallButtonOfFloor(this.currentFloor).isPressedDown()){
                    pause();
                    currentHotel.getFloors()[this.currentFloor].elevatorArrival(this);
                }
                moveDown();
            }
            if (cabButtons.getButtonsStatus()[this.currentFloor] || getCallButtonOfFloor(this.currentFloor).isPressed()){
                pause();
                currentHotel.getFloors()[this.currentFloor].elevatorArrival(this);
            }
        } while (!Simulation.isSimEnded());
    }


    // Setters/Getters -------------------------------------

    // gets the capacity of the elevator
    public int getCAPACITY() {
        return CAPACITY;
    }

    // gets the maximum weight of the elevator
    public double getMAX_WEIGHT() {
        return MAX_WEIGHT;
    }

    // gets the speed of the elevator
    public double getSPEED() {
        return SPEED;
    }

    // gets the current hotel
    public int getCurrentFloor() {
        return currentFloor;
    }
    
    private CallButtons getCallButtonOfFloor(int floor) {
        Floor currentFloor = this.currentHotel.getFloors()[floor];
        return currentFloor.getCallButton();
    }

    // sets the current floor
    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    // gets the current passengers
    public ArrayList<Passenger> getCurrentPassengers(){
        return currentPassengers;
    }

    // gets the elevator status
    public ElevatorStatus getElevatorStatus() {
        return elevatorStatus;
    }

    // sets the elevator status
    public void setElevatorStatus(ElevatorStatus elevatorStatus) {
        this.elevatorStatus = elevatorStatus;
    }

    // gets the doors status
    public DoorsStatus getDoorsStatus() {
        return doorsStatus;
    }

    // gets the CabButtons
    public CabButtons getCabButtons() {
        return cabButtons;
    }

    // sets the doors status
    public void setDoorsStatus(DoorsStatus doorsStatus) {
        this.doorsStatus = doorsStatus;
    }

    // gets the total current passengers weight
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