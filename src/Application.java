import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Product.Item;
import Product.Keyboard;
import Product.Mouse;

public class Application extends JFrame {

	/**
	 * Don't know why it's needed
	 */
	private static final long serialVersionUID = 1L;
	private User currentUser;
	private Basket itemBasket = new Basket();

	private JPanel contentPane;
	private JLabel lblUsername;
	private JButton btnAddItem;
	private JButton btnBasket;
	private JTable table;

	private DefaultTableModel model = new DefaultTableModel() {
		private static final long serialVersionUID = 1L;

		@Override
		public boolean isCellEditable(int row, int column) {
			// Set all cells to be non-editable
			return false;
		}
	};
	private JButton btnAddToBasket;
	private JPanel panelBasket;
	private JLabel lblTotalCostNum;
	private JComboBox<String> comboColours;
	private JComboBox<String> comboBrands;
	private JPanel panelFilter;
	private JButton btnKeyboard;
	private JButton btnMice;
	private JButton btnCheckout;

	/**
	 * Set current user and permissions
	 * 
	 * @param user
	 */
	public void setUser(User user) {
		this.currentUser = user;
		lblUsername.setText(user.getName());

		// Enable only for admins
		btnAddItem.setVisible(user.getRole().equalsIgnoreCase("admin"));

		// Enable only for customers
		btnBasket.setVisible(!user.getRole().equalsIgnoreCase("admin"));
		btnAddToBasket.setVisible(!user.getRole().equalsIgnoreCase("admin"));

		// Defaults to keyboard view
		fillKeyboardTable();
	}

	/**
	 * Filter items in table
	 * 
	 * @param item - Item to filter
	 * @return Boolean, if Item matches filter or not.
	 */
	private boolean filter(Item item) {
		// Get filters
		String selectedColour = (String) comboColours.getSelectedItem();
		String selectedBrand = (String) comboBrands.getSelectedItem();

		// Conditions for filters
		boolean cond1 = selectedBrand.contains("any") && selectedColour.contains("any");
		boolean cond2 = selectedBrand.equalsIgnoreCase(item.getBrand()) && selectedColour.contains("any");
		boolean cond3 = selectedBrand.contains("any") && selectedColour.equalsIgnoreCase(item.getColour());
		boolean cond4 = selectedBrand.equalsIgnoreCase(item.getBrand())
				&& selectedColour.equalsIgnoreCase(item.getColour());

		return (cond1 || cond2 || cond3 || cond4);
	}

	/**
	 * Sets all filters to "any"
	 */
	private void resetFilter() {
		comboBrands.setSelectedIndex(0);
		comboColours.setSelectedIndex(0);
	}

	/**
	 * Remove all Items in this basket
	 */
	private void clearBasket() {
		itemBasket.clear();
		btnBasket.setText("Basket");
		fillKeyboardTable();
	}

	/**
	 * Fill tables with keyboards
	 */
	private void fillKeyboardTable() {
		try {
			List<Keyboard> keyboards = Database.getKeyboards();

			// Column headers
			String[] columns = null;

			// Permission to see original price
			boolean isAdmin = currentUser.getRole().equalsIgnoreCase("admin");
			if (isAdmin) {
				columns = new String[] { "Barcode", "Type", "Brand", "Colour", "Connectivity", "Stock Quantity",
						"Original Price (£)", "Retail Price (£)", "Layout" };

			} else {
				columns = new String[] { "Barcode", "Type", "Brand", "Colour", "Connectivity", "Stock Quantity",
						"Retail Price", "Layout" };
			}

			// Reset table model
			model.setColumnIdentifiers(columns);
			model.setRowCount(0);

			// Add rows to table model
			for (Keyboard keyboard : keyboards) {
				Object[] row = keyboard.getProperties(isAdmin).toArray();

				if (filter(keyboard)) {
					model.addRow(row);
				}
			}

			table.setModel(model);

			btnKeyboard.setEnabled(false);
			btnMice.setEnabled(true);

			panelBasket.setVisible(false);
			panelFilter.setVisible(true);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(table, "Unable to get list of products.\nPress OK to quit.", "Error",
					JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}

	/**
	 * Fills table with mice
	 */
	private void fillMouseTable() {
		try {
			List<Mouse> mice = Database.getMice();

			// Table headers
			String[] columns = null;

			// Permission to see original price
			boolean isAdmin = currentUser.getRole().equalsIgnoreCase("admin");
			if (isAdmin) {
				columns = new String[] { "Barcode", "Type", "Brand", "Colour", "Connectivity", "Stock Quantity",
						"Original Price (£)", "Retail Price (£)", "No. of Buttons" };

			} else {
				columns = new String[] { "Barcode", "Type", "Brand", "Colour", "Connectivity", "Stock Quantity",
						"Retail Price", "No. of Buttons" };
			}

			// Reset table model
			model.setColumnIdentifiers(columns);
			model.setRowCount(0);

			// Add rows to table model
			for (Mouse mouse : mice) {
				Object[] row = mouse.getProperties(isAdmin).toArray();

				if (filter(mouse)) {
					model.addRow(row);
				}
			}

			table.setModel(model);

			btnKeyboard.setEnabled(true);
			btnMice.setEnabled(false);

			panelBasket.setVisible(false);
			panelFilter.setVisible(true);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(table, "Unable to get list of products.\nPress OK to quit.", "Error",
					JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}

	/**
	 * Fills table with items in basket
	 */
	private void fillBasketTable() {
		/*
		 * When current basket is empty, ask if user wants to load a saved basket only
		 * the latest saved basket will be loaded
		 */
		if (itemBasket.isEmpty()) {
			int option = JOptionPane.showConfirmDialog(table,
					"Your basket is empty.\nWould you like to load a saved basket?", "Basket",
					JOptionPane.YES_NO_OPTION);

			if (option == JOptionPane.YES_OPTION) {
				try {
					// Tries to load basket from file
					Basket savedBasket = Database.getSavedBasket(currentUser);

					if (savedBasket.isEmpty()) {
						JOptionPane.showMessageDialog(table, "You haven't saved anything yet.");
						return;

					} else {
						itemBasket = savedBasket;
						btnBasket.setText("Basket ( " + itemBasket.getSize() + " )");
					}

				} catch (FileNotFoundException e) {
					e.printStackTrace();

					JOptionPane.showMessageDialog(table, "Unable to get your saved basket", "Error",
							JOptionPane.ERROR_MESSAGE);

					// Return to Keyboard view when failed.
					fillKeyboardTable();
					return;
				}
			} else {
				// Do nothing, exit from method.
				return;
			}
		}

		/*
		 * After basket is populated
		 */

		// Display total price of basket
		java.text.DecimalFormat dFormat = new java.text.DecimalFormat("#,###,##0.00");
		lblTotalCostNum.setText(dFormat.format(itemBasket.getTotalPrice()));

		// Table headers
		String[] columns = { "Barcode", "Category", "Type", "Brand", "Colour", "Quantity", "Price" };

		// Reset table model
		model.setColumnIdentifiers(columns);
		model.setRowCount(0);

		// Populate table
		List<Item> itemList = itemBasket.getItems();
		for (Item item : itemList) {

			// get all properties of item
			Object[] prop = item.getProperties(false).toArray();
			// Check the item category
			String category = (item instanceof Keyboard ? "keyboard" : "mouse");

			Object[] rowData = { prop[0], category, prop[1], prop[2], prop[3], item.quantity, prop[6] };
			model.addRow(rowData);
		}

		table.setModel(model);

		panelBasket.setVisible(true);
		panelFilter.setVisible(false);

		btnKeyboard.setEnabled(true);
		btnMice.setEnabled(true);
	}

	/**
	 * Get the currently selected item on the table
	 * 
	 * @return Currently selected Item
	 * @throws FileNotFoundException
	 * @throws IndexOutOfBoundsException
	 */
	private Item getSelectedItem() throws FileNotFoundException, IndexOutOfBoundsException {
		int selectedRow = table.getSelectedRow();

		// Get Item's barcode
		String barcode = model.getValueAt(selectedRow, 0).toString();

		// Return Item object from barcode
		return Database.scanBarcode(barcode);
	}

	/**
	 * Add the selected item to basket
	 */
	private void addToBasket() {
		try {
			// add item to basket
			itemBasket.addItem(getSelectedItem());

			// Show basket size on Basket button
			btnBasket.setText("Basket ( " + itemBasket.getSize() + " )");

			if (panelBasket.isVisible()) {
				// If in basket view (increase item quantity)
				// Update basket view
				fillBasketTable();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(table, "Unable to add to basket.", "Disk I/O Error",
					JOptionPane.ERROR_MESSAGE);

		} catch (IndexOutOfBoundsException e1) {
			// Nothing selected, do nothing.
			return;
		}
	}

	/**
	 * Remove the selected item from basket
	 */
	private void removeFromBasket() {
		try {
			// Remove from basket
			itemBasket.removeItem(getSelectedItem());

			if (itemBasket.getSize() > 0) {
				// Update basket view if not empty
				btnBasket.setText("Basket ( " + itemBasket.getSize() + " )");
				fillBasketTable();

			} else {
				// Exit from basket view if empty
				btnBasket.setText("Basket");
				fillKeyboardTable();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(table, "Unable to remove item.", "Disk I/O Error", JOptionPane.ERROR_MESSAGE);

		} catch (IndexOutOfBoundsException e1) {
			// Nothing selected, do nothing.
			return;
		}
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Application frame = new Application();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * 
	 * Lots of listeners here amongst auto-gen code Like a Java bag of mixed nuts ;)
	 */
	public Application() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 1024, 768);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		/*
		 * Confirm exiting application
		 */
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				String message = "Are you sure you want to quit?";

				// Add basket clear reminder for customers
				if (!currentUser.getRole().equalsIgnoreCase("admin")) {
					message += "\nAll unsaved items in the basket will be lost.";
				}

				int option = JOptionPane.showConfirmDialog(contentPane, message, "Quit", JOptionPane.YES_NO_OPTION);

				if (option == JOptionPane.YES_OPTION) {
					if (!itemBasket.isEmpty()) {
						try {
							// Clears & logs basket if not empty
							Database.logBasket(currentUser, itemBasket, "cancelled");
							clearBasket();

						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
							// Take the L if can't log when exiting.
						}
					}

					// Kill process
					System.exit(0);
				}
			}
		});

		JPanel panelNav = new JPanel();
		panelNav.setBounds(5, 5, 1008, 50);
		contentPane.add(panelNav);
		panelNav.setLayout(null);

		btnKeyboard = new JButton("Browse Keyboards");
		btnKeyboard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/*
				 * Switch to keyboard view
				 */
				resetFilter();
				fillKeyboardTable();

			}
		});
		btnKeyboard.setBounds(10, 11, 145, 28);
		panelNav.add(btnKeyboard);

		btnMice = new JButton("Browse Mice");
		btnMice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/*
				 * Switch to mouse view
				 */
				resetFilter();
				fillMouseTable();
			}
		});
		btnMice.setBounds(165, 11, 145, 28);
		panelNav.add(btnMice);

		/*
		 * Adding a new product
		 */
		btnAddItem = new JButton("Add Item");
		btnAddItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Create form for adding products
				ProductForm pForm = new ProductForm();
				pForm.setVisible(true);

				// Listen for when window is closing
				pForm.addWindowListener(new WindowAdapter() {
					/*
					 * When a window is about to close
					 */
					@Override
					public void windowClosing(WindowEvent e) {
						if (pForm.getItem() == null) {
							// when [cancel] or [x] is clicked
							// confirm cancellation

							int option = JOptionPane.showConfirmDialog(pForm,
									"Are you sure you want to cancel?\nThis item will be discarded.", "Discard Item",
									JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

							if (option == JOptionPane.YES_OPTION) {
								pForm.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
								pForm.dispatchEvent(new WindowEvent(pForm, WindowEvent.WINDOW_CLOSED));
							}
						} else {
							// when [ok] is pressed just close normally

							pForm.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
							pForm.dispatchEvent(new WindowEvent(pForm, WindowEvent.WINDOW_CLOSED));
						}
					}

					/*
					 * Triggered when window is closed (after above)
					 */
					@Override
					public void windowClosed(WindowEvent e) {
						String[] item = pForm.getItem();

						if (item != null) {
							// when [ok] is pressed

							// Append new item to file
							String line = Database.formatter(item);
							Database.append(Database.PATH_ITEM, line);

							// Refresh view
							if (btnKeyboard.isEnabled()) {
								fillKeyboardTable();
							} else {
								fillMouseTable();
							}

							// Prevent team rocket's double trouble
							pForm.removeWindowListener(this);
						}
					}
				});
			}
		});
		btnAddItem.setBounds(320, 11, 145, 28);
		panelNav.add(btnAddItem);

		lblUsername = new JLabel("USERNAME");
		lblUsername.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblUsername.setHorizontalAlignment(SwingConstants.TRAILING);
		lblUsername.setBounds(677, 11, 166, 28);
		panelNav.add(lblUsername);

		btnBasket = new JButton("Basket");
		btnBasket.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/*
				 * Show basket view
				 */
				fillBasketTable();
			}
		});
		btnBasket.setBounds(853, 11, 145, 28);
		panelNav.add(btnBasket);

		btnAddToBasket = new JButton("Add to Basket");
		btnAddToBasket.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/*
				 * Add selected item to basket
				 */
				addToBasket();
			}
		});
		btnAddToBasket.setBounds(475, 11, 145, 28);
		panelNav.add(btnAddToBasket);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(5, 128, 1008, 539);
		contentPane.add(scrollPane);

		table = new JTable();
		table.getTableHeader().setReorderingAllowed(false);	// Disable header reordering
		scrollPane.setViewportView(table);

		panelBasket = new JPanel();
		panelBasket.setBounds(5, 67, 1008, 50);
		contentPane.add(panelBasket);
		panelBasket.setLayout(null);
		panelBasket.setVisible(false);

		JButton btnClearBasket = new JButton("Clear Basket");
		btnClearBasket.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*
				 * Empty basket
				 */
				try {
					// Ask for confirmation
					int option = JOptionPane.showConfirmDialog(table, "Are you sure you want to clear the basket?",
							"Clear Basket", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

					if (option == JOptionPane.YES_OPTION) {
						// Log & empty basket
						Database.logBasket(currentUser, itemBasket, "cancelled");
						clearBasket();
					}

				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(table, "Unable to clear basket.", "I/O Error",
							JOptionPane.ERROR_MESSAGE);
				}

			}
		});
		btnClearBasket.setBounds(321, 11, 145, 28);
		panelBasket.add(btnClearBasket);

		btnCheckout = new JButton("Checkout");
		btnCheckout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/*
				 * Checkout basket
				 */

				// Check if items in the basket have enough stock for the order
				for (Item item : itemBasket.getItems()) {
					if (item.quantity > item.getStock()) {

						// Message to show
						String itemInfo = item.getColour() + " " + item.getBrand() + " "
								+ (item instanceof Keyboard ? "keyboard" : "mouse");

						String message = "The " + itemInfo
								+ " doesn't have the required amount in stock.\nYou're ordering " + item.quantity
								+ " and we have " + item.getStock() + " in stock.";

						JOptionPane.showMessageDialog(table, message);
						return;
					}
				}

				/*
				 * If able to checkout
				 */
				// Create payment form
				Checkout c = new Checkout();
				c.setVisible(true);

				// Listen passionately to the window
				c.addWindowListener(new WindowAdapter() {
					/*
					 * When window is about to close
					 */
					@Override
					public void windowClosing(WindowEvent e) {
						// If closed with [cancel] or [x]
						// Ask for confirmation to cancel payment
						if (c.getPaymentType() == null) {
							int option = JOptionPane.showConfirmDialog(c, "Are you sure you want to cancel payment?",
									"Cancel Payment", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

							if (option == JOptionPane.YES_OPTION) {
								c.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
								c.dispatchEvent(new WindowEvent(c, WindowEvent.WINDOW_CLOSED));
							}
						} else {
							// if closed with [Ok]
							// Close normally
							c.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
							c.dispatchEvent(new WindowEvent(c, WindowEvent.WINDOW_CLOSED));
						}
					}

					/*
					 * When the window is closed (after above)
					 */
					@Override
					public void windowClosed(WindowEvent e) {
						String paymentType = c.getPaymentType();

						if (paymentType != null) {
							// If [OK] is pressed
							try {
								// Log payment and subtract stock
								Database.logBasket(currentUser, itemBasket, "purchased", paymentType);
								Database.subtractStock(itemBasket);

								// Show confirmation message
								String message = "£ " + lblTotalCostNum.getText() + " paid using " + paymentType;
								JOptionPane.showMessageDialog(table, message);

								// empty basket
								clearBasket();
							} catch (FileNotFoundException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
								JOptionPane.showMessageDialog(table, "Unable to process your purchase.", "I/O Error",
										JOptionPane.ERROR_MESSAGE);
							} finally {
								// prevent double entry
								c.removeWindowListener(this);
							}
						}

					}
				});
			}
		});
		btnCheckout.setBounds(853, 11, 145, 28);
		panelBasket.add(btnCheckout);

		JButton btnIncQuantity = new JButton("+");
		btnIncQuantity.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnIncQuantity.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*
				 * Increment selected item's quantity
				 */
				addToBasket();
			}
		});
		btnIncQuantity.setBounds(10, 11, 70, 28);
		panelBasket.add(btnIncQuantity);

		JButton btnDecQuantity = new JButton("-");
		btnDecQuantity.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*
				 * Decrement selected item's quantity
				 */
				removeFromBasket();
			}
		});
		btnDecQuantity.setFont(new Font("Tahoma", Font.BOLD, 24));
		btnDecQuantity.setBounds(86, 11, 70, 28);
		panelBasket.add(btnDecQuantity);

		lblTotalCostNum = new JLabel("0.00");
		lblTotalCostNum.setFont(new Font("Segoe UI", Font.BOLD, 20));
		lblTotalCostNum.setHorizontalAlignment(SwingConstants.TRAILING);
		lblTotalCostNum.setBounds(719, 11, 124, 28);
		panelBasket.add(lblTotalCostNum);

		JLabel lblTotalCost = new JLabel("Total: £");
		lblTotalCost.setFont(new Font("Segoe UI", Font.BOLD, 16));
		lblTotalCost.setHorizontalAlignment(SwingConstants.TRAILING);
		lblTotalCost.setBounds(620, 11, 89, 28);
		panelBasket.add(lblTotalCost);

		JButton btnSaveBasket = new JButton("Save Basket");
		btnSaveBasket.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*
				 * Save the basket to log file
				 */
				try {
					if (!Database.getSavedBasket(currentUser).isEmpty()) {
						// If the user had saved the basket previously
						// Show this warning
						int option = JOptionPane.showConfirmDialog(table,
								"You have a basket saved previously.\nBaskets saved before today will be lost.\n\nContinue?",
								"Previous Save Found", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

						if (option == JOptionPane.NO_OPTION) {
							// Cancel operation
							return;
						}
					}

					// Log & clear basket
					Database.logBasket(currentUser, itemBasket, "saved");
					clearBasket();

					JOptionPane.showMessageDialog(table, "Basket saved!");
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(table, "Unable to save your basket.", "I/O Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnSaveBasket.setBounds(166, 11, 145, 28);
		panelBasket.add(btnSaveBasket);

		panelFilter = new JPanel();
		panelFilter.setBounds(5, 678, 1008, 50);
		contentPane.add(panelFilter);
		panelFilter.setLayout(null);

		JLabel lblNewLabel = new JLabel("Filter Products");
		lblNewLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
		lblNewLabel.setBounds(10, 11, 110, 28);
		panelFilter.add(lblNewLabel);

		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(130, 11, 10, 28);
		panelFilter.add(separator);

		JLabel lblFilterColour = new JLabel("Colours");
		lblFilterColour.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblFilterColour.setBounds(150, 11, 57, 28);
		panelFilter.add(lblFilterColour);

		comboColours = new JComboBox<String>();
		comboColours.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*
				 * Filter by colour
				 */
				// Refresh current view
				if (!btnKeyboard.isEnabled()) {
					fillKeyboardTable();
				} else {
					fillMouseTable();
				}
			}
		});
		lblFilterColour.setLabelFor(comboColours);
		String[] colourModel = { "Unavailable" };
		/*
		 * Get all product colours for filtering
		 */
		try {
			colourModel = Database.getFilters(4);
		} catch (FileNotFoundException e1) {
			// Disable filtering if database illiterate
			e1.printStackTrace();
			comboColours.setEnabled(false);
		}
		Arrays.sort(colourModel);
		comboColours.setModel(new DefaultComboBoxModel<String>(colourModel));
		comboColours.setBounds(217, 11, 110, 28);
		panelFilter.add(comboColours);

		JLabel lblFilterBrand = new JLabel("Brand");
		lblFilterBrand.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblFilterBrand.setBounds(369, 11, 57, 28);
		panelFilter.add(lblFilterBrand);

		comboBrands = new JComboBox<String>();
		comboBrands.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*
				 * Filter by brand
				 */
				// Refresh current view
				if (!btnKeyboard.isEnabled()) {
					fillKeyboardTable();
				} else {
					fillMouseTable();
				}
			}
		});
		lblFilterBrand.setLabelFor(comboBrands);
		String[] brandModel = { "Unavailable" };
		/*
		 * Get all brands for filtering
		 */
		try {
			brandModel = Database.getFilters(3);
		} catch (FileNotFoundException e1) {
			// Disable filtering if can't read
			e1.printStackTrace();
			comboBrands.setEnabled(false);
		}
		Arrays.sort(brandModel);
		comboBrands.setModel(new DefaultComboBoxModel<String>(brandModel));
		comboBrands.setBounds(436, 11, 110, 28);
		panelFilter.add(comboBrands);

		JButton btnResetFilter = new JButton("Reset");
		btnResetFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/*
				 * Reset filters to "any"
				 */
				resetFilter();
			}
		});
		btnResetFilter.setBounds(873, 11, 125, 28);
		panelFilter.add(btnResetFilter);
	}
}
