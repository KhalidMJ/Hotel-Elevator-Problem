import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class Test {
    public static void main(String[] args){
        String path = "src/Passengers.csv";
        String[][] passengers = getPassengersFromFile(path);
        System.out.println(Arrays.deepToString(passengers));
    }

    public static String[][] getPassengersFromFile(String path){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            ArrayList<String> lines = new ArrayList<>(reader.lines().toList());
            int count = (int) (lines.size() - 1);
            int fields = (int) lines.get(0).split(",").length;
            String[][] passengers = new String[count][fields];

            for (int i = 1; i < lines.size(); i++){
                String[] values = lines.get(i).split(",");

                for (int j = 0; j < values.length; j++){
                    passengers[i - 1][j] = values[j]; // (i - 1) so that it would skip the labels row and only store passengers data
                }
            }

            reader.close();
            return passengers;
        } catch (Exception e) {
            System.out.println("ERROR");
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
            return null;
        }
    }
}
