package dev.platinum.hotel;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import com.google.common.io.Resources;

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
			URL url = Resources.getResource("database.db");
			String path = new File(url.toURI()).getAbsolutePath();
	
			DriverManager.registerDriver(new org.sqlite.JDBC());
			connection = DriverManager.getConnection("jdbc:sqlite:" + path);
			createTables();
		}
		catch(URISyntaxException | SQLException e)
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
			String reservationTable = "CREATE TABLE IF NOT EXISTS reservations(id INT PRIMARY KEY, reservation_date DATETIME, arrival_date DATETIME, departure_date DATETIME, number_of_occupants INT, room_ids TEXT)"; // Room_ids as text in format "id,id,id"
			String roomTable = "CREATE TABLE IF NOT EXISTS rooms(id INT PRIMARY KEY, rate INT)";
	
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

	public static Object selectReservationById(String reservationId)
	{
		try
		{
			Statement statement = connection.createStatement();
			String selectReservation = String.format("SELECT * FROM reservations WHERE id = %s", reservationId);
			ResultSet results = statement.executeQuery(selectReservation);
			System.out.println(results.toString());
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
		return null;
	}

	public static int insertReservation(LocalDate reservationDate, LocalDate arrivalDate, LocalDate departureDate, int numberOfOccupants)
	{
		try
		{
			Statement statement = connection.createStatement();
			String insertReservation = String.format("INSERT INTO reservations(reservation_date, arrival_date, departure_date, number_of_occupants) VALUES(%s, %s, %s, %d)", reservationDate
			, arrivalDate, departureDate, numberOfOccupants);

			statement.executeUpdate(insertReservation, Statement.RETURN_GENERATED_KEYS);
			ResultSet keys = statement.getGeneratedKeys();
			System.out.println(keys);
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
		return 1;
	}
}
