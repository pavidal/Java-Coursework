package Product;

import java.util.ArrayList;
import java.util.List;

public class Keyboard extends Item {

	private String layout;

	/**
	 * Create a new Keyboard.
	 *
	 * @param barcode       - Device barcode ID
	 * @param deviceType    - Its category (standard, gaming, etc.)
	 * @param brand         - The device's manufacturer
	 * @param colour        - Primary colour of the device
	 * @param connectivity  - Its connectivity (wired or wireless)
	 * @param stockQuantity - Amount of this item in stock
	 * @param originalCost  - Original cost of purchase
	 * @param retailPrice   - Advertised retail price
	 * @param layout        - Keyboard layout
	 */
	public Keyboard(int barcode, String deviceType, String brand, String colour, String connectivity, int stockQuantity,
			double originalCost, double retailPrice, String layout) {

		super(barcode, deviceType, brand, colour, connectivity, stockQuantity, originalCost, retailPrice);
		this.layout = layout;
	}

	/**
	 * Create a new Keyboard.
	 *
	 * @param desc - Array of the item's description
	 */
	public Keyboard(String[] desc) {
		super(desc);
		this.layout = desc[9];
	}

	/**
	 * Gets all properties of this Keyboard. This includes its layout.
	 * 
	 * @param getOGCost - Include original cost
	 * @return List of properties
	 */
	public List<Object> getProperties(boolean getOGCost) {
		List<Object> superList = new ArrayList<Object>();
		superList.addAll(super.getProperties(getOGCost));
		superList.add(layout);

		return superList;
	}
}
