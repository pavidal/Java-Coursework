
public class User {
	
	private String id;
	private String username;
	private String surname;
	private String postcode;
	private String houseno;
	private String city;
	private String role;
	
	
	public User(String[] user) {
		this.id = user[0];
		this.username = user[1];
		this.surname = user[2];
		this.houseno = user[3];
		this.postcode = user[4];
		this.city = user[5];
		this.role = user[6];
	}
	
	public String getRole() {
		return this.role;
	}
	
	public String getName() {
		return this.username;
	}
	
	public String getID() {
		return id;
	}
	
	public String getPostcode() {
		return this.postcode;
	}
	
	public boolean validateLogin(String username, String surname, String postcode) {
		
		boolean condition = this.username.equals(username) 
				&& this.surname.equalsIgnoreCase(surname) 
				&& this.postcode.equalsIgnoreCase(postcode);
		
		return condition;
	}
	
	public String[] getAll() {
		String[] arr = {
				this.id,
				this.username,
				this.surname,
				this.houseno,
				this.postcode,
				this.city,
				this.role
		};
		return arr;
	}
}
