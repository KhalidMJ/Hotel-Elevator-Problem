public class Passenger{
    private final String name;
    private final String id;
    private final double weight;
    private final int age;
    private int currentFloor;
    private int destinationFloor;
    private long arrivalTime;
    private long departureTime;
    private long waitingTime;


    protected Passenger(String name, String id, double weight, int age, long arrivalTime, int currentFloor, int destinationFloor) {
        this.name = name;
        this.id = id;
        this.weight = weight;
        this.age = age;
        this.currentFloor = currentFloor;
        this.destinationFloor = destinationFloor;
        this.arrivalTime = arrivalTime;

    }


    protected Passenger() {
        this.name = "Anonymous  guest";
        this.id = "11111";
        this.weight = 70;
        this.age = 25;
    }

    public void enterElevator() {
        Simulation.delay(2);
    }

    public void exitElevator() {
        Simulation.delay(2);
        departureTime = Simulation.getElapsedTime();
        waitingTime = departureTime - this.arrivalTime;
        Simulation.addWaitingTime(this.waitingTime);
        ResultsTablePane.updateTable(this);
    }

    public void requestUp(CallButtons callButtons) {
        arrivalTime = Simulation.getElapsedTime();
        callButtons.requestUp();
    }

    public void requestDown(CallButtons callButtons) {
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
        return this.currentFloor;
    }

    public int getDestinationFloor() {
        return this.destinationFloor;
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