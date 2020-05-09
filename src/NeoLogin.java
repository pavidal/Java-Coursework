import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Window;

import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;

public class NeoLogin extends JFrame {

	private JPanel contentPane;
	private JTextField fieldUsername;
	private JTextField fieldSurname;
	private JTextField fieldPostcode;
	private JButton btnLogin;
	
	private User currentUser;
	
	/**
	 * Validate all fields.
	 * Enables Login button when all fields matches the requirements.
	 */
	private void validateFields() {
		boolean isValid = true;

		// Check if each is empty & sanitise inputs
        for (JTextField field : new JTextField[] {fieldUsername, fieldPostcode, fieldSurname}) {
            isValid = isValid && !(field.getText().isEmpty() || field.getText().contains(","));
        }

        // Post code regex from https://en.wikipedia.org/wiki/Postcodes_in_the_United_Kingdom#Validation
        String regex = "^(([A-Z]{1,2}[0-9][A-Z0-9]?|ASCN|STHL|TDCU|BBND|[BFS]IQQ|PCRN|TKCA) ?[0-9][A-Z]{2}|BFPO ?[0-9]{1,4}|(KY[0-9]|MSR|VG|AI)[ -]?[0-9]{4}|[A-Z]{2} ?[0-9]{2}|GE ?CX|GIR ?0A{2}|SAN ?TA1)$";
        Pattern postcode = Pattern.compile(regex);
        Matcher match = postcode.matcher(fieldPostcode.getText().toUpperCase());

        if (!match.matches()) isValid = false;

        btnLogin.setEnabled(isValid);
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NeoLogin frame = new NeoLogin();
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
	public NeoLogin() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setAlwaysOnTop(true);
		setFont(new Font("Segoe UI", Font.PLAIN, 12));
		setResizable(false);
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 480, 630);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// When login is successful, the login window is disposed
        // and detected with this instead of exiting.
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
            	// Remove listener as this is a one-time action
            	removeWindowListener(this);
            	
            	// Show main window
            	Application app = new Application();
            	app.setUser(currentUser);
            	app.setVisible(true);
            }
        });
		
		JLabel formTitle = new JLabel("Login");
		formTitle.setFont(new Font("Segoe UI", Font.BOLD, 48));
		formTitle.setBounds(10, 11, 444, 75);
		contentPane.add(formTitle);
		
		fieldUsername = new JTextField();
		fieldUsername.setFont(new Font("Segoe UI", Font.PLAIN, 28));
		fieldUsername.setBounds(10, 165, 454, 50);
		contentPane.add(fieldUsername);
		fieldUsername.setColumns(10);
		// Validate all fields on the fly
		fieldUsername.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				validateFields();
				
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				validateFields();
				
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				validateFields();
				
			}
			
		});	
		JLabel lbUsername = new JLabel("Username");
		lbUsername.setLabelFor(fieldUsername);
		lbUsername.setFont(new Font("Segoe UI", Font.BOLD, 24));
		lbUsername.setBounds(10, 118, 454, 36);
		contentPane.add(lbUsername);
		
		JLabel lbSurname = new JLabel("Last Name");
		lbSurname.setFont(new Font("Segoe UI", Font.BOLD, 24));
		lbSurname.setBounds(10, 248, 454, 36);
		contentPane.add(lbSurname);
		
		fieldSurname = new JTextField();
		lbSurname.setLabelFor(fieldSurname);
		fieldSurname.setFont(new Font("Segoe UI", Font.PLAIN, 28));
		fieldSurname.setColumns(10);
		fieldSurname.setBounds(10, 295, 454, 50);
		contentPane.add(fieldSurname);
		// Validate all fields on the fly
		fieldSurname.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent e) {
				validateFields();
				
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				validateFields();
				
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				validateFields();
				
			}
			
		});
		
		JLabel lblPostCode = new JLabel("Post Code");
		lblPostCode.setFont(new Font("Segoe UI", Font.BOLD, 24));
		lblPostCode.setBounds(10, 381, 454, 36);
		contentPane.add(lblPostCode);
		
		fieldPostcode = new JTextField();
		lblPostCode.setLabelFor(fieldPostcode);
		fieldPostcode.setFont(new Font("Segoe UI", Font.PLAIN, 28));
		fieldPostcode.setColumns(10);
		fieldPostcode.setBounds(10, 428, 454, 50);
		contentPane.add(fieldPostcode);
		// Validate all fields on the fly
		fieldPostcode.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent e) {
				validateFields();
				
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				validateFields();
				
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				validateFields();
				
			}
			
		});
		
		JButton btnQuit = new JButton("Quit");
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnQuit.setFont(new Font("Segoe UI", Font.BOLD, 24));
		btnQuit.setForeground(new Color(0, 0, 0));
		btnQuit.setBackground(new Color(255, 153, 153));
		btnQuit.setBounds(10, 527, 155, 50);
		contentPane.add(btnQuit);
		
		btnLogin = new JButton("Login");
		btnLogin.setEnabled(false);
		btnLogin.addActionListener(new ActionListener() {
			/**
			 * Verify all fields. 
			 * If input matches an entry in DB, sets current user for main and disposes window. 
			 */
			public void actionPerformed(ActionEvent e) {
				Window thisWindow = Window.getWindows()[0];
				
				String username = fieldUsername.getText();
				String surname = fieldSurname.getText();
				String postcode = fieldPostcode.getText();
				
				try {
					ArrayList<User> userList = Database.getUsers();
					
					for (User u : userList) {
						System.out.println(Arrays.deepToString(u.getAll()));
						if (u.validateLogin(username, surname, postcode)) {
							// Send the current user details
							currentUser = u;
							
							// Dispose window so windowlistener can detect
							setDefaultCloseOperation(DISPOSE_ON_CLOSE);
							dispatchEvent(new WindowEvent(thisWindow, WindowEvent.WINDOW_CLOSING));
							return;
						}
					}
					
					// When none matched
					JOptionPane.showMessageDialog(thisWindow, "Wrong login details. \nPlease check your credentials.", "Bad Login.", JOptionPane.WARNING_MESSAGE);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(thisWindow, "Unable to read database.", "Error", JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});
		btnLogin.setForeground(new Color(0, 0, 0));
		btnLogin.setBackground(new Color(204, 255, 204));
		btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 24));
		btnLogin.setBounds(175, 527, 279, 50);
		contentPane.add(btnLogin);
	}

}
