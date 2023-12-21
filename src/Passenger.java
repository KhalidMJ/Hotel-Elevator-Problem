public abstract class Passenger implements RequestElevator{
    private final String name;
    private final String id;
    private final double weight;
    private final int age;
    private Floor currentFloor;
    private Floor destinationFloor;
    private long arrivalTime;
    private long departureTime;
    private long waitingTime;


    public Passenger(String name, String id, double weight, int age, long arrivalTime, Floor currentFloor, Floor destinationFloor) {
        this.name = name;
        this.id = id;
        this.weight = weight;
        this.age = age;
        this.currentFloor = currentFloor;
        this.destinationFloor = destinationFloor;
        this.arrivalTime = arrivalTime;
    }

    public Passenger() {
        this.name = "Anonymous  guest";
        this.id = "11111";
        this.weight = 70;
        this.age = 25;
    }

    // Method to simulate the passenger entering the elevator, and the time it takes to do so.
    public void enterElevator() {
        Simulation.delay(1.8);
    }

    // Method to simulate the passenger exiting the elevator, and calculate the waiting time.
    public void exitElevator() {
        Simulation.delay(1.8);
        departureTime = Simulation.getElapsedTime();
        waitingTime = departureTime - this.arrivalTime;
        Simulation.addWaitingTime(this.waitingTime);
        ResultsTablePane.updateTable(this); // updating the results table
    }

    public void requestUp() {
        CallButtons callButtons = currentFloor.getCallButton();
        arrivalTime = Simulation.getElapsedTime();
        callButtons.requestUp();
    }

    public void requestDown() {
        CallButtons callButtons = currentFloor.getCallButton();
        arrivalTime = Simulation.getElapsedTime();
        callButtons.requestDown();
    }

    // Setters and Getters ---------------------------------------------
    public String getName() {
        return this.name;
    }

    public String getId() {
        return this.id;
    }

    public double getWeight() {
        return this.weight;
    }

    public int getAge() {
        return this.age;
    }


    public int getCurrentFloor() {
        return this.currentFloor.getFloorNumber();
    }

    public int getDestinationFloor() {
        return this.destinationFloor.getFloorNumber();
    }


    public long getArrivalTime() {
        return this.arrivalTime;
    }

    public long getDepartureTime() {
        return this.departureTime;
    }

    public long getWaitingTime() {
        return this.waitingTime;
    }

    @Override
    public String toString() {
        return "Passenger{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", weight=" + weight +
                ", age=" + age +
                ", currentFloor=" + currentFloor +
                ", destinationFloor=" + destinationFloor +
                ", arrivalTime=" + arrivalTime +
                ", departureTime=" + departureTime +
                ", waitingTime=" + waitingTime +
                '}' + "\n";
    }

 }