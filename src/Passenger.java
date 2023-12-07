public abstract class Passenger {

    private final String name;
    private final String id;
    private final double weight;
    private final int age;
    int currentFloor;
    int destinationFloor;
    long arrivalTime;


    protected Passenger(String name, String id, double weight, int age,int currentFloor, int destinationFloor, long arrivalTime ) {
        this.name = name;
        this.id = id;
        this.weight = weight;
        this.age = age;
        this.currentFloor=currentFloor;
        this.destinationFloor=destinationFloor;
        this.arrivalTime=arrivalTime;
    }

    protected Passenger(){
        this.name = "Anonymous  guest";
        this.id = "11111";
        this.weight = 70;
        this.age = 25;
    }

    public abstract void hasArrived();

    public int getDestinationFloor(){
        return 99;
    }

    public void exitElevator() {

    }

    public void enterElevator(){

    }


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
    public void requestUp() {

        this.destinationFloor = this.currentFloor + 1;
    }

    public void requestDown() {
        this.destinationFloor = this.currentFloor - 1;
    }

    public int currentFloor() {
        return this.currentFloor;
    }

    public int destinationFloor() {
        return this.destinationFloor;
    }


    public long arrivalTime() {
        return this.arrivalTime();
    }

}