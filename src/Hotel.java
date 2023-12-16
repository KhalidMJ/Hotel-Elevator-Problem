public class Hotel {
    private Floor[] floors;
    private Elevator[] elevators;
    private Passenger[] passengers;

    public Hotel(int floorCount, int elevatorsCount) {

        floors = new Floor[floorCount];
        for (int i = 0; i < floorCount; i++){
            floors[i] = new Floor(i, "Normal");
        }

        elevators = new Elevator[elevatorsCount];
        for (int i = 0; i < elevatorsCount; i++){
            elevators[i] = new Elevator();
            elevators[i].setCurrentHotel(this);
        }
    }

    public synchronized void updateFloorPassengers(){
        int i = 0;
        do {
            if (passengers[i].getArrivalTime() <= Simulation.getElapsedTime()){
                int passCurrentFloor = passengers[i].getCurrentFloor();
                floors[passCurrentFloor].addPassenger(passengers[i]);
                i++;
            }
            Simulation.delay(0.2);
        } while (i < passengers.length);
    }

    public void setPassengers(Passenger[] passengers){
        this.passengers = passengers;
    }

    public Floor[] getFloors() {
        return this.floors;
    }


    public Elevator[] getElevators() {
        return this.elevators;
    }

}


