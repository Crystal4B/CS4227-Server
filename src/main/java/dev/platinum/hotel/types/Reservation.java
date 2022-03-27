package dev.platinum.hotel.types;

import java.sql.Timestamp;
import java.util.List;

/**
 * The Reservation class for parsing and dispatching reservation queries & mutations
 * @author Marcin Sęk
 */
public class Reservation
{
	private int id;
	private Timestamp checkIn;
	private Timestamp checkOut;
	private boolean paid;
	private User user;
	private List<Guest> guests;

	
	/**
	 * Constructor using arrival and departure dates only
	 * @param id the id of the reservation in the database
	 */
	public Reservation(int id)
	{
		this.id = id;
	}
	
	/**
	 * Constructor using arrival and departure dates only
	 * @param checkIn the check-in date of the reservation
	 * @param checkOut the check-out date of the reservation
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
	 * @param user the user who made the reservation
	 * @param guests the guests staying in the hotel in the reservation
	 */
	public Reservation(Timestamp checkIn, Timestamp checkOut, User user, List<Guest> guests)
	{
		this(checkIn, checkOut);
		this.user = user;
		this.guests = guests;
	}

	/**
	 * Constructor using checkIn, checkOut and rooms
	 * @param id the id of the reservation in the database
	 * @param checkIn the check-in date of the reservation
	 * @param checkOut the check-out date of the reservation
	 * @param user the user who made the reservation
	 * @param guests the guests staying in the hotel in the reservation
	 */
	public Reservation(int id, Timestamp checkIn, Timestamp checkOut, User user, List<Guest> guests)
	{
		this(checkIn, checkOut, user, guests);
		this.id = id;
	}

	/**
	 * Constructor creating a complete reservation object
	 * @param id of the reservation in the database
	 * @param checkIn date of the reservation
	 * @param checkOut date of the reservation
	 * @param user who made the reservation
	 * @param guests staying in the hotel in this reservation
	 * @param paid whether the reservation is paid or not
	 */
	public Reservation(int id, Timestamp checkIn, Timestamp checkOut, User user, List<Guest> guests, boolean paid)
	{
		this(id, checkIn, checkOut, user, guests);
		this.paid = paid;
    }

    /**
	 * Simple id getter
	 * @return id of reservation as int
	 */
	public int getId()
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

	/**
	 * Simple paid getter
	 * @return paid as boolean
	 */
	public boolean getPaid()
	{
		return this.paid;
	}

	/**
	 * Simple user getter
	 * @return user who created the reservation
	 */
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
	public void setId(int newId)
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
	 * Simple paid setter
	 * @param newPaid desired paid state for reservation
	 */
	public void setPaid(boolean newPaid)
	{
		this.paid = newPaid;
	}

	/**
	 * Simple user setter
	 * @param newUser desired user for reservation
	 */
	public void setUser(User newUser)
	{
		this.user = newUser;
	}

	/**
	 * Simple guests setter
	 * @param newGuests desired list of guests for reservation
	 */
	public void setGuests(List<Guest> newGuests)
	{
		this.guests = newGuests;
	}
}
