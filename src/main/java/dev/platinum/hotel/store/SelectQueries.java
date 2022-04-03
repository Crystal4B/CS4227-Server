package dev.platinum.hotel.store;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dev.platinum.hotel.types.Guest;
import dev.platinum.hotel.types.Reservation;
import dev.platinum.hotel.types.Room;
import dev.platinum.hotel.types.User;
import dev.platinum.hotel.types.Voucher;


/**
 * Store class for hanadling select queries
 * @author Marcin Sęk
 */
public class SelectQueries extends StoreComponent
{
	/**
	 * Function for selecting a Reservation by its id
	 * @param reservationId the ID of the reservation
	 * @return a Reservation object if a reservation was found, null otherwise
	 */
	public static Reservation selectReservationById(int reservationId)
	{
		try
		{
			Statement statement = connection.createStatement();
			String selectReservation = "SELECT * FROM " + RESERVATIONS_TABLE_NAME + " INNER JOIN " + USERS_TABLE_NAME + " ON " + USERS_TABLE_NAME + "." + ID_COLUMN + "=" + RESERVATIONS_TABLE_NAME + "." + USER_ID_COLUMN + " WHERE " + RESERVATIONS_TABLE_NAME + "." + ID_COLUMN + " = " + reservationId;
			ResultSet results = statement.executeQuery(selectReservation);
			if (results.next())
			{
				int id = results.getInt(ID_COLUMN);

				// results.getTimestamp("column_name") returns 1970-01-01
				Timestamp checkIn = Timestamp.valueOf((String) results.getObject(CHECK_IN_COLUMN));
				Timestamp checkOut = Timestamp.valueOf((String) results.getObject(CHECK_OUT_COLUMN));

				int userId = results.getInt(USER_ID_COLUMN);
				String type = results.getString(TYPE_COLUMN);
				String email = results.getString(EMAIL_COLUMN);
				String username = results.getString(USERNAME_COLUMN);
				User user = new User(userId, type, email, username);
				
				String guestIds = results.getString(GUEST_IDS_COLUMN);
				int guestIdsArr[] = Arrays.stream(guestIds.split(",")).mapToInt(Integer::parseInt).toArray();
				List<Guest> guests = selectGuestsByIds(guestIdsArr);

				Reservation reservation = new Reservation(id, checkIn, checkOut, user, guests);
				
				return reservation;
			}
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
		return null;
	}

	/**
	 * Function for selecting a Room by its id
	 * @param roomId the ID of the room
	 * @return a Room object if a room was found, null otherwise
	 */
	public static Room selectRoomById(int roomId)
	{
		try
		{
			Statement statement = connection.createStatement();
			String selectRoom = "SELECT * FROM + " + ROOMS_TABLE_NAME + " WHERE " + ID_COLUMN + " = " + roomId;
			ResultSet results = statement.executeQuery(selectRoom);
			if (results.next())
			{
				int id = results.getInt(ID_COLUMN);
				String type = results.getString(TYPE_COLUMN);
				String perks = results.getString(PERKS_COLUMN);
				int numberOfBeds = results.getInt(NUMBER_OF_BEDS_COLUMN);
				int rate = results.getInt(RATE_COLUMN);

				Room room = new Room(id, type, perks, numberOfBeds, rate);
				
				return room;
			}
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
		return null;
	}
	
	/**
	 * Function for selecting a Voucher by its id
	 * @param voucherId the ID of the room
	 * @return a Voucher object if a voucher was found, null otherwise
	 */
	public static Voucher selectVoucherById(int voucherId)
	{
		try
		{
			Statement statement = connection.createStatement();
			String selectVoucher = "SELECT * FROM " + VOUCHER_TABLE_NAME + " INNER JOIN " + USERS_TABLE_NAME + " ON " + USERS_TABLE_NAME + "." + ID_COLUMN + "=" + VOUCHER_TABLE_NAME + "." + USER_ID_COLUMN + " WHERE " + VOUCHER_TABLE_NAME + "." + ID_COLUMN + " = " + voucherId;
			ResultSet results = statement.executeQuery(selectVoucher);
			if (results.next())
			{
				int id = results.getInt(ID_COLUMN);
				String type = results.getString(TYPE_COLUMN);
				double amount = results.getDouble(AMOUNT_COLUMN);
				Timestamp issue_date = results.getTimestamp(ISSUE_DATE_COLUMN);
				Timestamp expiry_date = results.getTimestamp(EXPIRY_DATE_COLUMN) ;
				
				int userId = results.getInt(USER_ID_COLUMN);
				String typeU = results.getString(TYPE_COLUMN);
				String email = results.getString(EMAIL_COLUMN);
				String username = results.getString(USERNAME_COLUMN);
				User creator = new User(userId, typeU, email, username);

				int reservationId = results.getInt(RESERVATION_ID_COLUMN);

				Reservation available = new Reservation(reservationId);

				Voucher voucherBoi = new Voucher(id,issue_date, expiry_date, type, amount, creator, available);
			
				return voucherBoi;
			}
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
		return null;
	}

	/**
	 * Function for selecting multiple Rooms by their ids
	 * @param ids list of room IDs being selected
	 * @return A list containing all the Room Objects found
	 */
	public static List<Room> selectRoomsByIds(int ids[])
	{
		String selectRooms = "SELECT * FROM " + ROOMS_TABLE_NAME + " WHERE " + ID_COLUMN + " IN(";
		for (int i = 0; i < ids.length; i++) 
		{
			selectRooms += ids[i];
			if (i < ids.length - 1)
			{
				selectRooms += ",";
			}
		}
		selectRooms += ")";

		try
		{
			Statement statement = connection.createStatement();
			ResultSet results = statement.executeQuery(selectRooms);

			List<Room> rooms = new ArrayList<>();
			while (results.next())
			{
				int id = results.getInt(ID_COLUMN);
				String type = results.getString(TYPE_COLUMN);
				String perks = results.getString(PERKS_COLUMN);
				int numberOfBeds = results.getInt(NUMBER_OF_BEDS_COLUMN);
				int rate = results.getInt(RATE_COLUMN);

				rooms.add(new Room(id, type, perks, numberOfBeds, rate));
			}
			return rooms;
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}

		return null;
	}

	/**
	 * Function for selecting all available rooms using a start and end date
	 * @param checkIn The beginning of the range
	 * @param checkOut The End of the range
	 * @return A list containing all the Room Objects found
	 */
	public static List<Room> selectAvailableRoomsByDates(Timestamp checkIn, Timestamp checkOut)
	{
		List<Room> unavailableRooms = selectOccupiedRoomIdsByDates(checkIn, checkOut);

		String selectAvailableRooms = "SELECT * FROM " + ROOMS_TABLE_NAME + " WHERE " + ID_COLUMN + " NOT IN (";
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
			List<Room> availableRooms = new ArrayList<>();
			while (results.next())
			{
				int id = results.getInt(ID_COLUMN);
				String type = results.getString(TYPE_COLUMN);
				String perks = results.getString(PERKS_COLUMN);
				int numberOfBeds = results.getInt(NUMBER_OF_BEDS_COLUMN);
				int rate = results.getInt(RATE_COLUMN);

				availableRooms.add(new Room(id, type, perks, numberOfBeds, rate));
			}
			return availableRooms;
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}

		return null;
	}

	/**
	 * Function for selecting all unavailable rooms using a start and end date
	 * @param checkIn The beginning of the range
	 * @param checkOut The End of the range
	 * @return A list containing all the Room Objects found
	 */
	public static List<Room> selectOccupiedRoomIdsByDates(Timestamp checkIn, Timestamp checkOut)
	{
		try
		{
			String selectRoomIds = "SELECT " + GUEST_IDS_COLUMN + " FROM " + RESERVATIONS_TABLE_NAME + " WHERE " + CHECK_IN_COLUMN + " >= datetime('" + checkIn + "') AND " + CHECK_OUT_COLUMN + " <= datetime('" + checkOut + "')";

			Statement statement = connection.createStatement();
			ResultSet results = statement.executeQuery(selectRoomIds);

			String guestIds = "";
			while (results.next())
			{
				guestIds += results.getString(GUEST_IDS_COLUMN);
			}
			if (guestIds == "")
			{
				return new ArrayList<>();
			}

			int ids[] = Arrays.stream(guestIds.split(",")).mapToInt(Integer::parseInt).toArray();

			return selectOccupiedRoomsByGuestIds(ids);
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}

		return new ArrayList<>();
	}

	/**
	 * Functioon for selecting all rooms occupied by specific guests
	 * @param ids the list of guest ids
	 * @return A list containing all the Room objects found
	 */
	public static List<Room> selectOccupiedRoomsByGuestIds(int ids[])
	{
		String selectRoomsByGuests = "SELECT " + ROOM_ID_COLUMN + "," + TYPE_COLUMN + "," + PERKS_COLUMN + "," + NUMBER_OF_BEDS_COLUMN + "," + RATE_COLUMN + " FROM " + GUESTS_TABLE_NAME + " INNER JOIN " + ROOMS_TABLE_NAME + " ON " + ROOMS_TABLE_NAME + "." + ID_COLUMN + "=" + GUESTS_TABLE_NAME + "." + ROOM_ID_COLUMN + " WHERE " + GUESTS_TABLE_NAME + "." + ID_COLUMN + " IN (";
		for (int i = 0; i < ids.length; i++)
		{
			selectRoomsByGuests += ids[i];
			if (i < ids.length - 1)
			{
				selectRoomsByGuests += ",";
			}
		}
		selectRoomsByGuests += ")";

		try
		{
			Statement statement = connection.createStatement();
			ResultSet results = statement.executeQuery(selectRoomsByGuests);

			List<Room> rooms = new ArrayList<>();
			while (results.next())
			{
				int roomId = results.getInt(ROOM_ID_COLUMN);
				String type = results.getString(TYPE_COLUMN);
				String perks = results.getString(PERKS_COLUMN);
				int numberOfBeds = results.getInt(NUMBER_OF_BEDS_COLUMN);
				int rate = results.getInt(RATE_COLUMN);

				rooms.add(new Room(roomId, type, perks, numberOfBeds, rate));
			}

			return rooms;
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}

		return null;
	}

	/**
	 * Function for selecting a user by their id
	 * @param id the id of the user being selected
	 * @return A user object if a user was found
	 */
	public static User selectUserById(int id)
	{
		String selectUser = "SELECT * FROM " + USERS_TABLE_NAME + " WHERE " + ID_COLUMN + " = " + id;

		try
		{
			Statement statement = connection.createStatement();
			ResultSet results = statement.executeQuery(selectUser);

			if (results.next())
			{
				String type = results.getString(TYPE_COLUMN);
				String username = results.getString(USERNAME_COLUMN);
				
				return new User(id, type, username);
			}
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}

		return null;
	}

	/**
	 * Function for selecting guests by their ids
	 * @param ids the list of guest ids
	 * @return A list containing all the Guest objects found
	 */
	public static List<Guest> selectGuestsByIds(int ids[])
	{
		String selectRoomsByGuests = "SELECT * FROM " + GUESTS_TABLE_NAME + " INNER JOIN " + ROOMS_TABLE_NAME + " ON " + ROOMS_TABLE_NAME + "." + ID_COLUMN + "=" + GUESTS_TABLE_NAME + "." + ROOM_ID_COLUMN + " WHERE " + GUESTS_TABLE_NAME + "." + ID_COLUMN + " IN (";
		for (int i = 0; i < ids.length; i++)
		{
			selectRoomsByGuests += ids[i];
			if (i < ids.length - 1)
			{
				selectRoomsByGuests += ",";
			}
		}
		selectRoomsByGuests += ")";

		try
		{
			Statement statement = connection.createStatement();
			ResultSet results = statement.executeQuery(selectRoomsByGuests);

			List<Guest> guests = new ArrayList<>();
			while (results.next())
			{
				int id = results.getInt(ID_COLUMN);
				String firstName = results.getString(FIRST_NAME_COLUMN);
				String lastName = results.getString(LAST_NAME_COLUMN);

				int roomId = results.getInt(ROOM_ID_COLUMN);
				String type = results.getString(TYPE_COLUMN);
				String perks = results.getString(PERKS_COLUMN);
				int numberOfBeds = results.getInt(NUMBER_OF_BEDS_COLUMN);
				int rate = results.getInt(RATE_COLUMN);
				Room occupiedRoom = new Room(roomId, type, perks, numberOfBeds, rate);

				guests.add(new Guest(id, firstName, lastName, occupiedRoom));
			}

			return guests;
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}

		return null;
	}

	/**
	 * Function for selecting a user using its login details
	 * @param user attempting to login
	 * @return A User object if one has been found
	 */
	public static User selectUserByLogin(User user)
	{
		try
		{
			String selectUser = "SELECT " + ID_COLUMN + "," + TYPE_COLUMN + "," + USERNAME_COLUMN + " FROM " + USERS_TABLE_NAME + " WHERE " + EMAIL_COLUMN + " = '" + user.getEmail() + "' AND " + PASSWORD_COLUMN + " = '" + user.getPassword() + "'";

			Statement statement = connection.createStatement();
			ResultSet results = statement.executeQuery(selectUser);
			if (results.next())
			{
				int id = results.getInt(ID_COLUMN);
				String type = results.getString(TYPE_COLUMN);
				String username = results.getString(USERNAME_COLUMN);

				user.setId(id);
				user.setType(type);
				user.setUsername(username);
				
				return user;
			}
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
		return null;
	}

	/**
	 * Query for selecting all rooms in the database
	 * @return list of rooms in the database
	 */
	public static List<Room> selectAllRooms()
	{
		String selectAllRooms = "SELECT * FROM " + ROOMS_TABLE_NAME;

		try
		{
			Statement statement = connection.createStatement();
			ResultSet results = statement.executeQuery(selectAllRooms);

			List<Room> rooms = new ArrayList<>();
			while (results.next())
			{
				int roomId = results.getInt(ID_COLUMN);
				String type = results.getString(TYPE_COLUMN);
				String perks = results.getString(PERKS_COLUMN);
				int numberOfBeds = results.getInt(NUMBER_OF_BEDS_COLUMN);
				int rate = results.getInt(RATE_COLUMN);

				rooms.add(new Room(roomId, type, perks, numberOfBeds, rate));
			}
			
			return rooms;
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}

		return null;
	}

	/**
	 * Function for validating is an email address is available for creating a user
	 * @param email address used in registration process
	 * @return boolean value describing availability
	 */
	public static boolean checkEmailAvailablity(String email)
	{
		String selectUserByEmail = "SELECT " + EMAIL_COLUMN + " FROM " + USERS_TABLE_NAME + " WHERE " + EMAIL_COLUMN + "='" + email + "'";
		try
		{
			Statement statement = connection.createStatement();
			ResultSet results = statement.executeQuery(selectUserByEmail);

			return !results.next(); // empty results = available
		}
		catch(SQLException e)
		{
			System.out.println(e);
		}

		return false;
	}

	/**
	 * Query for checking if guests exist
	 * @param guests list of guests to be checked
	 * @return updated list of guests with their database ids
	 */
	public static List<Guest> checkGuestExistance(List<Guest> guests)
	{
		String selectGuestsByLegalName = "SELECT * FROM " + GUESTS_TABLE_NAME + " WHERE (" + FIRST_NAME_COLUMN + " || ' ' || " + LAST_NAME_COLUMN + ") IN (";
		for (int i = 0; i < guests.size(); i++)
		{
			Guest guest = guests.get(i);
			selectGuestsByLegalName += "'" + guest.getFirstName() + " " + guest.getLastName() + "'";
			
			if (i < guests.size()-1)
			{
				selectGuestsByLegalName += ",";
			}
		}
		selectGuestsByLegalName += ")";

		try
		{
			Statement statement = connection.createStatement();
			ResultSet results = statement.executeQuery(selectGuestsByLegalName);

			List<Guest> resultList = new ArrayList<>();
			while (results.next())
			{
				int id = results.getInt(ID_COLUMN);
				String firstName = results.getString(FIRST_NAME_COLUMN);
				String lastName = results.getString(LAST_NAME_COLUMN);
				int roomId = results.getInt(ROOM_ID_COLUMN);

				resultList.add(new Guest(id, firstName, lastName, new Room(roomId)));
			}

			return resultList;
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}

		return new ArrayList<>();
	}

	/**
	 * Query for selecting a list of reservations that are visible to a specific user
	 * @param user the user trying to see reservations
	 * @return the list of reservations visible
	 */
    public static List<Reservation> selectReservationsByUser(User user)
	{
		String selectReservationsByUser = "SELECT * FROM " + RESERVATIONS_TABLE_NAME + " INNER JOIN " + USERS_TABLE_NAME + " ON " + USERS_TABLE_NAME + "." + ID_COLUMN + "=" + RESERVATIONS_TABLE_NAME + "." + USER_ID_COLUMN;
		if (!user.getType().equals("Staff"))
		{
			selectReservationsByUser += " WHERE " + USER_ID_COLUMN + "=" + user.getId(); 
		}

		try
		{
			Statement statement = connection.createStatement();
			ResultSet results = statement.executeQuery(selectReservationsByUser);

			List<Reservation> reservations = new ArrayList<>();
			while (results.next())
			{
				int id = results.getInt(ID_COLUMN);

				// results.getTimestamp("column_name") returns 1970-01-01
				Timestamp checkIn = Timestamp.valueOf((String) results.getObject(CHECK_IN_COLUMN));
				Timestamp checkOut = Timestamp.valueOf((String) results.getObject(CHECK_OUT_COLUMN));

				int userId = results.getInt(USER_ID_COLUMN);
				String type = results.getString(TYPE_COLUMN);
				String username = results.getString(USERNAME_COLUMN);
				String email = results.getString(EMAIL_COLUMN);

				User resultUser = new User(userId, type, email, username);
				
				String guestIds = results.getString(GUEST_IDS_COLUMN);
				int guestIdsArr[] = Arrays.stream(guestIds.split(",")).mapToInt(Integer::parseInt).toArray();
				List<Guest> guests = selectGuestsByIds(guestIdsArr);

				Reservation reservation = new Reservation(id, checkIn, checkOut, resultUser, guests);
				reservations.add(reservation);
			}
			return reservations;
		}
		catch(SQLException e)
		{
			System.out.println(e);
		}

        return new ArrayList<>();
    }
}
