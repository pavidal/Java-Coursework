import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends JFrame {
    private JLabel usernameLabel;
    private JLabel loginLabel;
    private JTextField usernameField;
    private JTextField surnameField;
    private JLabel surnameLabel;
    private JLabel postCodeLabel;
    private JTextField postCodeField;
    private JButton quitButton;
    private JButton loginButton;
    private JPanel LoginPanel;

    public Login() {
        usernameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                validateLogin();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                validateLogin();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                validateLogin();
            }
        });

        surnameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                validateLogin();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                validateLogin();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                validateLogin();
            }
        });

        postCodeField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                validateLogin();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                validateLogin();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                validateLogin();
            }
        });
    }

    private void validateLogin() {
        boolean isValid = true;

        for (JTextField field : new JTextField[] {usernameField, postCodeField, surnameField}) {
            isValid = isValid && (!field.getText().isEmpty() || !field.getText().contains(","));
        }

        Pattern postcode = Pattern.compile("^[A-Z]{1,2}[0-9R][0-9A-Z]? [0-9][ABD-HJLNP-UW-Z]{2}$");
        Matcher match = postcode.matcher(postCodeField.getText());

        if (!match.matches()) isValid = false;

        loginButton.setEnabled(isValid);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Login");
        frame.setContentPane(new Login().LoginPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                
            }
        });
    }


}
