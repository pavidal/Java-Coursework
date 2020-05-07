public class Mouse extends Item{
    private int buttonCount;

    /**
     *
     * @param barcode - Device barcode ID
     * @param deviceType - Its category (standard, gaming, etc.)
     * @param brand - The device's manufacturer
     * @param colour - Primary colour of the device
     * @param connectivity - Its connectivity (wired or wireless)
     * @param stockQuantity - Amount of this item in stock
     * @param originalCost - Original cost of purchase
     * @param retailPrice - Advertised retail price
     * @param buttonCount - Number of mouse buttons
     */
    public Mouse(int barcode, String deviceType, String brand, String colour, String connectivity,
                    String stockQuantity, double originalCost, double retailPrice, int buttonCount) {

        super(barcode, deviceType, brand, colour, connectivity, stockQuantity, originalCost, retailPrice);
        this.buttonCount = buttonCount;
    }

    /**
     *
     * @param desc
     */
    public Mouse(String[] desc) {
        super(desc);
        this.buttonCount = Integer.parseInt(desc[8]);
    }
}
