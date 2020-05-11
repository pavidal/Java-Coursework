import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Window;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;

public class Checkout extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JLabel lblPayment;
	private JComboBox<String> comboPayment;
	private JLabel lblCardNo;
	private JButton okButton;
	private JTextField fieldEmail;
	private JTextField fieldCardSec;
	private JTextField fieldCardNo;

	private boolean btnOkPressed = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Checkout dialog = new Checkout();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getPaymentType() {
		if (btnOkPressed) {
			return (String) comboPayment.getSelectedItem();
		}

		return null;
	}

	public void validateFields() {
		String cardNo = fieldCardNo.getText();
		String cardSec = fieldCardSec.getText();
		String email = fieldEmail.getText();

		// Payment Selection
		boolean isCard = (comboPayment.getSelectedIndex() == 0);

		// Assume input is valid
		boolean isValid = true;

		String[] toValidate = (isCard ? new String[] { cardNo, cardSec } : new String[] { email });

		for (String field : toValidate) {
			isValid = isValid && !(field.isEmpty() || field.contains(","));
		}

		// Regex from https://howtodoinjava.com/regex/java-regex-validate-email-address/
		String regex = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
		Pattern emailPattern = Pattern.compile(regex);
		Matcher emailMatcher = emailPattern.matcher(email);

		// Could clean up this but can't be bothered
		if (!isCard && !emailMatcher.matches()) {
			// Validate email with regex for paypal
			isValid = false;

		} else if (isCard) {

			// Checks if input is integer for card
			try {
				Long.parseLong(cardNo);
				Long.parseLong(cardSec);
			} catch (Exception e) {
				isValid = false;
			}

			// Check for card details' length
			if (cardNo.length() != 16 || cardSec.length() != 3) {
				isValid = false;
			}
		}

		okButton.setEnabled(isValid);
	}

	/**
	 * Create the dialog.
	 */
	public Checkout() {
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 480, 465);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblTitle = new JLabel("Checkout Basket");
			lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
			lblTitle.setBounds(10, 11, 424, 40);
			contentPanel.add(lblTitle);
		}
		{
			lblPayment = new JLabel("Payment Type");
			lblPayment.setFont(new Font("Segoe UI", Font.BOLD, 18));
			lblPayment.setBounds(10, 95, 454, 30);
			contentPanel.add(lblPayment);
		}
		{
			comboPayment = new JComboBox<String>();
			comboPayment.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					boolean condition = (comboPayment.getSelectedIndex() == 0);

					fieldCardNo.setEnabled(condition);
					fieldCardSec.setEnabled(condition);

					fieldEmail.setEnabled(!condition);

					validateFields();
				}
			});
			comboPayment.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblPayment.setLabelFor(comboPayment);
			comboPayment.setModel(new DefaultComboBoxModel<String>(new String[] { "Credit Card", "PayPal" }));
			comboPayment.setBounds(10, 136, 227, 30);
			contentPanel.add(comboPayment);
		}
		{
			JSeparator separator = new JSeparator();
			separator.setBounds(10, 177, 454, 10);
			contentPanel.add(separator);
		}
		{
			JPanel panelCard = new JPanel();
			panelCard.setBounds(10, 198, 454, 90);
			contentPanel.add(panelCard);
			panelCard.setLayout(null);
			{
				lblCardNo = new JLabel("Card Number");
				lblCardNo.setFont(new Font("Segoe UI", Font.BOLD, 14));
				lblCardNo.setBounds(10, 11, 100, 30);
				panelCard.add(lblCardNo);
			}

			JLabel lblCardSec = new JLabel("CVV");
			lblCardSec.setFont(new Font("Segoe UI", Font.BOLD, 14));
			lblCardSec.setBounds(10, 52, 100, 30);
			panelCard.add(lblCardSec);

			fieldCardNo = new JTextField();
			lblCardNo.setLabelFor(fieldCardNo);
			fieldCardNo.getDocument().addDocumentListener(new DocumentListener() {

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
			fieldCardNo.setBounds(120, 11, 324, 30);
			panelCard.add(fieldCardNo);
			fieldCardNo.setColumns(10);

			fieldCardSec = new JTextField();
			lblCardSec.setLabelFor(fieldCardSec);
			fieldCardSec.getDocument().addDocumentListener(new DocumentListener() {

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
			fieldCardSec.setColumns(10);
			fieldCardSec.setBounds(120, 52, 100, 30);
			panelCard.add(fieldCardSec);
		}

		JSeparator separator = new JSeparator();
		separator.setBounds(10, 299, 454, 10);
		contentPanel.add(separator);

		JPanel panel = new JPanel();
		panel.setBounds(10, 320, 454, 50);
		contentPanel.add(panel);
		panel.setLayout(null);

		JLabel lblEmailAddress = new JLabel("Email Address");
		lblEmailAddress.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblEmailAddress.setBounds(10, 11, 100, 30);
		panel.add(lblEmailAddress);

		fieldEmail = new JTextField();
		fieldEmail.setEnabled(false);
		fieldEmail.getDocument().addDocumentListener(new DocumentListener() {

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
		lblEmailAddress.setLabelFor(fieldEmail);
		fieldEmail.setColumns(10);
		fieldEmail.setBounds(120, 11, 324, 30);
		panel.add(fieldEmail);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
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
					public void actionPerformed(ActionEvent e) {
						Window thisWindow = Window.getWindows()[0];

						btnOkPressed = false;

						dispatchEvent(new WindowEvent(thisWindow, WindowEvent.WINDOW_CLOSING));
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
