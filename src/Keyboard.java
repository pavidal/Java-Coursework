public class Keyboard extends Item{

    private String layout;

    /**
     * This is a parent class for Keyboard and Mouse.
     *
     * @param barcode       - Device barcode ID
     * @param deviceType    - Its category (standard, gaming, etc.)
     * @param brand         - The device's manufacturer
     * @param colour        - Primary colour of the device
     * @param connectivity  - Its connectivity (wired or wireless)
     * @param stockQuantity - Amount of this item in stock
     * @param originalCost  - Original cost of purchase
     * @param retailPrice   - Advertised retail price
     */
    public Keyboard(int barcode, String deviceType, String brand, String colour, String connectivity,
                    String stockQuantity, double originalCost, double retailPrice, String layout) {

        super(barcode, deviceType, brand, colour, connectivity, stockQuantity, originalCost, retailPrice);
        this.layout = layout;
    }

    /**
     * This is a parent class of Keyboard and Mouse.
     *
     * @param desc - Array of the item's description
     */
    public Keyboard(String[] desc) {
        super(desc);
        this.layout = desc[8];
    }

    public String[] getDesc() {
        return new String[] {};
    }
}
