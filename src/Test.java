import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class Test {
    public static void main(String[] args){
        String path = "src/Passengers.csv";
        String[][] passengers = getPassengersFromFile(path);
        System.out.println(Arrays.deepToString(passengers));
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
}
