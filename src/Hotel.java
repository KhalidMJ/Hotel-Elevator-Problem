/**
 * Represents a hotel with floors, elevators, and passengers.
 * @author Younes
 */
public class Hotel {
    /**
     * The array of floors in the hotel.
     */
    private Floor[] floors;

    /**
     * The array of elevators in the hotel.
     */
    private Elevator[] elevators;

    /**
     * The array of passengers in the hotel.
     */
    private Passenger[] passengers;

    /**
     * Constructs a new Hotel instance with a specified number of floors and elevators.
     *
     * @param floorCount      The number of floors in the hotel.
     * @param elevatorsCount  The number of elevators in the hotel.
     */
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

    /**
     * Updates the floor with passengers whose arrival time has come.
     */
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

    /**
     * Sets the array of passengers in the hotel.
     *
     * @param passengers The array of passengers to set.
     */
    public void setPassengers(Passenger[] passengers){
        this.passengers = passengers;
    }

    /**
     * Gets the array of floors in the hotel.
     *
     * @return The array of floors.
     */
    public Floor[] getFloors() {
        return this.floors;
    }

    /**
     * Gets the array of elevators in the hotel.
     *
     * @return The array of elevators.
     */
    public Elevator[] getElevators() {
        return this.elevators;
    }
}


