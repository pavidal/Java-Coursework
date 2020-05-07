import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
//        System.out.println(Arrays.deepToString(Database.read(Database.PATH_LOG)));
        NeoLogin l = new NeoLogin();
        l.setVisible(true);
        
        // When login is successful, the login window is disposed
        // and detected with this instead of exiting.
        l.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
            	System.out.println("Login Successful.");
            	
            	// Remove listener as this is a one-time action
            	l.removeWindowListener(this);
            	
            	// TODO: Show main window
            }
        });
    }
    
    private static void init() {
    	System.out.println("afgag");
    }
}
