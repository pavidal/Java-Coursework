
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Database {
    public static final String PATH_LOG = "src/Data/ActivityLog.txt";
    public static final String PATH_USER = "src/Data/UserAccounts.txt";

    /**
     * Returns a
     *
     * @return [List<Item>] itemList
     * @throws FileNotFoundException - TODO Desc
     */
    public static Item[] getStock() throws FileNotFoundException {
        List<Item> itemList = new ArrayList<>();

        try (Scanner sc = new Scanner(new File("src/Data/Stock.txt"))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();

                if (!line.isEmpty()) {      // prevents empty lines from getting into list
                    String[] row = line.split(", ");

                    if (line.toLowerCase().contains("keyboard")) itemList.add(new Keyboard(row));
                    else if (line.toLowerCase().contains("mouse")) itemList.add(new Mouse(row));
                }
            }
        }
        return itemList.toArray(new Item[0]);
    }

    /**
     * @param path
     * @return
     * @throws FileNotFoundException
     */
    public static String[][] read(String path) throws FileNotFoundException {
        ArrayList<String[]> table = new ArrayList<String[]>();

        try (Scanner sc = new Scanner(new File(path))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] row = line.split(" , ");
                if (!line.isEmpty()) table.add(row);     // prevents empty lines from getting into list
            }
        }

        return table.toArray(new String[0][0]);
    }

    public static void append(String path, String row) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File(path), true))) {
            bw.newLine();       // assumes that there will be no newline char at EOF
            bw.write(row);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void write(String path, String file) {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(new File(path), false))) {
            bw.write(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static String formatter(String[] list) {
    	StringBuilder sb = new StringBuilder();
    	
    	for (int i = 0; i < list.length; i++) {
    		if (i == list.length - 1) {
    			sb.append(list[i]);
    		} else {
    			sb.append(list[i]).append(", ");
    		}
    	}
    	
    	return sb.toString();
    }
    
    public static String formatter(String[][] list) {
    	StringBuilder sb = new StringBuilder();
    	
    	for (int i = 0; i < list.length; i++) {
    		if (i == list.length - 1) {
    			sb.append(formatter(list[i]));
    		} else {
    			sb.append(formatter(list[i])).append(System.lineSeparator());
    		}
    	}
    	
    	return sb.toString();
    }
    
    public static boolean find(String[][] haystack, String needle) {
    	
    	for (String[] row : haystack) {
    		// TODO
    	}
    	
    	return false;
    }
}
