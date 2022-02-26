package dev.platinum.hotel;

import java.sql.Timestamp;

/**
 * The Reservation class for parsing and dispatching reservation queries & mutations
 * @author Marcin Sęk
 */
public class Reservation
{
	public String id;
	public Timestamp reservationDate;
	public Timestamp arrivalDate;
	public Timestamp departureDate;
	public int numberOfOccupants;

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

	@Override
	public String toString()
	{
		return String.format("%s, %s, %s, %s, %d", id, reservationDate, arrivalDate, departureDate, numberOfOccupants);
	}
}
