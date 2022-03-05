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

import dev.platinum.hotel.types.Reservation;
import dev.platinum.hotel.types.Room;
import dev.platinum.hotel.types.User;

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
			String reservationTable = "CREATE TABLE IF NOT EXISTS reservations(id INTEGER PRIMARY KEY AUTOINCREMENT, arrival_date DATETIME NOT NULL, departure_date DATETIME NOT NULL, room_ids TEXT NOT NULL)"; // Room_ids as text in format "id,id,id"
			String roomTable = "CREATE TABLE IF NOT EXISTS rooms(id INTEGER PRIMARY KEY AUTOINCREMENT, type TEXT NOT NULL, name TEXT NOT NULL, perks TEXT NOT NULL, number_of_beds INT NOT NULL, rate INT NOT NULL, user_ids TEXT NOT NULL)"; // user_ids as text in format "id,id,id"
			String userTable = "CREATE TABLE IF NOT EXISTS users(id INTEGER PRIMARY KEY AUTOINCREMENT, type TEXT NOT NULL, email TEXT NOT NULL, username TEXT NOT NULL, password TEXT NOT NULL)";

			statement.addBatch(reservationTable);
			statement.addBatch(roomTable);
			statement.addBatch(userTable);
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
				Timestamp arrivalDate = Timestamp.valueOf((String) results.getObject("arrival_date"));
				Timestamp departureDate = Timestamp.valueOf((String) results.getObject("departure_date"));
				String roomIds = results.getString("room_ids");
				String roomIdsArr[] = roomIds.split(",");
				ArrayList<Room> rooms = selectRoomsByIds(roomIdsArr);

				Reservation reservation = new Reservation(id, arrivalDate, departureDate, rooms);
				
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
				String userIds = results.getString("user_ids");
				String ids[] = userIds.split(",");
				ArrayList<User> occupants = selectUsersByIds(ids);

				Room room = new Room(id, name, perks, numberOfBeds, rate, occupants);
				
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
				String type = results.getString("type");
				String name = results.getString("name");
				String perks = results.getString("perks");
				int numberOfBeds = results.getInt("number_of_beds");
				int rate = results.getInt("rate");
				String userIds = results.getString("user_ids");
				String ids[] = userIds.split(",");
				ArrayList<User> occupants = selectUsersByIds(ids);
				rooms.add(new Room(id, type, name, perks, numberOfBeds, rate, occupants));
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
				String type = results.getString("type");
				String name = results.getString("name");
				String perks = results.getString("perks");
				int numberOfBeds = results.getInt("number_of_beds");
				int rate = results.getInt("rate");
				String userIds = results.getString("user_ids");
				String ids[] = userIds.split(",");
				ArrayList<User> occupants = selectUsersByIds(ids);

				availableRooms.add(new Room(id, type, name, perks, numberOfBeds, rate, occupants));
			}
			return availableRooms;
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}

		return null;
	}

	private static ArrayList<User> selectUsersByIds(String[] ids)
	{
		try
		{
			String selectUsers = "SELECT * FROM users WHERE id IN (";
			for (int i = 0; i < ids.length; i++)
			{
				selectUsers += ids[i];
				if (i < ids.length -1)
				{
					selectUsers += ",";
				}
			}
			selectUsers += ")";
	
			Statement statement = connection.createStatement();
			ResultSet results = statement.executeQuery(selectUsers);

			ArrayList<User> users = new ArrayList<>();
			while(results.next())
			{
				String id = results.getString("id");
				String type = results.getString("type");
				String email = results.getString("email");

				users.add(new User(id, type, email));
			}

			return users;
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

	public static User selectUserByLogin(User user)
	{
		try
		{
			String selectUser = String.format("SELECT id, type, username FROM users WHERE email='%s' AND password='%s'", user.getEmail(), user.getPassword());

			Statement statement = connection.createStatement();
			ResultSet results = statement.executeQuery(selectUser);
			if (results.next())
			{
				String id = String.valueOf(results.getInt("id"));
				String type = results.getString("type");
				String username = results.getString("username");

				user.setId(id);
				user.setType(type);
				user.setUsername(username);
				
				return user;
			}
		}
		catch (SQLException e)
		{
			System.out.println(e + " SELECT ");
		}
		return null;
	}

	public static Reservation insertReservation(Reservation incomingReservation)
	{
		try
		{
			String roomIds = "";
			for (int i = 0; i < incomingReservation.getRooms().size(); i++)
			{
				roomIds += incomingReservation.getRooms().get(i).getId();
				if (i < incomingReservation.getRooms().size() -1)
				{
					roomIds += ",";
				}
			}

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			Statement statement = connection.createStatement();
			String insertReservation = String.format("INSERT INTO reservations(arrival_date, departure_date, room_ids) VALUES('%s', '%s', '%s')", sdf.format(incomingReservation.arrivalDate()), sdf.format(incomingReservation.departureDate()), roomIds);

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
				ArrayList<User> occupants = room.getOccupants();
				String user_ids = "";
				for (int j = 0; j < occupants.size(); j++)
				{
					User occupant = occupants.get(j);
					user_ids += occupant.getId();
					if (j < occupants.size()-1)
					{
						user_ids += ",";
					}
				}

				String insertRooms = String.format("INSERT INTO 'rooms'('type', 'name', 'perks', 'number_of_beds', 'rate', 'user_ids') VALUES('%s', '%s', '%s', %d, %d, '%s')", room.getType(), room.getName(), room.getPerks(), room.getNumberOfBeds(), room.getRate(), user_ids);
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

	public static User insertUser(User user)
	{
		try
		{
			String insertUser = String.format("INSERT INTO 'users'('type', 'email', 'username', 'password') VALUES('%s', '%s', '%s', '%s')", user.getType(), user.getEmail(), user.getUsername(), user.getPassword());

			Statement statement = connection.createStatement();
			statement.execute(insertUser);
			connection.commit();

			ResultSet keys = statement.getGeneratedKeys();
			if (keys.next())
			{
				user.setId(String.valueOf(keys.getLong(1)));
				return user;
			}
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}

		return null;
	}

	public static User deleteUser(User user)
	{
		try
		{
			String deleteUser = String.format("DELETE FROM users WHERE id='%s' RETURNING *", user.getId());

			Statement statement = connection.createStatement();
			ResultSet results = statement.executeQuery(deleteUser);

			User resultUser = null;
			if (results.next())
			{
				String id = results.getString("id");
				String type = results.getString("type");
				String email = results.getString("email");

				resultUser = new User(id, type, email);
			}
			results.close();
			connection.commit();
			return resultUser;

		}
		catch (SQLException e)
		{
			System.out.println(e);
		}

		return null;
	}

	public static ArrayList<Room> deleteRooms(ArrayList<Room> rooms)
	{
		try
		{
			String deleteRooms = "DELETE FROM users WHERE id IN (";
			for (int i = 0; i < rooms.size(); i++)
			{
				Room room = rooms.get(i);
				deleteRooms += "'" + room.getId() + "'";
				if (i < rooms.size() -1)
				{
					deleteRooms += ",";
				}
			}
			deleteRooms += ") RETURNING *";

			System.out.println(deleteRooms);

			Statement statement = connection.createStatement();
			ResultSet results = statement.executeQuery(deleteRooms);
			
			ArrayList<Room> deletedRooms = new ArrayList<>();
			while (results.next())
			{
				String id = String.valueOf(results.getInt("id"));
				String type = results.getString("type");
				String name = results.getString("name");
				String perks = results.getString("perks");
				int numberOfBeds = results.getInt("number_of_beds");
				int rate = results.getInt("rate");
				String userIds = results.getString("user_ids");
				String ids[] = userIds.split(",");
				ArrayList<User> occupants = selectUsersByIds(ids);
				deletedRooms.add(new Room(id, type, name, perks, numberOfBeds, rate, occupants));
			}
			results.close();
			connection.commit();

			return deletedRooms;
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}

		return null;
	}

	public static Reservation deleteReservation(String id)
	{
		try
		{
			String deleteReservation = String.format("DELETE FROM users WHERE id='%s' RETURNING *", id);

			Statement statement = connection.createStatement();
			ResultSet results = statement.executeQuery(deleteReservation);
			
			Reservation resultReservation = null;
			if (results.next())
			{
				String deletedId = String.valueOf(results.getInt("id"));
				// results.getTimestamp("column_name") returns 1970-01-01
				Timestamp arrivalDate = Timestamp.valueOf((String) results.getObject("arrival_date"));
				Timestamp departureDate = Timestamp.valueOf((String) results.getObject("departure_date"));
				String roomIds = results.getString("room_ids");
				String roomIdsArr[] = roomIds.split(",");
				ArrayList<Room> rooms = selectRoomsByIds(roomIdsArr);

				resultReservation = new Reservation(deletedId, arrivalDate, departureDate, rooms);
			}
			results.close();
			connection.commit();

			return resultReservation;
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}

		return null;
	}
}
