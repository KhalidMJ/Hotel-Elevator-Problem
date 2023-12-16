import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Simulation class represents the simulation environment.
 * It includes methods for extracting passenger data from a CSV file, creating Passenger objects,
 * generating random passengers, and simulating the passage of time.
 * @author Khalid
 */
public class Simulation {

    /**
     * The start time of the simulation in milliseconds.
     */
    private static long startTime;

    /**
     * A flag indicating whether the simulation has ended.
     */
    private static boolean isSimEnded = false;

    /**
     * List to store waiting times of passengers during the simulation.
     */
    private static ArrayList<Long> waitingTimes = new ArrayList<>();

    /**
     * The sum of waiting times of all passengers.
     */
    private static long sumOfWaitingTimes = 0;

    /**
     * The total number of passengers in the simulation.
     */
    private static int passengersCount;


    /**
     * Method to extract passengers data from a CSV file and store them into a 2D array.
     *
     * @param path The path to the CSV file.
     * @return A 2D array representing passengers data.
     */
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

    /**
     * Method to create Passenger objects using the data from a 2D array and return them in an array.
     *
     * @param passengersData The 2D array containing passengers' data.
     * @return An array of Passenger objects.
     * @throws IOException If there is an issue reading the data.
     */
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
            currentFloor = Integer.parseInt(data[6]);
            destinationFloor = Integer.parseInt(data[7]);

            if (data[0].equalsIgnoreCase("Guest")) {
                Guest passenger = new Guest(name, id, weight, age,arrivalTime, currentFloor, destinationFloor);
                passengerObjects[i] = passenger;
            } else if (data[0].equalsIgnoreCase("Staff")) {
                Staff passenger = new Staff(name, id, weight, age, arrivalTime, currentFloor, destinationFloor);
                passengerObjects[i] = passenger;
            } else {throw new IOException("CSV FILE FORMAT IS NOT CORRECT");}
            i++;
        }
        return passengerObjects;
    }

    /**
     * Method to generate passengers' data with random attributes and store them in a CSV file.
     * @param count      The number of passengers to generate.
     * @param firstPasTime The earliest arrival time for passengers in seconds.
     * @param lastPasTime The latest arrival time for passengers in seconds.
     * @param path       The path to the CSV file to store generated passengers data.
     */
    public static void generateRandomPassengersToFile(int count, long firstPasTime, long lastPasTime, String path){
        String[] generatedPassengers = new String[count];
        String passenger;
        // Probability Distributions
        double[] currentFloorPD = {0.35, 0.10, 0.10, 0.09, 0.09, 0.09, 0.09, 0.09};
        double[] FloorFromResPD = {0.72, 0.04, 0.04, 0.04, 0.04, 0.04, 0.04, 0.04};
        double[] FloorFromGroundPD = {0.00, 0.15, 0.15, 0.14, 0.14, 0.14, 0.14, 0.14};

        // Generating Random Passengers
        for (int i = 0; i < count; i++){ //FIXME: A passenger can have the same current and destination floors.
            passenger = "guest,"
                        + returnRandomName() + "," // Generating a random name
                        + returnRandomIndexFromRange(10000, 19999) + "," // Generating a random ID
                        + returnRandomIndexFromRange(30, 130) + "," // Generating a random weight
                        + returnRandomIndexFromRange(15, 90) + ","  // Generating a random age
                        + returnRandomIndexFromRange((int)firstPasTime, (int)lastPasTime) + ","; // generating a random arrival time

            int currentFloor = returnRandomIndexFromProbDist(currentFloorPD);
            int destination;
            if (currentFloor == 0){
                destination = returnRandomIndexFromProbDist(FloorFromGroundPD);
                while (destination == currentFloor){
                    destination = returnRandomIndexFromProbDist(FloorFromGroundPD);
                }
            } else {
                 destination = returnRandomIndexFromProbDist(FloorFromResPD);
                while (destination == currentFloor){
                    destination = returnRandomIndexFromProbDist(FloorFromResPD);
                }
            }


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

    /**
     * Method to return an index based on a probability distribution given by the user.
     *
     * @param dist The probability distribution array. The sum of its elements must be 1.0.
     * @return The selected index based on the given probability distribution.
     * @throws ArithmeticException If the sum of the probability distribution is not equal to 1.0.
     */
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

    /**
     * Method to sort an array of passengers by arrival time.
     *
     * @param passengers The array of passengers to be sorted.
     * @return A sorted array of passengers by arrival time.
     */
    public static Passenger[] sortPassengersByArrivalTime(Passenger[] passengers){
        Passenger[] sortedPassengers = passengers.clone();
        Passenger temp;
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

    /**
     * Method to return a random integer from a specified range.
     *
     * @param min The minimum value of the range.
     * @param max The maximum value of the range.
     * @return A random integer within the specified range.
     */
    public static int returnRandomIndexFromRange(int min, int max){
        return java.util.concurrent.ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    /**
     * Method to return a random name from a predefined list of names.
     *
     * @return A randomly selected name.
     */
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

    /**
     * This method will simulate the passage of time by causing the current thread to sleep.
     *
     * @param seconds The duration, in seconds, for which the thread will be suspended.
     */
    public static void delay(double seconds){
        try {
            Thread.sleep((long)(seconds * 1000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method will indicate that the simulation has ended.
     */
    public static void endSim(){
        isSimEnded = true;
    }

    /**
     * Method to set the start time of the simulation.
     */
    public static void setStartTime(){
        startTime = System.currentTimeMillis();
    }

    /**
     * Method to return the elapsed time since the start of the simulation.
     *
     * @return The elapsed time in seconds.
     */
    public static long getElapsedTime(){
        long elapsedTime = (System.currentTimeMillis() / 1000) - (startTime / 1000);
        return elapsedTime;
    }

    /**
     * Method to return true if the simulation has ended.
     *
     * @return True if the simulation has ended, false otherwise.
     */
    public static boolean isSimEnded() {
        return isSimEnded;
    }

    /**
     * Method to add waiting time for a passenger and check if the simulation has ended.
     *
     * @param waitingTime The waiting time of a passenger.
     */
    public static void addWaitingTime(long waitingTime){
        waitingTimes.add(waitingTime);
        sumOfWaitingTimes += waitingTime;
        if (waitingTimes.size() == Simulation.passengersCount){
            endSim();
        }
    }

    /**
     * Method to get the average waiting time of all passengers.
     *
     * @return The average waiting time in seconds.
     */
    public static long getAverageWaitingTime(){
        if (waitingTimes.isEmpty()) return 0; // If size is zero, it means no passengers have been added yet.
        return sumOfWaitingTimes/waitingTimes.size();
    }
}