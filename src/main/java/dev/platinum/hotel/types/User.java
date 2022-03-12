package dev.platinum.hotel.types;

/**
 * The User class for parsing and dispatching user queries & mutations
 * @author Marcin Sęk
 */
public class User
{
	private int id;
	private String type;
	private String email;
	private String username;
	private String password;

	/**
	 * Constructor using only id
	 * @param id of the user
	 */
	public User(int id)
	{
		this.id = id;
	}

	/**
	 * Constructor using only email and password
	 * @param email address of the user
	 * @param password of the user
	 */
	public User(String email, String password)
	{
		this.email = email;
		this.password = password;
	}

	/**
	 * Constructor using id, type and email address
	 * @param id of the user
	 * @param type of user
	 * @param email address of the user
	 */
	public User(int id, String type, String email)
	{
		this(id);
		this.type = type;
		this.email = email;
	}

	/**
	 * Constructor using id, type, email and username
	 * @param id of user
	 * @param type of user
	 * @param email address of user
	 * @param username of user
	 */
	public User(int id, String type, String email, String username)
	{
		this(id, type, email);
		this.username = username;
	}

	/**
	 * Constructor using type, email, username and password
	 * @param type of user
	 * @param email address of the user
	 * @param username of the user
	 * @param password of the user
	 */
	public User(String type, String email, String username, String password)
	{
		this(email, password);
		this.username = username;

		if (type == null)
		{
			this.type = "Customer";
		}
		else
		{
			this.type = type;
		}

	}

	/**
	 * Constructor using all user parameters
	 * @param id of the user
	 * @param type of user
	 * @param email address of the user
	 * @param username of the user
	 * @param password of the user
	 */
	public User(int id, String type, String email, String username, String password)
	{
		this(type, email, username, password);
		this.id = id;
	}

	/**
	 * Simple id getter
	 * @return id of user as a int
	 */
	public int getId()
	{
		return this.id;
	}

	/**
	 * Simple type getter
	 * @return type of user as a String
	 */
	public String getType()
	{
		return this.type;
	}

	/**
	 * Simple email getter
	 * @return email address of the user as a String
	 */
	public String getEmail()
	{
		return this.email;
	}

	/**
	 * Simple username getter
	 * @return username of the user as a String
	 */
	public String getUsername()
	{
		return this.username;
	}

	/**
	 * Simple password getter
	 * @return password of the user as a String
	 */
	public String getPassword()
	{
		return this.password;
	}

	/**
	 * Simple id setter
	 * @param newId desired id for the user
	 */
	public void setId(int newId)
	{
		this.id = newId;
	}

	/**
	 * Simple type setter
	 * @param newType desired type for the user
	 */
	public void setType(String newType)
	{
		this.type = newType;
	}

	/**
	 * Simple email setter
	 * @param newEmail desired email for the user
	 */
	public void setEmail(String newEmail)
	{
		this.email = newEmail;
	}

	/**
	 * Simple username setter
	 * @param newUsername desired username for the user
	 */
	public void setUsername(String newUsername)
	{
		this.username = newUsername;
	}

	/**
	 * Simple password setter
	 * @param newPassword desired password for the user
	 */
	public void setPassword(String newPassword)
	{
		this.password = newPassword;
	}
}
