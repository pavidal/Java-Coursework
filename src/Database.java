import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Database {
    public Database() {
        // TODO: Maybe add some stuff
    }

    public static String[][] getStocks() throws FileNotFoundException {
        try (Scanner sc = new Scanner(new File("src/Data/Stock.txt"))) {
            // TODO: Do Something
        }

        return null;
    }
}
