package dev.platinum.hotel;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.TreeMap;

import com.google.common.io.Resources;

/**
 * The store class is made for managing the data being stored on the server
 * @author Marcin Sęk
 */
public class Store
{
	private static Connection connection;
	
	/**
	 * Function for initialising the Store
	 */
	public static void init()
	{
		try
		{
			URL url = Resources.getResource("database.db");
			String path = new File(url.toURI()).getAbsolutePath();
	
			connection = DriverManager.getConnection(path);
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
			String reservationTable = "CREATE TABLE IF NOT EXISTS reservations(id INT PRIMARY KEY, reservation_date DATETIME, arrival_date DATETIME, departure_date, number_of_occupants INT, room_ids TEXT)"; // Room_ids as text in format "id,id,id"
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

	public static Object selectReservationById(int reservationId)
	{
		return null;
	}

	public static int insertReservation()
	{
		return 1;
	}
}
