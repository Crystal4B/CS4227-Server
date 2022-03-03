package dev.platinum.hotel.types;

public class User
{
	private String id;
	private String type;
	private String email;
	private String username;
	private String password;

	public User(String id)
	{
		this.id = id;
	}

	public User(String email, String password)
	{
		this.email = email;
		this.password = password;
	}

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

	public User(String id, String type, String email, String username, String password)
	{
		this(type, email, username, password);
		this.id = id;
	}

	public String getId()
	{
		return this.id;
	}

	public String getType()
	{
		return this.type;
	}

	public String getEmail()
	{
		return this.email;
	}

	public String getUsername()
	{
		return this.username;
	}

	public String getPassword()
	{
		return this.password;
	}

	public void setId(String newId)
	{
		this.id = newId;
	}

	public void setType(String newType)
	{
		this.type = newType;
	}

	public void setEmail(String newEmail)
	{
		this.email = newEmail;
	}

	public void setUsername(String newUsername)
	{
		this.username = newUsername;
	}

	public void setPassword(String newPassword)
	{
		this.password = newPassword;
	}
}
