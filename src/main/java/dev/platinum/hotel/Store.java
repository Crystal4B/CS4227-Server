package dev.platinum.hotel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import com.ibm.icu.text.SimpleDateFormat;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * The store class is made for managing the data being stored on the server
 * @author Marcin Sęk
 */
@Component
public class Store
{
	private static Connection connection;
	
	@Bean
	public static void init()
	{
		try
		{
			DriverManager.registerDriver(new org.sqlite.JDBC());
			connection = DriverManager.getConnection("jdbc:sqlite:db.sqlite");
			connection.setAutoCommit(false);
			createTables();
		}
		catch(SQLException e)
		{
			System.out.println(e.toString());
		}
	}

	/**
	 * Function for setting up the sql tables if required
	 */
	private static void createTables()
	{
		try
		{
			Statement statement = connection.createStatement();
			String reservationTable = "CREATE TABLE IF NOT EXISTS reservations(id INTEGER PRIMARY KEY AUTOINCREMENT, reservation_date DATETIME, arrival_date DATETIME, departure_date DATETIME, number_of_occupants INT, room_ids TEXT)"; // Room_ids as text in format "id,id,id"
			String roomTable = "CREATE TABLE IF NOT EXISTS rooms(id INTEGER PRIMARY KEY AUTOINCREMENT, rate INT)";
	
			statement.addBatch(reservationTable);
			statement.addBatch(roomTable);
			statement.executeBatch();

			connection.commit();
		}
		catch(SQLException e)
		{
			System.out.println(e.toString());
		}
	}

	public static Reservation selectReservationById(String reservationId)
	{
		try
		{
			Statement statement = connection.createStatement();
			String selectReservation = String.format("SELECT * FROM reservations WHERE id = %s", reservationId);
			ResultSet results = statement.executeQuery(selectReservation);
			if (results.next())
			{
				String id = String.valueOf(results.getInt("id"));
				// results.getTimestamp("column_name") returns 1970-01-01
				Timestamp reservationDate = Timestamp.valueOf((String) results.getObject("reservation_date"));
				Timestamp arrivalDate = Timestamp.valueOf((String) results.getObject("arrival_date"));
				Timestamp departureDate = Timestamp.valueOf((String) results.getObject("departure_date"));
				int numberOfOccupants = results.getInt("number_of_occupants");

				Reservation reservation = new Reservation(id, reservationDate, arrivalDate, departureDate, numberOfOccupants);
				
				return reservation;
			}
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
		return null;
	}

	public static Reservation insertReservation(Reservation incomingReservation)
	{
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			Statement statement = connection.createStatement();
			String insertReservation = String.format("INSERT INTO reservations(reservation_date, arrival_date, departure_date, number_of_occupants) VALUES('%s', '%s', '%s', %d)", sdf.format(incomingReservation.getReservationDate()), sdf.format(incomingReservation.arrivalDate()), sdf.format(incomingReservation.departureDate()), incomingReservation.numberOfOccupants());

			statement.execute(insertReservation);
			connection.commit();

			ResultSet keys = statement.getGeneratedKeys();
			if (keys.next())
			{
				incomingReservation.setId(String.valueOf(keys.getLong(1)));
				return incomingReservation;
			}
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
		return null;
	}
}
