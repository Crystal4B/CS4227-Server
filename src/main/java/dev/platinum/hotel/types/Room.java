package dev.platinum.hotel.types;

import java.util.ArrayList;

public class Room
{
	private String id;
	private String type;
	private String name;
	private String perks;
	private int numberOfBeds;
	private int rate;
	private ArrayList<User> occupants;

	public Room(String id)
	{
		this.id = id;
	}

	public Room(String name, String type, String perks, int numberOfBeds, int rate)
	{
		this.type = type;
		this.name = name;
		this.perks = perks;
		this.numberOfBeds = numberOfBeds;
		this.rate = rate;
	}

	public Room(String name, String type, String perks, int numberOfBeds, int rate, ArrayList<User> occupants)
	{
		this(name, type, perks, numberOfBeds, rate);
		this.occupants = occupants;
	}

	public Room(String id, String type, String name, String perks, int numberOfBeds, int rate, ArrayList<User> occupants)
	{
		this(name, type, perks, numberOfBeds, rate, occupants);
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

	public String getName()
	{
		return this.name;
	}

	public String getPerks()
	{
		return this.perks;
	}

	public int getNumberOfBeds()
	{
		return this.numberOfBeds;
	}

	public int getRate()
	{
		return this.rate;
	}

	public ArrayList<User> getOccupants()
	{
		return this.occupants;
	}

	public void setId(String newId)
	{
		this.id = newId;
	}

	public void setType(String newType)
	{
		this.type = newType;
	}

	public void setName(String newName)
	{
		this.name = newName;
	}

	public void setPerks(String newPerks)
	{
		this.perks = newPerks;
	}

	public void setNumberOfBeds(int newNumber)
	{
		this.numberOfBeds = newNumber;
	}

	public void setRate(int newRate)
	{
		this.rate = newRate;
	}

	public void setOccupants(ArrayList<User> newOccupants)
	{
		this.occupants = newOccupants;
	}
}
