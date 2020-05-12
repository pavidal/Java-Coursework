
public class User {

	private String id;
	private String username;
	private String surname;
	private String postcode;
	@SuppressWarnings("unused")
	private String houseno; // What's the point of storing this?
	@SuppressWarnings("unused")
	private String city; // and this? It's never used.
	private String role;

	/**
	 * Creates a new user
	 * 
	 * @param user - Array of user information (same format as database)
	 */
	public User(String[] user) {
		this.id = user[0];
		this.username = user[1];
		this.surname = user[2];
		this.houseno = user[3];
		this.postcode = user[4];
		this.city = user[5];
		this.role = user[6];
	}

	/**
	 * Gets the user's role
	 * 
	 * @return Whether the user is an "admin" or "customer"
	 */
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

	/**
	 * Checks if login details matches this user's information
	 * 
	 * @param username
	 * @param surname
	 * @param postcode
	 * @return Boolean if condition is matched
	 */
	public boolean validateLogin(String username, String surname, String postcode) {

		boolean condition = this.username.equals(username) && this.surname.equalsIgnoreCase(surname)
				&& this.postcode.equalsIgnoreCase(postcode);

		return condition;
	}
}
