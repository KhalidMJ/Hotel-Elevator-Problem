public class Main {
    public static void main(String[] args){
        // Creating the simulation main objects -----------------------

        Floor[] floors = new Floor[8]; // creating floors objects
        for (int i = 0; i < 8; i++) {floors[i] = new Floor(i, "res");}

        Elevator[] elev = {new Elevator(), new Elevator()}; // creating elevators
        Hotel hotel = new Hotel(floors, elev); // creating hotel object

    }
}
