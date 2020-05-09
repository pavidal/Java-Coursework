import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.util.Arrays;
import javax.swing.JOptionPane;

public class Main {
	
	/**
	 * DEPRECATED
	 * TODO: Remove
	 */
	
	private static User currentUser;
	
	public static void setUser(User user) {
    	currentUser = user;
    }
	
    public static void main(String[] args) throws FileNotFoundException {
        NeoLogin l = new NeoLogin();
        l.setVisible(true);
        
        // When login is successful, the login window is disposed
        // and detected with this instead of exiting.
        l.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
            	System.out.println("Login Successful.");
            	System.out.println(Arrays.toString(currentUser.getAll()));
            	
            	// Remove listener as this is a one-time action
            	l.removeWindowListener(this);
            	
            	// TODO: Show main window
            	initApp();
            }
        });
    }
    
    private static void initApp() {
    	Application app = new Application();
    	app.setUser(currentUser);
    	app.setVisible(true);
    	
//    	// Ask for confirmation when exiting
//    	app.addWindowListener(new WindowAdapter() {
//    		public void windowClosing(WindowEvent e) {
//    			String message = "Are you sure you want to quit?";
//    			
//    			// Add basket clear reminder for customers
//    			if (!currentUser.getRole().equalsIgnoreCase("admin")) {
//    				message += "\nAll unsaved items in the basket will be lost.";
//    			}
//    			
//    			int option = JOptionPane.showConfirmDialog(app, message, "Quit", JOptionPane.YES_NO_OPTION);
//				
//    			if (option == JOptionPane.YES_OPTION) {
//    				System.exit(0);
//    			}
//			}
//		});
    }
}
