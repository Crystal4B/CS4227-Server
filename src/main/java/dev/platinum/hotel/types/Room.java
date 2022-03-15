package dev.platinum.hotel.types;

/**
 * The Room class for parsing and dispatching room queries & mutations
 * @author Marcin Sęk
 */
public class Room
{
	private int id;
	private String type;
	private String perks;
	private int numberOfBeds;
	private int rate;

	/**
	 * Constructor using id only
	 * @param id of the room
	 */
	public Room(int id)
	{
		this.id = id;
	}

	/**
	 * Constructor using type, perks, numberOfBeds and Rate
	 * @param type of room
	 * @param perks given with room
	 * @param numberOfBeds in the room
	 * @param rate for the room on a daily basis
	 */
	public Room(String type, String perks, int numberOfBeds, int rate)
	{
		this.type = type;
		this.perks = perks;
		this.numberOfBeds = numberOfBeds;
		this.rate = rate;
	}

	/**
	 * Constructor using id, name, type, perks, numberOfBeds, rate and occupants
	 * @param id of the room in the database
	 * @param type of room
	 * @param perks given with room
	 * @param numberOfBeds in the room
	 * @param rate for the room on a daily basis
	 */
	public Room(int id, String type, String perks, int numberOfBeds, int rate)
	{
		this(type, perks, numberOfBeds, rate);
		this.id = id;
	}

	/**
	 * Simple id getter
	 * @return id of the room as int
	 */
	public int getId()
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
	 * Simple id setter
	 * @param newId desired id for the room
	 */
	public void setId(int newId)
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
}
