public class Hotel {
    private int floorCount;
    private Floor[] floors;
    private int elevatorCount;
    private Elevator[] elevators;

    public Hotel(int floorCount, int elevatorsCount) {
        this.floorCount = floorCount;
        this.elevatorCount = elevatorsCount;

        floors = new Floor[floorCount];
        for (int i = 0; i < this.floorCount; i++){
            floors[i] = new Floor(i, "Normal");
        }

        elevators = new Elevator[elevatorCount];
        for (int i = 0; i < this.floorCount; i++){
            elevators[i] = new Elevator();
        }
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
        return this.elevators;
    }
}
