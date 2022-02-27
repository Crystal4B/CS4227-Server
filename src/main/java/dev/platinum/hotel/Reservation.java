package dev.platinum.hotel;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * The Reservation class for parsing and dispatching reservation queries & mutations
 * @author Marcin Sęk
 */
public class Reservation
{
	private String id;
	private Timestamp reservationDate;
	private Timestamp arrivalDate;
	private Timestamp departureDate;
	private int numberOfOccupants;
	private ArrayList<Room> rooms;

	public Reservation(Timestamp reservationDate, Timestamp arrivalDate, Timestamp departureDate, int numberOfOccupants)
	{
		this.reservationDate = reservationDate;
		this.arrivalDate = arrivalDate;
		this.departureDate = departureDate;
		this.numberOfOccupants = numberOfOccupants;
	}

	public Reservation(String id, Timestamp reservationDate, Timestamp arrivalDate, Timestamp departureDate, int numberOfOccupants)
	{
		this(reservationDate, arrivalDate, departureDate, numberOfOccupants);
		this.id = id;
	}

	public Reservation(String id, Timestamp reservationDate, Timestamp arrivalDate, Timestamp departureDate,
	int numberOfOccupants, ArrayList<Room> rooms)
	{
		this(id, reservationDate, arrivalDate, departureDate, numberOfOccupants);
		this.rooms = rooms;
	}

	public String getId()
	{
		return this.id;
	}

	public Timestamp getReservationDate()
	{
		return this.reservationDate;
	}

	public Timestamp arrivalDate()
	{
		return this.arrivalDate;
	}

	public Timestamp departureDate()
	{
		return this.departureDate;
	}

	public int numberOfOccupants()
	{
		return this.numberOfOccupants;
	}

	public ArrayList<Room> getRooms()
	{
		return this.rooms;
	}

	public void setId(String newId)
	{
		this.id = newId;
	}

	public void setReservationDate(Timestamp newDate)
	{
		this.reservationDate = newDate;
	}

	public void arrivalDate(Timestamp newDate)
	{
		this.arrivalDate = newDate;
	}

	public void departureDate(Timestamp newDate)
	{
		this.departureDate = newDate;
	}

	public void numberOfOccupants(int newNumber)
	{
		this.numberOfOccupants = newNumber;
	}

	public void setRooms(ArrayList<Room> rooms)
	{
		this.rooms = rooms;
	}

	@Override
	public String toString()
	{
		return String.format("%s, %s, %s, %s, %d", id, reservationDate, arrivalDate, departureDate, numberOfOccupants);
	}
}
