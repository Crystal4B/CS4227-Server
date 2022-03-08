package dev.platinum.hotel.types;

import java.util.ArrayList;

/**
 * The Room class for parsing and dispatching room queries & mutations
 * @author Marcin Sęk
 */
public class Room
{
	private String id;
	private String type;
	private String name;
	private String perks;
	private int numberOfBeds;
	private int rate;
	private ArrayList<User> occupants;

	/**
	 * Constructor using id only
	 * @param id of the room
	 */
	public Room(String id)
	{
		this.id = id;
	}

	/**
	 * Constructor using name, type, perks, numberOfBeds and Rate
	 * @param name of the room
	 * @param type of room
	 * @param perks given with room
	 * @param numberOfBeds in the room
	 * @param rate for the room on a daily basis
	 */
	public Room(String name, String type, String perks, int numberOfBeds, int rate)
	{
		this.type = type;
		this.name = name;
		this.perks = perks;
		this.numberOfBeds = numberOfBeds;
		this.rate = rate;
	}

	/**
	 * Constructor using name, type, perks, numberOfBeds, rate and occupants
	 * @param name of the room
	 * @param type of room
	 * @param perks given with room
	 * @param numberOfBeds in the room
	 * @param rate for the room on a daily basis
	 * @param occupants list of users staying in the room
	 */
	public Room(String name, String type, String perks, int numberOfBeds, int rate, ArrayList<User> occupants)
	{
		this(name, type, perks, numberOfBeds, rate);
		this.occupants = occupants;
	}

	/**
	 * Constructor using id, name, type, perks, numberOfBeds, rate and occupants
	 * @param id of the room in the database
	 * @param type of room
	 * @param name of the room
	 * @param perks given with room
	 * @param numberOfBeds in the room
	 * @param rate for the room on a daily basis
	 * @param occupants list of users staying in the room
	 */
	public Room(String id, String type, String name, String perks, int numberOfBeds, int rate, ArrayList<User> occupants)
	{
		this(name, type, perks, numberOfBeds, rate, occupants);
		this.id = id;
	}

	/**
	 * Simple id getter
	 * @return id of the room as String
	 */
	public String getId()
	{
		return this.id;
	}

	/**
	 * Simple type getter
	 * @return the type of room as String
	 */
	public String getType()
	{
		return this.type;
	}

	/**
	 * Simple name getter
	 * @return the name of the room as a String
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * Simple perk getter
	 * @return the perks of the room as a String
	 */
	public String getPerks()
	{
		return this.perks;
	}

	/**
	 * Simple number of beds getter
	 * @return the number of beds in the room as an int
	 */
	public int getNumberOfBeds()
	{
		return this.numberOfBeds;
	}

	/**
	 * Simple rate getter
	 * @return the rate of the room on a daily basis as an int
	 */
	public int getRate()
	{
		return this.rate;
	}

	/**
	 * Simple occupants getter
	 * @return a list of Users occupying the room
	 */
	public ArrayList<User> getOccupants()
	{
		return this.occupants;
	}

	/**
	 * Simple id setter
	 * @param newId desired id for the room
	 */
	public void setId(String newId)
	{
		this.id = newId;
	}

	/**
	 * Simple type setter
	 * @param newType desired type for the room
	 */
	public void setType(String newType)
	{
		this.type = newType;
	}

	/**
	 * Simple name setter
	 * @param newName desired name for the room
	 */
	public void setName(String newName)
	{
		this.name = newName;
	}

	/**
	 * Simple perks setter
	 * @param newPerks desired perks for the room
	 */
	public void setPerks(String newPerks)
	{
		this.perks = newPerks;
	}

	/**
	 * Simple numberOfBeds setter
	 * @param newNumber desired number of beds for the room
	 */
	public void setNumberOfBeds(int newNumber)
	{
		this.numberOfBeds = newNumber;
	}

	/**
	 * Simple rate setter
	 * @param newRate desired daily rate for the room
	 */
	public void setRate(int newRate)
	{
		this.rate = newRate;
	}

	/**
	 * Simple occupants setter
	 * @param newOccupants desired occupants for the room
	 */
	public void setOccupants(ArrayList<User> newOccupants)
	{
		this.occupants = newOccupants;
	}
}
