package dev.platinum.hotel.types;

import java.sql.Timestamp;
import java.util.List;

// TODO: UPDATE JAVADOCS

/**
 * The Reservation class for parsing and dispatching reservation queries & mutations
 * @author Marcin Sęk
 */
public class Reservation
{
	private String id;
	private Timestamp checkIn;
	private Timestamp checkOut;
	private User user;
	private List<Guest> guests;

	/**
	 * Constructor using arrival and departure dates only
	 * @param arrivalDate the check-in date of the reservation
	 * @param departureDate the check-out date of the reservation
	 */
	public Reservation(Timestamp checkIn, Timestamp checkOut)
	{
		this.checkIn = checkIn;
		this.checkOut = checkOut;
	}

	/**
	 * Constructor using arrivalDate, departureDate and rooms
	 * @param checkIn the check-in date of the reservation
	 * @param checkOut the check-out date of the reservation
	 * @param user
	 * @param guests
	 */
	public Reservation(Timestamp checkIn, Timestamp checkOut, User user, List<Guest> guests)
	{
		this(checkIn, checkOut);
		this.user = user;
		this.guests = guests;
	}

	/**
	 * Constructor using arrivalDate, departureDate and rooms
	 * @param id the id of the reservation in the database
	 * @param checkIn the check-in date of the reservation
	 * @param checkOut the check-out date of the reservation
	 * @param user
	 * @param guests
	 */
	public Reservation(String id, Timestamp checkIn, Timestamp checkOut, User user, List<Guest> guests)
	{
		this(checkIn, checkOut, user, guests);
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
	 * Simple checkIn getter
	 * @return checkIn as Timestamp
	 */
	public Timestamp getCheckIn()
	{
		return this.checkIn;
	}

	/**
	 * Simple checkOut getter
	 * @return checkOut as Timestamp
	 */
	public Timestamp getCheckOut()
	{
		return this.checkOut;
	}

	public User getUser()
	{
		return this.user;
	}

	/**
	 * Simple rooms getter
	 * @return rooms as ArrayList[Room]
	 */
	public List<Guest> getGuests()
	{
		return this.guests;
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
	 * Simple checkIn setter
	 * @param newDate desired checkIn for reservation
	 */
	public void setCheckIn(Timestamp newDate)
	{
		this.checkIn = newDate;
	}

	/**
	 * Simple checkOut setter
	 * @param newDate desired checkOut for reservation
	 */
	public void setCheckOut(Timestamp newDate)
	{
		this.checkOut = newDate;
	}

	/**
	 * Simple rooms setter
	 * @param newRooms desired list of rooms for reservation
	 */
	public void setGuests(List<Guest> newGuests)
	{
		this.guests = newGuests;
	}

	@Override
	public String toString()
	{
		// TODO: Add Rooms to String here
		return String.format("%s, %s, %s, %s, %d", id, checkIn, checkOut);
	}
}
