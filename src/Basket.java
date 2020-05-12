import java.util.ArrayList;
import java.util.List;
import Product.Item;

public class Basket {
	// List of Items in basket
	private List<Item> itemBasket = new ArrayList<Item>();

	/**
	 * Create empty basket
	 */
	public Basket() {

	}

	/**
	 * Import basket
	 * 
	 * @param items - List of Items
	 */
	public Basket(List<Item> items) {
		this.itemBasket = items;
	}

	/**
	 * Adds item to basket
	 * 
	 * @param item - Item to add
	 */
	public void addItem(Item item) {
		// Search if the item already exists
		int index = findIndex(item);

		if (index >= 0) {
			// Increment quantity of existing items
			Item existedItem = itemBasket.get(index);

			existedItem.quantity += 1;
			itemBasket.set(index, existedItem);
		} else {
			// add as new Item
			itemBasket.add(item);
		}
	}

	/**
	 * Search for an item in basket
	 * 
	 * @param item - Item to search
	 * @return Index of Item in Basket
	 */
	private int findIndex(Item item) {
		for (Item existedItem : itemBasket) {
			if (existedItem.getBarcode() == item.getBarcode()) {
				return itemBasket.indexOf(existedItem);
			}
		}

		// If not found
		return -1;
	}

	/**
	 * Removes an Item from Basket by decrementing quantity
	 * 
	 * @param item - Item object to remove
	 */
	public void removeItem(Item item) {
		int index = findIndex(item);
		Item existedItem = itemBasket.get(index);

		if (existedItem.quantity > 1) {
			// Decrement quantity if more than one exists
			existedItem.quantity -= 1;
		} else {
			// remove Item if one left
			itemBasket.remove(index);
		}

		// No need to check as removal can only happen by selection
		// and input is validated
	}

	/**
	 * Clears basket, removing all items.
	 */
	public void clear() {
		itemBasket.clear();
	}

	/**
	 * Get the number of unique Items in Basket
	 * 
	 * @return Number of unique Items (AKA size of list)
	 */
	public int getSize() {
		return itemBasket.size();
	}

	/**
	 * Checks if list is empty
	 * 
	 * @return boolean matching condition
	 */
	public boolean isEmpty() {
		return (getSize() == 0);
	}

	/**
	 * Get all Items in Basket
	 * 
	 * @return List of Items in Basket
	 */
	public List<Item> getItems() {
		return itemBasket;
	}

	/**
	 * Get price of all Items
	 * 
	 * @return Price of items
	 */
	public double getTotalPrice() {
		double total = 0.0;
		for (Item item : itemBasket) {
			total += item.getPrice();
		}
		return total;
	}
}
