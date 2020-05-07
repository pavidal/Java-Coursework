public class Item {

    private int barcode;
    private String deviceType;
    private String brand;
    private String colour;
    private String connectivity;
    private String stockQuantity;
    private double originalCost;
    private double retailPrice;

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
                String colour, String connectivity, String stockQuantity,
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
     * @param desc - Array of the item's description
     */
    public Item(String[] desc) {
        this(Integer.parseInt(desc[0]),
                desc[1], desc[2], desc[3], desc[4], desc[5],
                Double.parseDouble(desc[6]), Double.parseDouble(desc[7]));
    }
}
