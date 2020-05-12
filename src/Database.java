
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import Product.Item;
import Product.Keyboard;
import Product.Mouse;

public class Database {
	// Paths to files
	public static final String PATH_LOG = "Data/ActivityLog.txt";
	public static final String PATH_USER = "Data/UserAccounts.txt";
	public static final String PATH_ITEM = "Data/Stock.txt";

	/*
	 * I've decided to make this a non-OO class as I don't see a benefit
	 * of making a class that accesses and modifies files object oriented.
	 * 
	 * I'd rather have this as a collection of tools instead.
	 * 
	 * If I were to have this as an OO class, I would have to make it a global
	 * object in other classes (which defeats the purpose as it's the same as 
	 * a non-OO class) or having to create an instance every time I want to 
	 * perform I/O operations. As for each method, I only use this class once
	 * or twice each time it's called which I think is inefficient.
	 * 
	 * Thank you for reading my blog.
	 */
	
	
	/**
	 * Returns a list of rows from a delimited text file
	 * 
	 * @param path - [String] Path to file
	 * @return table - [ArrayList<String[]>] contents of file in 2D arraylist
	 * @throws FileNotFoundException
	 */
	public static ArrayList<String[]> read(String path) throws FileNotFoundException {
		ArrayList<String[]> table = new ArrayList<String[]>();

		// Putting scanner in try so sc.close() is not needed
		try (Scanner sc = new Scanner(new File(path))) {

			while (sc.hasNextLine()) {

				// Splits each line into fields
				String line = sc.nextLine();
				String[] row = line.split(", ");

				// prevents empty lines from getting into list
				if (!line.isEmpty())
					table.add(row);
			}
		}

		// This returns an arraylist but I ended up not using
		// as it's more cumbersome that I thought
		// In hindsight 2020 I should have used a 2D list
		// instead of this hybrid
		return table;
	}

	/**
	 * Converts 2D ArrayList from read() to 2D array
	 * 
	 * @param arrList - [ArrayList<String[]>] File contents from read()
	 * @return File contents in 2D array
	 */
	public static String[][] toArray(ArrayList<String[]> arrList) {

		// This converts the arraylist from above into a
		// 2D array which I found it easier to use.
		// (even though they're probably the same)
		return arrList.toArray(new String[0][0]);
	}

	/**
	 * Appends to bottom of file
	 * 
	 * @param path - path to file
	 * @param line - line String to append
	 */
	public static void append(String path, String line) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File(path), true))) {

			// assumes that there will be no newline char at EOF
			// and inserts it.
			// I've compensated the possibility of an empty line
			// read()
			bw.newLine();
			bw.write(line);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Overwrites a file
	 * 
	 * @param path     - path to file
	 * @param contents - contents of file
	 */
	public static void write(String path, String contents) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File(path), false))) {
			bw.write(contents);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Format Array to a delimited String
	 * 
	 * @param list      - String array
	 * @param delimiter - Separator for each element
	 * @return A delimited String with the chosen delimiter
	 */
	public static String formatter(String[] list, String delimiter) {
		// Using StringBuilder for efficiency
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < list.length; i++) {

			// Check position in array
			if (i == list.length - 1) {

				// Don't append delimiter to the last element
				sb.append(list[i]);
			} else {

				// otherwise append
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

		// This acts as a shortcut
		return formatter(list, ", ");
	}

	/**
	 * Format 2D array to comma and newline delimited String
	 * 
	 * @param list - A 2D array of (possibly modified) file contents
	 * @return Delimited String for writing to file
	 */
	public static String formatter(String[][] list) {
		StringBuilder sb = new StringBuilder();

		// Works similarly with formatter (1D)
		for (int i = 0; i < list.length; i++) {
			if (list[i][0] == null) {

				// Skip if marked, see subtractStock().
				// Quite an ugly solution TBH but ArrayList
				// didn't work out.
				continue;
			} else if (i == list.length - 1) {

				// Don't append newline to last line
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
		// Reads from DB
		String[][] allUsers = toArray(read(PATH_USER));
		
		ArrayList<User> userList = new ArrayList<User>();

		// Building a list of users using User class
		for (String[] user : allUsers) {
			userList.add(new User(user));
		}

		return userList;
	}

	/**
	 * Get a list of all keyboards from stock list
	 * @return A List of all keyboards (Keyboard objects)
	 * @throws FileNotFoundException
	 */
	public static List<Keyboard> getKeyboards() throws FileNotFoundException {
		// Gets all items from file
		String[][] keyboardList = toArray(read(PATH_ITEM));
		
		List<Keyboard> itemList = new ArrayList<Keyboard>();

		// Builds a list of keyboards
		for (String[] keyboard : keyboardList) {
			if (keyboard[1].equalsIgnoreCase("keyboard")) {
				itemList.add(new Keyboard(keyboard));
			}
		}

		// Sorts by descending stock quantity
		Collections.sort(itemList);
		
		return itemList;
	}

	/**
	 * Gets a list of mice from stock list
	 * @return A List of all mice (Mouse objects)
	 * @throws FileNotFoundException
	 */
	public static List<Mouse> getMice() throws FileNotFoundException {
		// Gets all items from file
		String[][] miceList = toArray(read(PATH_ITEM));
		
		List<Mouse> itemList = new ArrayList<Mouse>();

		// Builds a list of mice
		for (String[] mouse : miceList) {
			if (mouse[1].equalsIgnoreCase("mouse")) {
				itemList.add(new Mouse(mouse));
			}
		}
		
		// Sorts by descending stock quantity
		Collections.sort(itemList);
		
		return itemList;
		
		// In hindsight, I could have combined this with getKeyboards()
		// and return using Item list
		// OR get all items in one big list of Item and filter later
		// I'm too far into this now and there's not enough time to refactor
	}

	/**
	 * Gets the latest basket saved by the user
	 * @param user - Current user as an object
	 * @return A Basket object of Items saved
	 * @throws FileNotFoundException
	 */
	public static Basket getSavedBasket(User user) throws FileNotFoundException {
		// Get all activities from log file
		String[][] logs = toArray(read(PATH_LOG));
		
		List<Item> itemList = new ArrayList<Item>();

		// User ID for filtering
		String id = user.getID();
		// For recording date of latest saved basket
		String newestDate = "";

		// Building a basket
		for (String[] entry : logs) {
			
			// Filtering activity by "saved" and User ID
			if (entry[0].equals(id) && entry[5].equalsIgnoreCase("saved")) {
				
				// Checks if date matches previous log entry.
				// If the previous date is empty, ignore.
				if (!newestDate.isEmpty() && !entry[7].contentEquals(newestDate)) {
					
					// Exit loop when condition is met as activity log
					// is in reverse chronological order
					break;
				} else {
					
					// Gets ID of the item and "Scans the barcode" to get the associated Item object
					Item item = scanBarcode(entry[2]);
					// Sets quantity of item to what was saved
					item.quantity = Integer.parseInt(entry[4]);
					itemList.add(item);
					
					// Sets date to current entry
					newestDate = entry[7];
				}
			}
		}

		// Return as a basket object
		return new Basket(itemList);
		
		// There is a flaw with this method.
		// If the user saved a basket twice on the same day, it will append.
		// Unfortunately, there's no way to prevent this other than having
		// field to assign a common "Basket ID" or preventing the user from 
		// saving the basket twice.
	}

	/**
	 * Takes a barcode and returns an item associated with it
	 * @param barcode - Barcode of item
	 * @return An Item object associated with that barcode
	 * @throws FileNotFoundException
	 */
	public static Item scanBarcode(String barcode) throws FileNotFoundException {
		String[][] stock = toArray(read(PATH_ITEM));

		// Searching through stock for matching barcode
		for (String[] entry : stock) {
			if (entry[0].equals(barcode)) {
				if (entry[1].equalsIgnoreCase("keyboard")) {
					return new Keyboard(entry);
				} else if (entry[1].equalsIgnoreCase("mouse")) {
					return new Mouse(entry);
				}
			}
		}

		// I know that there's a null return and so I've verified
		// inputs before calling this (ensuring that null won't be returned)
		return null;
		
		// This is what getKeyboards and getMice should've been like.
		// But returning an Item List instead.
	}

	/**
	 * Checks if a barcode already exists
	 * @param barcode - Barcode of an item
	 * @return A boolean, true = free, false = exists.
	 * @throws FileNotFoundException
	 */
	public static boolean checkBarcode(String barcode) throws FileNotFoundException {
		String[][] stock = toArray(read(PATH_ITEM));

		for (String[] item : stock) {
			if (item[0].equals(barcode)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Logs a basket activity
	 * @param user - User responsible for this action
	 * @param basket - Current basket session of this user
	 * @param type - Type of basket activity (cancelled, saved, or purchased)
	 * @param paymentType - Type of payment if purchased
	 * @throws FileNotFoundException
	 */
	public static void logBasket(User user, Basket basket, String type, String paymentType)
			throws FileNotFoundException {
		
		// Get items from basket
		List<Item> itemList = basket.getItems();
		
		// Lines to append to top of file
		StringBuilder sb = new StringBuilder();
		
		// Rest of the activity file
		// Pre-formatted to append after
		String toAppend = formatter(toArray(read(PATH_LOG)));

		// Getting today's date and format it
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String date = dateFormat.format(Calendar.getInstance().getTime());

		// Turning each Item in list to a line to append
		for (Item item : itemList) {

			String[] entry = { user.getID(), user.getPostcode().toUpperCase(), String.valueOf(item.getBarcode()),
					String.valueOf(item.getSingularPrice()), String.valueOf(item.quantity), type, paymentType, date };

			// Turn array into comma delimited string
			sb.append(formatter(entry));
			sb.append(System.lineSeparator());
		}

		// append the rest of the file
		sb.append(toAppend);

		// overwrite log file with new information
		write(PATH_LOG, sb.toString());
	}

	/**
	 * Logs a basket activity. Without payment.
	 * @param user - User responsible for this activity
	 * @param basket - Current Basket session of this User
	 * @param type - Type of activity (cancelled or saved)
	 * @throws FileNotFoundException
	 */
	public static void logBasket(User user, Basket basket, String type) throws FileNotFoundException {
		logBasket(user, basket, type, "");
	}

	/**
	 * Get columns for filtering
	 * @param field - Index of column
	 * @return An array for use with combo boxes or whatever
	 * @throws FileNotFoundException
	 */
	public static String[] getFilters(int field) throws FileNotFoundException { 
		List<String> filterList = new ArrayList<String>();
		String[][] productArray = toArray(read(PATH_ITEM));

		for (String[] product : productArray) {
			if (!filterList.contains(product[field])) {
				filterList.add(product[field]);
			}
		}

		// This is to remove filters
		// A dot is added so it's on top
		filterList.add(".any");

		Collections.sort(filterList);
		
		// Returns as an array to use with comboboxes
		return filterList.toArray(new String[0]);
	}

	/**
	 * Subtracts stock quantity of database with the basket
	 * @param basket - Basket of purchased Items
	 * @throws FileNotFoundException
	 */
	public static void subtractStock(Basket basket) throws FileNotFoundException {
		List<Item> itemList = basket.getItems();
		String[][] inventory = toArray(read(PATH_ITEM));

		for (String[] row : inventory) {
			for (Item item : itemList) {
				
				// Subtracts stock of matching Items
				if (String.valueOf(item.getBarcode()).equals(row[0])) {
					int stockLeft = Integer.parseInt(row[6]) - item.quantity;

					// remove item if there's no stock left
					if (stockLeft == 0) {
						// Since replacing the row with a null doesn't work
						// somehow, I've decided to replace the first element
						// with a null instead.
						// I know it's awful but using an arraylist doesn't solve
						// this as it throws an exception when attempting
						// to remove the row.
						row[0] = null;
					} else {
						
						// otherwise modify value
						row[6] = String.valueOf(stockLeft);
					}
				}
			}
		}

		// Overwrite file with new information
		String toWrite = formatter(inventory);
		write(PATH_ITEM, toWrite);
	}
}
