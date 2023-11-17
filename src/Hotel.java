public class Hotel {
    private int floorCount;
    private Floor[] floors;
    private int elevatorCount;
    private Elevator[] elevators;

    public Hotel(Floor[] floors, Elevator[] elevators) {
        this.floors = floors;
        this.elevators = elevators;
        this.floorCount = this.floors.length;
        this.elevatorCount = this.elevators.length;
    }

    public int getFloorCount() {
        return floorCount;
    }

    public Floor[] getFloors() {
        return floors;
    }

    public int getElevatorCount() {
        return elevatorCount;
    }

    public Elevator[] getElevators() {
        return elevators;
    }
}
