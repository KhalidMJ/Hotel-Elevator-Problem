import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Simulation {
    public static void main(String[] args){

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
        if (passengersData == null) return null;

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

    // Method to generate passengers data with random attributes and store them in CSV file
    public static void generateRandomPassengersToFile(int count, long firstPasTime, long lastPasTime, String path){
        String[] generatedPassengers = new String[count];
        String passenger;
        // Probability Distributions
        double[] currentFloorPD = {0.35, 0.10, 0.10, 0.09, 0.09, 0.09, 0.09, 0.09};
        double[] FloorFromResPD = {0.72, 0.04, 0.04, 0.04, 0.04, 0.04, 0.04, 0.04, 0.04};
        double[] FloorFromGroundPD = {0.00, 0.15, 0.15, 0.14, 0.14, 0.14, 0.14, 0.14};

        // Generating Random Passengers
        for (int i = 0; i < count; i++){ //FIXME: A passenger can have the same current and destination floors.
            passenger = "guest,"
                        + returnRandomName() + "," // Generating a random name
                        + returnRandomIndexFromRange(10000, 19999) + "," // Generating a random ID
                        + returnRandomIndexFromRange(30, 130) + "," // Generating a random weight
                        + returnRandomIndexFromRange(15, 90) + ","  // Generating a random age
                        + returnRandomIndexFromRange((int)firstPasTime, (int)lastPasTime) + "," // generating a random arrival time
                        + returnRandomIndexFromProbDist(currentFloorPD) + "," // generating a random current floor
                        + returnRandomIndexFromProbDist(FloorFromGroundPD) + "\n"; // generating a random destination floor

            generatedPassengers[i] = passenger;
        }

        // Writing the generated passengers into CSV file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, false))) {
            writer.write("Type,name,id,weight,age,arrivalTime,currentFloor,destinationFloor\n");
            for (String person : generatedPassengers) {
                writer.write(person);
            }

            System.out.println("String array successfully written to file: " + path);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }


    }

    // Method to return an index based on a probability distribution given by user
    private static int returnRandomIndexFromProbDist(double[] dist) throws ArithmeticException{
        if (Arrays.stream(dist).sum() != 1.0) throw new ArithmeticException("The sum of the probability distribution must be 1!");

        // dist is a reference variable, to avoid changing it a temp copy is created.
        double[] distCopy = dist.clone();
        double rand = Math.random();
        for (int i = 0; i < distCopy.length; i++){
            if (rand <= distCopy[i]) return i;
            distCopy[i + 1] += distCopy[i];
        }
        return 0;
    }

    // Method to return a random integer from a range.
    public static int returnRandomIndexFromRange(int min, int max){
        return java.util.concurrent.ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    // Method to return a random name string
    private static String returnRandomName(){
        String[] names = {"Khalid", "Ammar", "Fares", "Yones", "Anas", "Muhammed", "Al-Waleed",
                "Sami", "Saud", "Marwan", "Thamer", "Mazen", "Hamad", "Rayan", "Salem", "Moath",
                "Ahmad", "Talal", "Faisel", "Ali", "Ibrahim", "Mansoor", "Yahya", "Mousa", "Malak",
                "Samir", "Fuad", "Hazem", "Rabeh", "Nasser", "Fawaz", "Bassam", "Sultan", "Sabeesh",
                "Omar", "Riyadh", "Sulaim", "Salman", "Habeeb", "Loey", "Eyad", "Hamed", "Hazma",
                "Husam", "Amjad", "Asaad", "Basem", "Bilal", "Hazem", "Hassan", "Jawad", "Jihad",
                "Osama", "Suhaib", "Hadi", "Fayez", "Mubark", "Meshal", "Hatem", "Ayham", "Sari",
                "Basel", "Rakan", "Badar", "Fahad", "Yazeed", "Suheil", "Thabet", "Ramiz", "Yasser"};
        int index;
        index = java.util.concurrent.ThreadLocalRandom.current().nextInt(names.length);
        return names[index];
    }

    // This method will simulate a passage of time
    public static void delay(double seconds){
        try {
            Thread.sleep((long)(seconds * 1000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

