import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


public class Test {
    public static void main(String[] args){
        String path = "src/Passengers.csv";
        String line = "";
        ArrayList<String> pas = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            int i = 0;
            while ((line = reader.readLine()) != null){
                if (i == 0) {i++; continue;}

                String[] values = line.split(",");
                pas.addAll(List.of(values));
            }
        } catch (Exception e){
            System.out.println("ERROR");

        } finally {

        }
        System.out.println(pas);
    }
}
