import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class Test {
    public static void main(String[] args){
        String path = "src/Passengers.csv";
        try {
            String[][] data = getPassengersFromFile(path);
            assert data != null;
            Passenger[] passengers = turnPassengersArrayIntoObjects(data);
            System.out.println(Arrays.toString(passengers));
        } catch (Exception e){
            e.printStackTrace();
        }



    }

    public static String[][] getPassengersFromFile(String path) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            ArrayList<String> lines = new ArrayList<>(reader.lines().toList());
            int count = lines.size() - 1;
            int fields = lines.get(0).split(",").length;
            String[][] passengers = new String[count][fields];

            for (int i = 1; i < lines.size(); i++){
                String[] values = lines.get(i).split(",");

                for (int j = 0; j < values.length; j++){
                    passengers[i - 1][j] = values[j]; // (i - 1) so that it would skip the labels row and only store passengers data
                }
            }

            reader.close();
            return passengers;
        } catch (FileNotFoundException fnfe) {
            System.out.println("There is no such a file");
            System.out.println("Make sure the file is called \"Passengers.csv\" and exist in \"src/Passengers.csv\" relative path");

            return null;
        } catch (IOException ioe){
            System.out.println(ioe.getMessage());
            return null;
        }
    }

    public static Passenger[] turnPassengersArrayIntoObjects(String[][] passengersData) throws IOException {
        int count = passengersData.length;
        int fields = passengersData[0].length;

        String name, id;
        double weight;
        int age, currentFloor, destinationFloor;
        long arrivalTime;

        int i = 0;
        Passenger[] passengerObjects = new Passenger[count];
        for (String[] data : passengersData) {
            name = data[1];
            id = data[2];
            weight = Double.parseDouble(data[3]);
            age = Integer.parseInt(data[4]);
            arrivalTime = Long.parseLong(data[5]);
            currentFloor = Integer.parseInt(data[5]);
            destinationFloor = Integer.parseInt(data[6]);

            if (data[0].equalsIgnoreCase("Guest")) {
                Guest passenger = new Guest(name, id, weight, age, currentFloor, destinationFloor, arrivalTime);
                passengerObjects[i] = passenger;
            } else if (data[0].equalsIgnoreCase("Staff")) {
                Staff passenger = new Staff(name, id, weight, age, currentFloor, destinationFloor, arrivalTime);
                passengerObjects[i] = passenger;
            } else {throw new IOException("CSV FILE FORMAT IS NOT CORRECT");}
            i++;
        }

        return passengerObjects;
    }
}
