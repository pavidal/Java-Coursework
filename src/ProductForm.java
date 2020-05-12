import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class ProductForm extends JDialog {

	/**
	 * Don't know why this is required.
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField fieldBarcode;
	private JTextField fieldType;
	private JTextField fieldBrand;
	private JTextField fieldColour;
	private JTextField fieldStock;
	private JTextField fieldRetailPrice;
	private JTextField fieldAdditionalInfo;
	private JTextField fieldOGCost;
	private JComboBox<String> comboCategory;
	private JComboBox<String> comboConnectivity;
	private JLabel lblAdditionalInfo;
	private JLabel lblType;
	private JButton okButton;

	private boolean btnOkPressed = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ProductForm dialog = new ProductForm();
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get form information
	 * 
	 * @return Form information
	 */
	public String[] getItem() {
		// No point using Item class as it will be written straight to DB

		if (btnOkPressed) {

			// Getting form data
			String barcode = fieldBarcode.getText();
			String category = ((String) comboCategory.getSelectedItem()).toLowerCase();
			String type = fieldType.getText().toLowerCase();
			String brand = fieldBrand.getText();
			String colour = fieldColour.getText().toLowerCase();
			String connectivity = (String) comboConnectivity.getSelectedItem();
			String stock = fieldStock.getText();
			String retailPrice = fieldRetailPrice.getText();
			String ogPrice = fieldOGCost.getText();
			String additionalInfo = fieldAdditionalInfo.getText().toUpperCase();

			// Formatting for consistency
			DecimalFormat dFormat = new DecimalFormat("#0.00");
			dFormat.setRoundingMode(RoundingMode.DOWN); // Truncate values

			double retailDouble = Double.parseDouble(retailPrice);
			double ogDouble = Double.parseDouble(ogPrice);

			retailPrice = dFormat.format(retailDouble);
			ogPrice = dFormat.format(ogDouble);

			String[] properties = { barcode, category, type, brand, colour, connectivity, stock, ogPrice, retailPrice,
					additionalInfo };

			return properties;
		}

		return null;
	}

	/**
	 * Validation of inputs
	 */
	private void validateFields() {

		// Gathering inputs
		String barcode = fieldBarcode.getText();
		String category = (String) comboCategory.getSelectedItem();
		String type = fieldType.getText();
		String brand = fieldBrand.getText();
		String colour = fieldColour.getText();
		String stock = fieldStock.getText();
		String retailPrice = fieldRetailPrice.getText();
		String ogPrice = fieldOGCost.getText();
		String additionalInfo = fieldAdditionalInfo.getText();

		String[] properties = { barcode, category, type, brand, colour, stock, ogPrice, retailPrice, additionalInfo };

		boolean isValid = true; // assume valid. If any aren't, it's turned false.
		boolean isKeyboard = (category.equals("Keyboard"));

		// Validate numerical inputs
		try {
			// For mouse buttons
			if (!isKeyboard) {
				int mouseBtns = Integer.parseInt(additionalInfo);
				isValid = isValid && (mouseBtns > 0);
			}

			int barcodeInt = Integer.parseInt(barcode);
			int stockInt = Integer.parseInt(stock);
			double retailDouble = Double.parseDouble(retailPrice);
			double ogDouble = Double.parseDouble(ogPrice);

			// If any are false, isValid is false.
			isValid = isValid && (barcodeInt > 0) && (stockInt > 0) && (retailDouble >= 0.0) && (ogDouble > 0.0);
		} catch (Exception e) {
			// Fail if any are not numeric
			isValid = false;
		}

		// Check if all fields are filled and sanitised
		for (String field : properties) {
			if (field.isEmpty() || field.contains(",")) {
				isValid = false;
				break;
			}
		}

		// other conditions to meet
		try {
			isValid = isValid && (barcode.length() == 6) // barcode 6 digits
					&& (isKeyboard ? (additionalInfo.length() == 2) : true) // 2 letter locale
					&& Database.checkBarcode(barcode); // Check if barcode already exists

		} catch (FileNotFoundException e) {
			isValid = false;
			e.printStackTrace();
		}

		// enable button if all conditions are met
		// otherwise, disabled.
		okButton.setEnabled(isValid);
	}

	/**
	 * Create the dialog.
	 */
	public ProductForm() {
		setAlwaysOnTop(true);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 480, 640);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblTitle = new JLabel("Add a New Product");
			lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
			lblTitle.setBounds(10, 11, 454, 50);
			contentPanel.add(lblTitle);
		}
		{
			JLabel lblProductType = new JLabel("Product Type");
			lblProductType.setFont(new Font("Segoe UI", Font.BOLD, 16));
			lblProductType.setBounds(10, 90, 454, 30);
			contentPanel.add(lblProductType);
		}

		comboCategory = new JComboBox<String>();
		comboCategory.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				/*
				 * Product category switched
				 * 
				 * Change labels of product type and additional information
				 */
				String category = (String) comboCategory.getSelectedItem();

				lblType.setText(category + " Type");

				String lblString = (category.equals("Keyboard") ? "Keyboard Layout" : "No. of Buttons");
				lblAdditionalInfo.setText(lblString);
			}
		});
		comboCategory.setModel(new DefaultComboBoxModel<String>(new String[] { "Keyboard", "Mouse" }));
		comboCategory.setBounds(10, 131, 228, 30);
		contentPanel.add(comboCategory);

		JSeparator separator = new JSeparator();
		separator.setBounds(10, 172, 454, 10);
		contentPanel.add(separator);

		JLabel lblBarcode = new JLabel("Barcode");
		lblBarcode.setHorizontalAlignment(SwingConstants.TRAILING);
		lblBarcode.setBounds(10, 193, 154, 30);
		contentPanel.add(lblBarcode);

		fieldBarcode = new JTextField();
		lblBarcode.setLabelFor(fieldBarcode);
		fieldBarcode.setBounds(174, 193, 290, 30);
		contentPanel.add(fieldBarcode);
		fieldBarcode.setColumns(10);

		lblType = new JLabel("Keyboard Type");
		lblType.setHorizontalAlignment(SwingConstants.TRAILING);
		lblType.setBounds(10, 234, 154, 30);
		contentPanel.add(lblType);

		fieldType = new JTextField();
		lblType.setLabelFor(fieldType);
		fieldType.setColumns(10);
		fieldType.setBounds(174, 234, 290, 30);
		contentPanel.add(fieldType);

		JLabel lblBrand = new JLabel("Brand (Case Sensitive)");
		lblBrand.setHorizontalAlignment(SwingConstants.TRAILING);
		lblBrand.setBounds(10, 275, 154, 30);
		contentPanel.add(lblBrand);

		fieldBrand = new JTextField();
		lblBrand.setLabelFor(fieldBrand);
		fieldBrand.setColumns(10);
		fieldBrand.setBounds(174, 275, 290, 30);
		contentPanel.add(fieldBrand);

		JLabel lblColour = new JLabel("Colour");
		lblColour.setHorizontalAlignment(SwingConstants.TRAILING);
		lblColour.setBounds(10, 316, 154, 30);
		contentPanel.add(lblColour);

		fieldColour = new JTextField();
		lblColour.setLabelFor(fieldColour);
		fieldColour.setColumns(10);
		fieldColour.setBounds(174, 316, 290, 30);
		contentPanel.add(fieldColour);

		JLabel lblConnectivity = new JLabel("Connectivity");
		lblConnectivity.setHorizontalAlignment(SwingConstants.TRAILING);
		lblConnectivity.setBounds(10, 357, 154, 30);
		contentPanel.add(lblConnectivity);

		comboConnectivity = new JComboBox<String>();
		lblConnectivity.setLabelFor(comboConnectivity);
		comboConnectivity.setModel(new DefaultComboBoxModel<String>(new String[] { "wired", "wireless" }));
		comboConnectivity.setBounds(174, 357, 290, 30);
		contentPanel.add(comboConnectivity);

		JLabel lblItemsInStock = new JLabel("Items in Stock");
		lblItemsInStock.setHorizontalAlignment(SwingConstants.TRAILING);
		lblItemsInStock.setBounds(10, 398, 154, 30);
		contentPanel.add(lblItemsInStock);

		fieldStock = new JTextField();
		lblItemsInStock.setLabelFor(fieldStock);
		fieldStock.setColumns(10);
		fieldStock.setBounds(174, 398, 290, 30);
		contentPanel.add(fieldStock);

		JLabel lblOriginalCost = new JLabel("Original Cost (£)");
		lblOriginalCost.setHorizontalAlignment(SwingConstants.TRAILING);
		lblOriginalCost.setBounds(10, 439, 154, 30);
		contentPanel.add(lblOriginalCost);

		fieldOGCost = new JTextField();
		lblOriginalCost.setLabelFor(fieldOGCost);
		fieldOGCost.setColumns(10);
		fieldOGCost.setBounds(174, 439, 290, 30);
		contentPanel.add(fieldOGCost);

		JLabel lblRetailPrice = new JLabel("Retail Price (£)");
		lblRetailPrice.setHorizontalAlignment(SwingConstants.TRAILING);
		lblRetailPrice.setBounds(10, 480, 154, 30);
		contentPanel.add(lblRetailPrice);

		fieldRetailPrice = new JTextField();
		lblRetailPrice.setLabelFor(fieldRetailPrice);
		fieldRetailPrice.setColumns(10);
		fieldRetailPrice.setBounds(174, 480, 290, 30);
		contentPanel.add(fieldRetailPrice);

		lblAdditionalInfo = new JLabel("Keyboard Layout");
		lblAdditionalInfo.setHorizontalAlignment(SwingConstants.TRAILING);
		lblAdditionalInfo.setBounds(10, 521, 154, 30);
		contentPanel.add(lblAdditionalInfo);

		fieldAdditionalInfo = new JTextField();
		lblAdditionalInfo.setLabelFor(fieldAdditionalInfo);
		fieldAdditionalInfo.setColumns(10);
		fieldAdditionalInfo.setBounds(174, 521, 290, 30);
		contentPanel.add(fieldAdditionalInfo);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						/*
						 * Ok button pressed
						 */
						Window thisWindow = Window.getWindows()[0];

						btnOkPressed = true;

						dispatchEvent(new WindowEvent(thisWindow, WindowEvent.WINDOW_CLOSING));
					}
				});
				okButton.setEnabled(false);
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						/*
						 * Cancel button pressed
						 */
						Window thisWindow = Window.getWindows()[0];

						btnOkPressed = false;

						dispatchEvent(new WindowEvent(thisWindow, WindowEvent.WINDOW_CLOSING));
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}

		/*
		 * Adding document listener to all text fields
		 */
		JTextField[] fieldList = { fieldBarcode, fieldType, fieldBrand, fieldColour, fieldStock, fieldRetailPrice,
				fieldAdditionalInfo, fieldOGCost };

		for (JTextField field : fieldList) {
			field.getDocument().addDocumentListener(new DocumentListener() {

				@Override
				public void removeUpdate(DocumentEvent e) {
					validateFields();

				}

				@Override
				public void insertUpdate(DocumentEvent e) {
					validateFields();

				}

				@Override
				public void changedUpdate(DocumentEvent e) {
					validateFields();

				}
			});
		}
	}
}
