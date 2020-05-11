package Product;

import java.util.Arrays;
import java.util.List;

public class Item implements Comparable<Item>{

    private int barcode;
    private String deviceType;
    private String brand;
    private String colour;
    private String connectivity;
    private int stockQuantity;
    private double originalCost;
    private double retailPrice;
    
    // Quantity for basket
    public int quantity = 1;

    /**
     * This is a parent class for Keyboard and Mouse.
     * @param barcode - Device barcode ID
     * @param deviceType - Its category (standard, gaming, etc.)
     * @param brand - The device's manufacturer
     * @param colour - Primary colour of the device
     * @param connectivity - Its connectivity (wired or wireless)
     * @param stockQuantity - Amount of this item in stock
     * @param originalCost - Original cost of purchase
     * @param retailPrice - Advertised retail price
     */

    public Item(int barcode, String deviceType, String brand,
                String colour, String connectivity, int stockQuantity,
                double originalCost, double retailPrice) {

        this.barcode = barcode;
        this.deviceType = deviceType;
        this.brand = brand;
        this.colour = colour;
        this.connectivity = connectivity;
        this.stockQuantity = stockQuantity;
        this.originalCost = originalCost;
        this.retailPrice = retailPrice;

    }

    /**
     * This is a parent class of Keyboard and Mouse.
     * Takes the entire row as input
     * @param desc - Array of the item's description
     */
    public Item(String[] desc) {
        this(Integer.parseInt(desc[0]),
                desc[2], desc[3], desc[4], desc[5], Integer.parseInt(desc[6]),
                Double.parseDouble(desc[7]), Double.parseDouble(desc[8]));
    }
    
    public int getBarcode() {
    	return this.barcode;
    }
    
    public double getPrice() {
    	return (this.retailPrice * this.quantity);
    }
    
    public String getColour() {
		return this.colour;
	}
    
    public String getBrand() {
    	return this.brand;
    }
    
    public double getSingularPrice() {
    	return this.retailPrice;
    }
    
    public int getStock() {
    	return this.stockQuantity;
    }
    
    public List<Object> getProperties(boolean getOGCost) {
    	Object[] properties = null;
    	if (getOGCost) {
    		properties = new Object[] {
        			this.barcode,
        			this.deviceType,
        			this.brand,
        			this.colour,
        			this.connectivity,
        			this.stockQuantity,
        			this.originalCost,
        			this.retailPrice,
        	};
    	} else {
    		properties = new Object[] {
        			this.barcode,
        			this.deviceType,
        			this.brand,
        			this.colour,
        			this.connectivity,
        			this.stockQuantity,
        			this.retailPrice,
        	};
		}
    	
    	return Arrays.asList(properties);
    }

	@Override
	public int compareTo(Item o) {
		return (o.stockQuantity - this.stockQuantity);
	}
}
