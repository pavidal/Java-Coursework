
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

import Product.Item;
import Product.Keyboard;
import Product.Mouse;

public class Database {
	public static final String PATH_LOG = "Data/ActivityLog.txt";
	public static final String PATH_USER = "Data/UserAccounts.txt";
	public static final String PATH_ITEM = "Data/Stock.txt";

	/**
	 * DEPRECATED TODO: Remove
	 *
	 * @return [List<Item>] itemList
	 * @throws FileNotFoundException - TODO Desc
	 */
	public static Item[] getStock() throws FileNotFoundException {
		List<Item> itemList = new ArrayList<>();

		try (Scanner sc = new Scanner(new File(PATH_ITEM))) {
			while (sc.hasNextLine()) {
				String line = sc.nextLine();

				if (!line.isEmpty()) { // prevents empty lines from getting into list
					String[] row = line.split(", ");

					if (line.toLowerCase().contains("keyboard"))
						itemList.add(new Keyboard(row));
					else if (line.toLowerCase().contains("mouse"))
						itemList.add(new Mouse(row));
				}
			}
		}
		return itemList.toArray(new Item[0]);
	}

	/**
	 * Returns a list of rows from a delimited text file
	 * 
	 * @param path - [String] Path to file
	 * @return table - [ArrayList<String[]>] contents of file in 2D arraylist
	 * @throws FileNotFoundException
	 */
	public static ArrayList<String[]> read(String path) throws FileNotFoundException {
		ArrayList<String[]> table = new ArrayList<String[]>();

		try (Scanner sc = new Scanner(new File(path))) {
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] row = line.split(", ");
				if (!line.isEmpty())
					table.add(row); // prevents empty lines from getting into list
			}
		}

		return table;
	}

	/**
	 * Converts 2D ArrayList from read() to 2D array
	 * 
	 * @param arrList - [ArrayList<String[]>] File contents from read()
	 * @return File contents in 2D array
	 */
	public static String[][] toArray(ArrayList<String[]> arrList) {
		return arrList.toArray(new String[0][0]);
	}

	/**
	 * Appends to top of file
	 * 
	 * @param path
	 * @param line
	 */
	public static void append(String path, String line) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File(path), true))) {
			bw.newLine(); // assumes that there will be no newline char at EOF
			bw.write(line);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Overwrites a file
	 * 
	 * @param path
	 * @param file
	 */
	public static void write(String path, String file) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File(path), false))) {
			bw.write(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Format Array to a delimited String
	 * 
	 * @param list
	 * @return Delimited String
	 */
	public static String formatter(String[] list, String delimiter) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < list.length; i++) {
			if (i == list.length - 1) {
				sb.append(list[i]);
			} else {
				sb.append(list[i]).append(delimiter);
			}
		}

		return sb.toString();
	}

	/**
	 * Format Array to a comma delimited String
	 * 
	 * @param list
	 * @return Delimited String
	 */
	public static String formatter(String[] list) {
		return formatter(list, ", ");
	}

	/**
	 * Format 2D array to comma and newline delimited String
	 * 
	 * @param list
	 * @return Delimited String
	 */
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

	/**
	 * Get a list of all users from DB
	 * 
	 * @return ArrayList of all users
	 * @throws FileNotFoundException
	 */
	public static ArrayList<User> getUsers() throws FileNotFoundException {
		String[][] allUsers = toArray(read(PATH_USER));
		ArrayList<User> userList = new ArrayList<User>();

		for (String[] user : allUsers) {
			userList.add(new User(user));
		}

		return userList;
	}

	public static List<Keyboard> getKeyboards() throws FileNotFoundException {
		String[][] keyboardList = toArray(read(PATH_ITEM));
		List<Keyboard> itemList = new ArrayList<Keyboard>();

		for (String[] keyboard : keyboardList) {
			if (keyboard[1].equalsIgnoreCase("keyboard")) {
				itemList.add(new Keyboard(keyboard));
			}
		}

		return itemList;
	}

	public static List<Mouse> getMice() throws FileNotFoundException {
		String[][] miceList = toArray(read(PATH_ITEM));
		List<Mouse> itemList = new ArrayList<Mouse>();

		for (String[] mouse : miceList) {
			if (mouse[1].equalsIgnoreCase("mouse")) {
				itemList.add(new Mouse(mouse));
			}
		}

		return itemList;
	}

	public static Basket getSavedBasket(User user) throws FileNotFoundException {
		String[][] logs = toArray(read(PATH_LOG));
		List<Item> itemList = new ArrayList<Item>();

		String id = user.getID();
		String newestDate = "";

		for (String[] entry : logs) {
			if (entry[0].equals(id) && entry[5].equalsIgnoreCase("saved")) {
				if (!newestDate.isEmpty() && !entry[7].contentEquals(newestDate)) {
					break;
				} else {
					Item item = scanBarcode(entry[2]);
					item.quantity = Integer.parseInt(entry[4]);
					itemList.add(item);
					newestDate = entry[7];
				}
			}
		}

		return new Basket(itemList);
	}

	public static Item scanBarcode(String barcode) throws FileNotFoundException {
		String[][] stock = toArray(read(PATH_ITEM));

		for (String[] entry : stock) {
			if (entry[0].equals(barcode)) {
				if (entry[1].equalsIgnoreCase("keyboard")) {
					return new Keyboard(entry);
				} else if (entry[1].equalsIgnoreCase("mouse")) {
					return new Mouse(entry);
				}
			}
		}

		return null;
	}

	public static void cancelPurchase(User user, Basket basket) throws FileNotFoundException {
		List<Item> itemList = basket.getItems();
		StringBuilder sb = new StringBuilder();
		String toAppend = formatter(toArray(read(PATH_LOG)));

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String date = dateFormat.format(Calendar.getInstance().getTime());

		for (Item item : itemList) {
			String[] entry = { 
					user.getID(), 
					user.getPostcode().toUpperCase(), 
					String.valueOf(item.getBarcode()),
					String.valueOf(item.getSingularPrice()), 
					String.valueOf(item.quantity), 
					"cancelled", 
					"", 
					date };
			
			sb.append(formatter(entry));
			sb.append(System.lineSeparator());
		}

		sb.append(toAppend);

		write(PATH_LOG, sb.toString());
	}
}
