package dev.platinum.hotel.types;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * The Reservation class for parsing and dispatching reservation queries & mutations
 * @author Marcin Sęk
 */
public class Reservation
{
	private String id;
	private Timestamp arrivalDate;
	private Timestamp departureDate;
	private ArrayList<Room> rooms;

	public Reservation(Timestamp arrivalDate, Timestamp departureDate)
	{
		this.arrivalDate = arrivalDate;
		this.departureDate = departureDate;
	}

	public Reservation(Timestamp arrivalDate, Timestamp departureDate, ArrayList<Room> rooms)
	{
		this(arrivalDate, departureDate);
		this.rooms = rooms;
	}

	public Reservation(String id, Timestamp arrivalDate, Timestamp departureDate, ArrayList<Room> rooms)
	{
		this(arrivalDate, departureDate, rooms);
		this.id = id;
	}

	public String getId()
	{
		return this.id;
	}

	public Timestamp arrivalDate()
	{
		return this.arrivalDate;
	}

	public Timestamp departureDate()
	{
		return this.departureDate;
	}

	public ArrayList<Room> getRooms()
	{
		return this.rooms;
	}

	public void setId(String newId)
	{
		this.id = newId;
	}

	public void arrivalDate(Timestamp newDate)
	{
		this.arrivalDate = newDate;
	}

	public void departureDate(Timestamp newDate)
	{
		this.departureDate = newDate;
	}

	public void setRooms(ArrayList<Room> rooms)
	{
		this.rooms = rooms;
	}

	@Override
	public String toString()
	{
		// TODO: Add Rooms to String here
		return String.format("%s, %s, %s, %s, %d", id, arrivalDate, departureDate);
	}
}
