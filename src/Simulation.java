import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Simulation {

    private static long startTime;
    private static boolean isSimEnded = false;
    private static ArrayList<Long> waitingTimes = new ArrayList<>();
    private static long sumOfWaitingTimes = 0;
    private static int passengersCount;

    // Method to extract passengers data form a csv file and store them into a 2D array
    public static String[][] getPassengersFromFile(String path) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            ArrayList<String> lines = new ArrayList<>(reader.lines().toList());

            // First line (labels) is omitted
            lines.remove(0);

            // Creating 2D array and store the extracted values in it
            int count = lines.size();
            int fields = lines.get(0).split(",").length;
            String[][] passengers = new String[count][fields];
            for (int i = 0; i < lines.size(); i++){
                String[] values = lines.get(i).split(",");
                for (int j = 0; j < values.length; j++){
                    passengers[i][j] = values[j];
                }
            }

            // Closing the reader, and returning the 2D array.
            reader.close();
            Simulation.passengersCount = passengers.length;
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
    public static Passenger[] turnPassengersArrayIntoObjects(String[][] passengersData, Hotel hotel) throws IOException {
        // Check if the input data is null
        if (passengersData == null) return null;

        // Initialize variables for passenger attributes
        int count = passengersData.length;
        String name, id;
        double weight;
        int age, currentFloor, destinationFloor;
        long arrivalTime;

        // Create an array of Passenger objects
        Passenger[] passengerObjects = new Passenger[count];

        // Loop through each row of the 2D array and create a passenger object, then store it in the array.
        int i = 0;
        for (String[] data : passengersData) {
            name = data[1];
            id = data[2];
            weight = Double.parseDouble(data[3]);
            age = Integer.parseInt(data[4]);
            arrivalTime = Long.parseLong(data[5]);
            currentFloor = Integer.parseInt(data[6]);
            destinationFloor = Integer.parseInt(data[7]);

            if (data[0].equalsIgnoreCase("Guest")) {
                Guest passenger = new Guest(name, id, weight, age,arrivalTime, hotel.getFloors()[currentFloor], hotel.getFloors()[destinationFloor]);
                passengerObjects[i] = passenger;
            } else if (data[0].equalsIgnoreCase("Staff")) {
                Staff passenger = new Staff(name, id, weight, age, arrivalTime, hotel.getFloors()[currentFloor], hotel.getFloors()[destinationFloor]);
                passengerObjects[i] = passenger;
            } else {
                // Throw an IOException if the CSV file format is not correct
                throw new IOException("CSV FILE FORMAT IS NOT CORRECT");
            }
            i++;
        }
        return passengerObjects;
    }

    // Method to generate passengers data with random attributes and store them in CSV file
    public static void generateRandomPassengersToFile(int count, long firstPasTime, long lastPasTime, String path){
        String[] generatedPassengers = new String[count];
        String passenger;
        // Probability Distributions for Guest passengers
        double[] guestCurrentFloorPD = {0.35, 0.11, 0.11, 0.11, 0.10, 0.08, 0.07, 0.07};
        double[] guestDestinationFloorFromResPD = {0.65, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05, 0.05};
        double[] guestDestinationFloorFromGroundPD = {0.00, 0.15, 0.15, 0.14, 0.14, 0.14, 0.14, 0.14};

        // Probability Distributions for Staff passengers
        double[] staffCurrentFloorPD = {0.08, 0.15, 0.15, 0.15, 0.14, 0.12, 0.11, 0.10};
        double[] staffDestinationFloorFromResPD = {0.05, 0.15, 0.14, 0.14, 0.13, 0.13, 0.13, 0.13};
        double[] staffDestinationFloorFromGroundPD = {0.00, 0.15, 0.15, 0.14, 0.14, 0.14, 0.14, 0.14};

        // Generating Random Passengers
        for (int i = 0; i < count; i++){
            // 20% chance to generate a staff passenger
            boolean isStaff = Math.random() <= 0.3;

            passenger = isStaff ? "Staff," : "Guest,"; // Adding the passenger type to the string
            passenger += returnRandomName() + "," // Generating a random name
                        + returnRandomIndexFromRange(10000, 19999) + "," // Generating a random ID
                        + returnRandomIndexFromRange(30, 130) + "," // Generating a random weight
                        + returnRandomIndexFromRange(15, 70) + ","  // Generating a random age
                        + returnRandomIndexFromRange((int)firstPasTime, (int)lastPasTime) + ","; // generating a random arrival time

            // generating a random current floor and destination floor based on the probability distributions, and making sure that the destination floor is not the same as the current floor
            int currentFloor = returnRandomIndexFromProbDist(isStaff ? staffCurrentFloorPD : guestCurrentFloorPD);
            int destination;
            if (currentFloor == 0){
                destination = returnRandomIndexFromProbDist(isStaff ? staffDestinationFloorFromGroundPD : guestDestinationFloorFromGroundPD);
                while (destination == currentFloor){
                    destination = returnRandomIndexFromProbDist(isStaff ? staffDestinationFloorFromGroundPD : guestDestinationFloorFromGroundPD);
                }
            } else {
                 destination = returnRandomIndexFromProbDist(isStaff ? staffDestinationFloorFromResPD : guestDestinationFloorFromResPD);
                while (destination == currentFloor){
                    destination = returnRandomIndexFromProbDist(isStaff ? staffDestinationFloorFromResPD : guestDestinationFloorFromResPD);
                }
            }

            // Adding the current floor and destination floor to the passenger string and adding it to the array
            passenger = passenger + currentFloor + "," + destination + "\n";
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
        // Check if the sum of the probability distribution is 1
        if (Arrays.stream(dist).sum() != 1.0) {
            throw new ArithmeticException("The sum of the probability distribution must be 1!");
        }

        // dist is a reference variable, to avoid changing it a temp copy is created.
        double[] distCopy = dist.clone();
        double rand = Math.random();
        for (int i = 0; i < distCopy.length; i++){
            if (rand <= distCopy[i]) return i;
            distCopy[i + 1] += distCopy[i];
        }
        return 0;
    }

    // Method to sort an array of Passenger objects by arrival time
    public static Passenger[] sortPassengersByArrivalTime(Passenger[] passengers){
        // Clone the input array to avoid modifying the original array
        Passenger[] sortedPassengers = passengers.clone();
        Passenger temp;

        // Bubble sort algorithm to sort passengers based on arrival time
        for (int i = 0; i < sortedPassengers.length; i++){
            for (int j = 0; j < sortedPassengers.length - 1; j++){
                if (sortedPassengers[j].getArrivalTime() > sortedPassengers[j + 1].getArrivalTime()){
                    temp = sortedPassengers[j];
                    sortedPassengers[j] = sortedPassengers[j + 1];
                    sortedPassengers[j + 1] = temp;
                }
            }
        }
        return sortedPassengers;
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

    // This method will indicate that the simulation has ended
    public static void endSim(){
        isSimEnded = true;
    }

    // This method will set the start time of the simulation
    public static void setStartTime(){
        startTime = System.currentTimeMillis();
    }

    // This method will return the elapsed time since the start of the simulation
    public static long getElapsedTime(){
        long elapsedTime = (System.currentTimeMillis() / 1000) - (startTime / 1000);
        return elapsedTime;
    }

    // This method will return true if the simulation has ended
    public static boolean isSimEnded() {
        return isSimEnded;
    }

    public static void addWaitingTime(long waitingTime){
        waitingTimes.add(waitingTime);
        sumOfWaitingTimes += waitingTime;
        if (waitingTimes.size() == Simulation.passengersCount){
            endSim();
        }
    }

    public static long getAverageWaitingTime(){
        if (waitingTimes.isEmpty()) return 0; // If size is zero, it means no passengers have been added yet.
        return sumOfWaitingTimes/waitingTimes.size();
    }
}



