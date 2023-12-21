public class Hotel {
    private Floor[] floors;
    private Elevator[] elevators;
    private Passenger[] passengers;

    // Constructor
    public Hotel(int floorCount, int elevatorsCount) {
        // Create floors
        floors = new Floor[floorCount];
        for (int i = 0; i < floorCount; i++){
            floors[i] = new Floor(i, "Normal");
        }

        // Create elevators
        elevators = new Elevator[elevatorsCount];
        for (int i = 0; i < elevatorsCount; i++){
            elevators[i] = new Elevator();
            elevators[i].setCurrentHotel(this);
        }
    }

    // Method to update the waiting passengers on the floors
    public synchronized void updateFloorPassengers(){
        int i = 0;
        do {
            // Check if the passenger has arrived, if so, add him to the floor
            if (passengers[i].getArrivalTime() <= Simulation.getElapsedTime()){
                int passCurrentFloor = passengers[i].getCurrentFloor();
                floors[passCurrentFloor].addPassenger(passengers[i]);
                i++;
            }
            Simulation.delay(0.2); // update every 0.2 seconds
        } while (i < passengers.length);
    }

    // Method to set the passengers
    public void setPassengers(Passenger[] passengers){
        this.passengers = passengers;
    }

    // Method to get the passengers
    public Floor[] getFloors() {
        return this.floors;
    }

    // Method to get the elevators
    public Elevator[] getElevators() {
        return this.elevators;
    }
}