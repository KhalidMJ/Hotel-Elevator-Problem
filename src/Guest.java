import java.sql.Time;

public class Guest extends Passenger {
    private int currentFloor;
    private int destinationFloor;
    private Time requestTime;
    private Time arriveTime;

    public Guest(int currentFloor, int destinationFloor){
        super();
        this.currentFloor = currentFloor;
        this.destinationFloor = destinationFloor;
    }

    public Guest() {
        super();
    }

    public void requestUp() {
        this.destinationFloor = this.currentFloor + 1;
    }

    public void requestDown() {
        this.destinationFloor = this.currentFloor - 1;
    }

    public String getName() {
        return this.name;
    }
    public int getId() {
        return this.id;
    }

    public double getWeight() {
        return this.weight;
    }

    public int currentFloor() {
        return this.currentFloor;
    }

    public int destinationFloor() {
        return this.destinationFloor;
    }
}