package dev.platinum.hotel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
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
			String roomTable = "CREATE TABLE IF NOT EXISTS rooms(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, perks TEXT, number_of_beds INT, rate INT)";
	
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
				String roomIds = results.getString("room_ids");
				String roomIdsArr[] = roomIds.split(",");
				ArrayList<Room> rooms = selectRoomsByIds(roomIdsArr);

				Reservation reservation = new Reservation(id, reservationDate, arrivalDate, departureDate, numberOfOccupants, rooms);
				
				return reservation;
			}
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
		return null;
	}

	public static Room selectRoomById(String roomId)
	{
		try
		{
			Statement statement = connection.createStatement();
			String selectRoom = String.format("SELECT * FROM rooms WHERE id = %s", roomId);
			ResultSet results = statement.executeQuery(selectRoom);
			if (results.next())
			{
				String id = String.valueOf(results.getInt("id"));
				String name = results.getString("name");
				String perks = results.getString("perks");
				int numberOfBeds = results.getInt("number_of_beds");
				int rate = results.getInt("rate");

				Room room = new Room(id, name, perks, numberOfBeds, rate);
				
				return room;
			}
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
		return null;
	}

	public static ArrayList<Room> selectRoomsByIds(String[] roomIdsArr)
	{
		String selectRooms = "SELECT * FROM rooms WHERE id IN(";
		for (int i = 0; i < roomIdsArr.length; i++) 
		{
			selectRooms += roomIdsArr[i];
			if (i < roomIdsArr.length - 1)
			{
				selectRooms += ",";
			}
		}
		selectRooms += ")";

		try
		{
			Statement statement = connection.createStatement();
			ResultSet results = statement.executeQuery(selectRooms);

			ArrayList<Room> rooms = new ArrayList<>();
			while (results.next())
			{
				String id = String.valueOf(results.getInt("id"));
				String name = results.getString("name");
				String perks = results.getString("perks");
				int numberOfBeds = results.getInt("number_of_beds");
				int rate = results.getInt("rate");
				rooms.add(new Room(id, name, perks, numberOfBeds, rate));
			}
			return rooms;
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}

		return null;
	}

	public static ArrayList<Room> selectAvailableRoomsByDates(Timestamp selectedArrival, Timestamp selectedDeparture)
	{
		ArrayList<Room> unavailableRooms = selectUnavailableRoomIdsByDates(selectedArrival, selectedDeparture);

		String selectAvailableRooms = "SELECT * FROM rooms WHERE id NOT IN (";
		for (int i = 0; i < unavailableRooms.size(); i++) 
		{
			selectAvailableRooms += unavailableRooms.get(i).getId();
			if (i < unavailableRooms.size() - 1)
			{
				selectAvailableRooms += ",";
			}
		}
		selectAvailableRooms += ")";

		try
		{
			Statement statement = connection.createStatement();
			ResultSet results = statement.executeQuery(selectAvailableRooms);
			ArrayList<Room> availableRooms = new ArrayList<>();
			while (results.next())
			{
				String id = String.valueOf(results.getInt("id"));
				String name = results.getString("name");
				String perks = results.getString("perks");
				int numberOfBeds = results.getInt("number_of_beds");
				int rate = results.getInt("rate");

				availableRooms.add(new Room(id, name, perks, numberOfBeds, rate));
			}
			return availableRooms;
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}

		return null;
	}

	public static ArrayList<Room> selectUnavailableRoomIdsByDates(Timestamp selectedArrival, Timestamp selectedDeparture)
	{
		ArrayList<Room> rooms = new ArrayList<>();

		try
		{
			String selectRoomIds = String.format("SELECT room_ids FROM reservations WHERE arrival_date >= datetime('%s') AND departure_date <= datetime('%s')", selectedDeparture, selectedArrival);

			Statement statement = connection.createStatement();
			ResultSet results = statement.executeQuery(selectRoomIds);
			String roomIds = "";
			while (results.next())
			{
				roomIds += results.getString("room_ids");
			}
			String ids[] = roomIds.split(",");

			for (String id : ids)
			{
				rooms.add(new Room(id));
			}
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}

		return rooms;
	}

	public static Reservation insertReservation(Reservation incomingReservation, ArrayList<Room> rooms2)
	{
		try
		{
			String roomIds = "";
			for (int i = 0; i < rooms2.size(); i++)
			{
				roomIds += rooms2.get(i).getId();
				if (i < rooms2.size() -1)
				{
					roomIds += ",";
				}
			}

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			Statement statement = connection.createStatement();
			String insertReservation = String.format("INSERT INTO reservations(reservation_date, arrival_date, departure_date, number_of_occupants, room_ids) VALUES('%s', '%s', '%s', %d, '%s')", sdf.format(incomingReservation.getReservationDate()), sdf.format(incomingReservation.arrivalDate()), sdf.format(incomingReservation.departureDate()), incomingReservation.numberOfOccupants(), roomIds);

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

	public static ArrayList<Room> insertRooms(ArrayList<Room> rooms)
	{
		try
		{
			Statement statement = connection.createStatement();

			for (int i = 0; i < rooms.size(); i++)
			{
				Room room = rooms.get(i);
				String insertRooms = String.format("INSERT INTO 'rooms'('name', 'perks', 'number_of_beds', 'rate') VALUES('%s', '%s', %d, %d)", room.getName(), room.getPerks(), room.getNumberOfBeds(), room.getRate());
				statement.addBatch(insertRooms);
			}

			statement.executeBatch();
			connection.commit();

			// generatedKeys holds last key inserted
			ResultSet keys = statement.getGeneratedKeys();
			int lastKey = -1;
			while (keys.next())
			{
				lastKey = (int) keys.getLong(1);
			}

			// as No IDs are specified in create query we can calculate generated IDS based on lastKey inserted and number of Rooms inserted
			for (int i = rooms.size() - 1; i >= 0; i--)
			{
				rooms.get(i).setId(String.valueOf(lastKey - (rooms.size() - 1 - i)));
			}
			return rooms;
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
		return null;
	}
}
