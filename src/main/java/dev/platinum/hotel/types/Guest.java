package dev.platinum.hotel.types;

//TODO: ADD JAVADOCS

public class Guest
{
	private String id;
	private String firstName;
	private String lastName;
	private Room room;

	public Guest(String id)
	{
		this.id = id;
	}

	public Guest(String id, String firstName, String lastName, Room room)
	{
		this(id);
		this.firstName = firstName;
		this.lastName = lastName;
		this.room = room;
	}

	public String getId()
	{
		return this.id;
	}

	public String getFirstName()
	{
		return this.firstName;
	}

	public String getLastName()
	{
		return this.lastName;
	}

	public Room getRooms()
	{
		return this.room;
	}

	public void setId(String newId)
	{
		this.id = newId;
	}

	public void setFirstName(String newFirstName)
	{
		this.firstName = newFirstName;
	}

	public void setLastName(String newLastName)
	{
		this.lastName = newLastName;
	}

	public void setRooms(Room room)
	{
		this.room = room;
	}
}