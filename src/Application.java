import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import Product.Item;
import Product.Keyboard;
import Product.Mouse;

import javax.swing.JScrollPane;

public class Application extends JFrame {

	private User currentUser;
	private Basket itemBasket = new Basket();

	private JPanel contentPane;
	private JLabel lblUsername;
	private JButton btnAddItem;
	private JButton btnBasket;
	private JTable table;

	private DefaultTableModel model = new DefaultTableModel();
	private JButton btnAddToBasket;
	private JPanel panelBasket;
	private JLabel lblTotalCostNum;

	public void setUser(User user) {
		this.currentUser = user;
		lblUsername.setText(user.getName());
		btnAddItem.setVisible(user.getRole().equalsIgnoreCase("admin"));
		btnBasket.setVisible(!user.getRole().equalsIgnoreCase("admin"));

		// Defaults to keyboard view
		fillKeyboardTable();
	}

	private void fillKeyboardTable() {
		try {

			List<Keyboard> keyboards = Database.getKeyboards();
			String[] columns = null;
			boolean isAdmin = currentUser.getRole().equalsIgnoreCase("admin");

			if (isAdmin) {
				columns = new String[] { "Barcode", "Type", "Brand", "Colour", "Connectivity", "Stock Quantity",
						"Original Price (£)", "Retail Price (£)", "Layout" };

			} else {
				columns = new String[] { "Barcode", "Type", "Brand", "Colour", "Connectivity", "Stock Quantity",
						"Retail Price", "Layout" };
			}

			model.setColumnIdentifiers(columns);
			model.setRowCount(0);

			for (Keyboard keyboard : keyboards) {
				Object[] row = keyboard.getProperties(isAdmin).toArray();

				model.addRow(row);
			}

			table.setModel(model);
			panelBasket.setVisible(false);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(table, "Unable to get list of products.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void fillMouseTable() {
		try {
			List<Mouse> mice = Database.getMice();
			String[] columns = null;
			boolean isAdmin = currentUser.getRole().equalsIgnoreCase("admin");

			if (isAdmin) {
				columns = new String[] { "Barcode", "Type", "Brand", "Colour", "Connectivity", "Stock Quantity",
						"Original Price (£)", "Retail Price (£)", "No. of Buttons" };

			} else {
				columns = new String[] { "Barcode", "Type", "Brand", "Colour", "Connectivity", "Stock Quantity",
						"Retail Price", "No. of Buttons" };
			}

			model.setColumnIdentifiers(columns);
			model.setRowCount(0);

			for (Mouse mouse : mice) {
				Object[] row = mouse.getProperties(isAdmin).toArray();

				model.addRow(row);
			}

			table.setModel(model);
			panelBasket.setVisible(false);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(table, "Unable to get list of products.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void fillBasketTable() {
		// When current basket is empty, ask if user wants to
		// load a saved basket
		// only the latest saved basket will be loaded
		if (itemBasket.isEmpty()) {
			int option = JOptionPane.showConfirmDialog(table,
					"Your basket is empty.\nWould you like to load a saved basket?", "Basket",
					JOptionPane.YES_NO_OPTION);

			if (option == JOptionPane.YES_OPTION) {
				try {

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
					return;
				}
			} else {
				// Do nothing, exit from method.
				return;
			}
		}

		java.text.DecimalFormat dFormat = new java.text.DecimalFormat("###,##0.00");
		lblTotalCostNum.setText(dFormat.format(itemBasket.getTotalPrice()));

		panelBasket.setVisible(true);

		String[] columns = { "Barcode", "Category", "Type", "Brand", "Colour", "Quantity", "Price" };
		List<Item> itemList = itemBasket.getItems();

		model.setColumnIdentifiers(columns);
		model.setRowCount(0);

		for (Item item : itemList) {
			Object[] prop = item.getProperties(false).toArray();
			String category = (item instanceof Keyboard ? "keyboard" : "mouse");

			Object[] rowData = { prop[0], category, prop[1], prop[2], prop[3], item.quantity, prop[6] };
			model.addRow(rowData);
		}

		table.setModel(model);
	}

	private Item getSelectedItem() throws FileNotFoundException, IndexOutOfBoundsException {
		int selectedRow = table.getSelectedRow();
		String barcode = model.getValueAt(selectedRow, 0).toString();
		return Database.scanBarcode(barcode);
	}

	private void addToBasket() {
		try {
			itemBasket.addItem(getSelectedItem());

			btnBasket.setText("Basket ( " + itemBasket.getSize() + " )");

			if (panelBasket.isVisible()) {
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

	private void removeFromBasket() {
		try {
			itemBasket.removeItem(getSelectedItem());

			if (itemBasket.getSize() > 0) {
				btnBasket.setText("Basket ( " + itemBasket.getSize() + " )");
				fillBasketTable();
			} else {
				btnBasket.setText("Basket");
				fillKeyboardTable();
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
	 */
	public Application() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 1024, 768);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

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
							Database.cancelPurchase(currentUser, itemBasket);
							itemBasket.clear();
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}

					System.exit(0);
				}
			}
		});

		JPanel panelNav = new JPanel();
		panelNav.setBounds(5, 5, 1008, 50);
		contentPane.add(panelNav);
		panelNav.setLayout(null);

		JButton btnKeyboard = new JButton("Browse Keyboards");
		btnKeyboard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fillKeyboardTable();
			}
		});
		btnKeyboard.setBounds(10, 11, 145, 28);
		panelNav.add(btnKeyboard);

		JButton btnMice = new JButton("Browse Mice");
		btnMice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fillMouseTable();
			}
		});
		btnMice.setBounds(165, 11, 145, 28);
		panelNav.add(btnMice);

		btnAddItem = new JButton("Add Item");
		btnAddItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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
				fillBasketTable();
			}
		});
		btnBasket.setBounds(853, 11, 145, 28);
		panelNav.add(btnBasket);

		btnAddToBasket = new JButton("Add to Basket");
		btnAddToBasket.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				addToBasket();
			}
		});
		btnAddToBasket.setBounds(475, 11, 145, 28);
		panelNav.add(btnAddToBasket);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(5, 128, 1008, 600);
		contentPane.add(scrollPane);

		table = new JTable();
		scrollPane.setViewportView(table);

		panelBasket = new JPanel();
		panelBasket.setBounds(5, 67, 1008, 50);
		contentPane.add(panelBasket);
		panelBasket.setLayout(null);
		panelBasket.setVisible(false);

		JButton btnClearBasket = new JButton("Clear Basket");
		btnClearBasket.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Database.cancelPurchase(currentUser, itemBasket);
					itemBasket.clear();
					btnBasket.setText("Basket ( " + itemBasket.getSize() + " )");
					fillKeyboardTable();

				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		btnClearBasket.setBounds(165, 11, 145, 28);
		panelBasket.add(btnClearBasket);

		JButton btnCheckout = new JButton("Checkout");
		btnCheckout.setBounds(853, 11, 145, 28);
		panelBasket.add(btnCheckout);

		JButton btnIncQuantity = new JButton("+");
		btnIncQuantity.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnIncQuantity.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addToBasket();
			}
		});
		btnIncQuantity.setBounds(10, 11, 70, 28);
		panelBasket.add(btnIncQuantity);

		JButton btnDecQuantity = new JButton("-");
		btnDecQuantity.setFont(new Font("Tahoma", Font.BOLD, 24));
		btnDecQuantity.setBounds(86, 11, 70, 28);
		panelBasket.add(btnDecQuantity);

		lblTotalCostNum = new JLabel("##,###.##");
		lblTotalCostNum.setFont(new Font("Segoe UI", Font.BOLD, 20));
		lblTotalCostNum.setHorizontalAlignment(SwingConstants.TRAILING);
		lblTotalCostNum.setBounds(719, 11, 124, 28);
		panelBasket.add(lblTotalCostNum);

		JLabel lblTotalCost = new JLabel("Total: £");
		lblTotalCost.setFont(new Font("Segoe UI", Font.BOLD, 16));
		lblTotalCost.setHorizontalAlignment(SwingConstants.TRAILING);
		lblTotalCost.setBounds(620, 11, 89, 28);
		panelBasket.add(lblTotalCost);
	}
}
