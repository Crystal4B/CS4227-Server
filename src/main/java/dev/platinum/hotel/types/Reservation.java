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

	/**
	 * Constructor using arrival and departure dates only
	 * @param arrivalDate the check-in date of the reservation
	 * @param departureDate the check-out date of the reservation
	 */
	public Reservation(Timestamp arrivalDate, Timestamp departureDate)
	{
		this.arrivalDate = arrivalDate;
		this.departureDate = departureDate;
	}

	/**
	 * Constructor using arrivalDate, departureDate and rooms
	 * @param arrivalDate the check-in date of the reservation
	 * @param departureDate the check-out date of the reservation
	 * @param rooms list of rooms associated with the reservation
	 */
	public Reservation(Timestamp arrivalDate, Timestamp departureDate, ArrayList<Room> rooms)
	{
		this(arrivalDate, departureDate);
		this.rooms = rooms;
	}

	/**
	 * Constructor using arrivalDate, departureDate and rooms
	 * @param id the id of the reservation in the database
	 * @param arrivalDate the check-in date of the reservation
	 * @param departureDate the check-out date of the reservation
	 * @param rooms list of rooms associated with the reservation
	 */
	public Reservation(String id, Timestamp arrivalDate, Timestamp departureDate, ArrayList<Room> rooms)
	{
		this(arrivalDate, departureDate, rooms);
		this.id = id;
	}

	/**
	 * Simple id getter
	 * @return id of reservation as string
	 */
	public String getId()
	{
		return this.id;
	}

	/**
	 * Simple arrivalDate getter
	 * @return arrivalDate as Timestamp
	 */
	public Timestamp arrivalDate()
	{
		return this.arrivalDate;
	}

	/**
	 * Simple departureDate getter
	 * @return departureDate as Timestamp
	 */
	public Timestamp departureDate()
	{
		return this.departureDate;
	}

	/**
	 * Simple rooms getter
	 * @return rooms as ArrayList[Room]
	 */
	public ArrayList<Room> getRooms()
	{
		return this.rooms;
	}

	/**
	 * Simple id setter
	 * @param newId desired id for reservation
	 */
	public void setId(String newId)
	{
		this.id = newId;
	}

	/**
	 * Simple arrivalDate setter
	 * @param newDate desired arrivalDate for reservation
	 */
	public void arrivalDate(Timestamp newDate)
	{
		this.arrivalDate = newDate;
	}

	/**
	 * Simple departureDate setter
	 * @param newDate desired departureDate for reservation
	 */
	public void departureDate(Timestamp newDate)
	{
		this.departureDate = newDate;
	}

	/**
	 * Simple rooms setter
	 * @param newRooms desired list of rooms for reservation
	 */
	public void setRooms(ArrayList<Room> newRooms)
	{
		this.rooms = newRooms;
	}

	@Override
	public String toString()
	{
		// TODO: Add Rooms to String here
		return String.format("%s, %s, %s, %s, %d", id, arrivalDate, departureDate);
	}
}
