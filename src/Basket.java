import java.util.ArrayList;
import java.util.List;
import Product.Item;

public class Basket {
	private List<Item> itemBasket = new ArrayList<Item>();
	
	/**
	 * Create empty basket
	 */
	public Basket() {
		
	}
	
	/**
	 * Import basket
	 * @param items - list of items
	 */
	public Basket(List<Item> items) {
		this.itemBasket = items;
	}
	
	public void addItem(Item item) {
		int index = findIndex(item);
		if (index >= 0) {
			// Increment quantity of existing items
			Item existedItem = itemBasket.get(index);
			
			existedItem.quantity += 1;
			itemBasket.set(index, existedItem);
		} else {
			itemBasket.add(item);
		}
	}
	
	private int findIndex(Item item) {
		for (Item existedItem : itemBasket) {
			if (existedItem.getBarcode() == item.getBarcode()) {
				return itemBasket.indexOf(existedItem);
			}
		}
		return -1;
	}
	
	public void removeItem(Item item) {
		int index = findIndex(item);
		Item existedItem = itemBasket.get(index);
		
		if (existedItem.quantity > 1) {
			existedItem.quantity -= 1;
		} else {
			itemBasket.remove(index);
		}
	}
	
	public void clear() {
		itemBasket.clear();
	}
	
	public int getSize() {
		return itemBasket.size();
	}
	
	public boolean isEmpty() {
		return (getSize() == 0);
	}
	
	public List<Item>getItems() {
		return itemBasket;
	}
	
	public double getTotalPrice() {
		double total = 0.0;
		for (Item item : itemBasket) {
			total += item.getPrice();
		}
		return total;
	}
}
