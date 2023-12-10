public class Passenger{
    private final String name;
    private final String id;
    private final double weight;
    private final int age;
    int currentFloor;
    int destinationFloor;
    long arrivalTime;
    long departureTime;
    long waitingTime;


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

    public long waitingTime() {
        return this.waitingTime;
    }

    public void enterElevator() {
        Simulation.delay(2);
        System.out.println(this.name + " entered the elevator at " + Simulation.getElapsedTime()/60 + " minutes and " + Simulation.getElapsedTime()%60 + " seconds.");
    }

    public void exitElevator() {
        Simulation.delay(2);
        departureTime = Simulation.getElapsedTime();
        waitingTime = departureTime - this.arrivalTime;
        System.out.println(this.name + " exited the elevator at " + Simulation.getElapsedTime()/60 + " minutes and " + Simulation.getElapsedTime()%60 + " seconds. Waiting time: " + waitingTime + " seconds.");
    }

    public void requestUp(CallButtons callButtons) {
        callButtons.requestUp();
    }

    public void requestDown(CallButtons callButtons) {
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