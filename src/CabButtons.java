public class CabButtons extends ControlButtons{


    for (int=0; i< floorButtons.length; i++)
        if (source == floorButtons[i]){
        int destinationFloor = i + 1;

            if (destinationFloor > currentFloor) {
                direction = "Up";
            } else if (destinationFloor < currentFloor) {
                direction = "Down";
            }

            currentFloor = destinationFloor;

    }

}
