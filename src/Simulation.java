import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

// TODO Create a method that would generate random passengers and store them in the CSV files

public class Simulation {
    public static void main(String[] args){
        String path = "src/Passengers.csv";
        // TEST UNIT - main function well be removed later
        try {
            String[][] data = getPassengersFromFile(path);
            assert data != null;
            Passenger[] passengers = turnPassengersArrayIntoObjects(data);
            System.out.println(Arrays.toString(passengers));
        } catch (Exception e){
            e.printStackTrace();
        }



    }
// Method to extract passengers data form a csv file and store them into a 2D array
    public static String[][] getPassengersFromFile(String path) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            ArrayList<String> lines = new ArrayList<>(reader.lines().toList());
            lines.remove(0); // First line (labels) is omitted
            int count = lines.size();
            int fields = lines.get(0).split(",").length;
            // Creating 2D array and store the extracted values in it
            String[][] passengers = new String[count][fields];
            for (int i = 0; i < lines.size(); i++){
                String[] values = lines.get(i).split(",");

                for (int j = 0; j < values.length; j++){
                    passengers[i][j] = values[j];
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

    // Method to create Passenger objects using the data from 2D array and return them in an array.
    public static Passenger[] turnPassengersArrayIntoObjects(String[][] passengersData) throws IOException {
        int count = passengersData.length;
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
