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

    public void setPassengers(Passenger[] passengers){
        this.passengers = passengers;
    }
    public Floor[] getFloors() {
        return this.floors;
    }


    public Elevator[] getElevators() {
        return this.elevators;
    }

    public void updateFloorPassengers(){
        // Check every 1 second for passengers arrival time
        // If Elepsedtime > passenger.getArrivaltime, Floor[currentFLoor].addPassegner(passenger)
    }
}


