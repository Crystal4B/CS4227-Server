package dev.platinum.hotel.types;

/**
 * The Guest class for parsing and dispatching guest queries & mutations
 * @author Marcin Sęk
 */
public class Guest
{
	private int id;
	private String firstName;
	private String lastName;
	private Room room;

	/**
	 * Simple constructor for Guest with an Id
	 * @param id of the guest
	 */
	public Guest(int id)
	{
		this.id = id;
	}

	/**
	 * Simple constructor for Guest with an id, firstname and lastname
	 * @param id of the guest
	 * @param firstName of the guest
	 * @param lastName of the guest
	 */
	public Guest(int id, String firstName, String lastName)
	{
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	/**
	 * Simple constructor for Guest without id
	 * @param firstName of the guest
	 * @param lastName of the guest
	 * @param room occupied by the guest
	 */
	public Guest(String firstName, String lastName, Room room)
	{
		this.firstName = firstName;
		this.lastName = lastName;
		this.room = room;
	}

	/**
	 * Simple constructor for Guest with an Id, firstname, lastname and room
	 * @param id of the guest
	 * @param firstName of the guest
	 * @param lastName of the guest
	 * @param room occupied by the guest
	 */
	public Guest(int id, String firstName, String lastName, Room room)
	{
		this(id);
		this.firstName = firstName;
		this.lastName = lastName;
		this.room = room;
	}

	/**
	 * Simple id getter
	 * @return id of the guest as a String
	 */
	public int getId()
	{
		return this.id;
	}

	/**
	 * Simple first name getter
	 * @return first name of the guest as a String
	 */
	public String getFirstName()
	{
		return this.firstName;
	}

	/**
	 * Simple last name getter
	 * @return last name of the guest as a String
	 */
	public String getLastName()
	{
		return this.lastName;
	}

	/**
	 * Simple room getter
	 * @return room occupied by the guest
	 */
	public Room getRoom()
	{
		return this.room;
	}

	/**
	 * Simple id setter
	 * @param newId for the guest
	 */
	public void setId(int newId)
	{
		this.id = newId;
	}

	/**
	 * Simple first name setter
	 * @param newFirstName for the guest
	 */
	public void setFirstName(String newFirstName)
	{
		this.firstName = newFirstName;
	}

	/**
	 * Simple last name setter
	 * @param newLastName for the guest
	 */
	public void setLastName(String newLastName)
	{
		this.lastName = newLastName;
	}

	/**
	 * Simple room setter
	 * @param newRoom for the guest
	 */
	public void setRooms(Room newRoom)
	{
		this.room = newRoom;
	}
}