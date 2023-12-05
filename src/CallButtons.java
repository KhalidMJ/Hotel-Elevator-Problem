/*
 this class is about the up and down buttons (the outside buttons )
 */

import java.util.Scanner;
public class CallButtons extends ControlButtons implements RequestElevator
{

    private static final int GROUND_FLOOR = 1; // the Ground floor in the hotel
    private static final int TOP_FLOOR = 8 ; // the top floor in the hotel

    // Current Floor
    private static final int currentFloor = GROUND_FLOOR;

    // Scanner for user input
    private Scanner scanner = new Scanner(System.in);

    private void displayCurrentFloor(){
        System.out.println("Current floor: (1-" + currentFloor + "):");

    }
    private int selectFloor(){
        System.out.println("Enter desired floor "+ TOP_FLOOR);
    }


    @Override
    public void requestUp() {

    }

    @Override
    public void requestDown() {

    }
}
