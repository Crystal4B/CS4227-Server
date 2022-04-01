package dev.platinum.hotel.store;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;

import dev.platinum.hotel.types.Guest;
import dev.platinum.hotel.types.Reservation;
import dev.platinum.hotel.types.Room;
import dev.platinum.hotel.types.User;
import dev.platinum.hotel.types.Voucher;

/**
 * Store class for hanadling insert queries
 * @author Marcin Sęk
 */
public class InsertQueries extends StoreComponent
{
	/**
	 * Function for inserting a reservation into the system
	 * @param incomingReservation the Reservation Object being added
	 * @return A Reservation object with its database ids
	 */
	public static Reservation insertReservation(Reservation incomingReservation)
	{
		List<Guest> incomingGuests = incomingReservation.getGuests();
		List<Guest> existingGuests = SelectQueries.checkGuestExistance(incomingGuests);
		List<Guest> newGuests = new ArrayList<>(); // Move to helper function
		for (int i = 0; i < incomingGuests.size(); i++)
		{
			Guest incomingGuest = incomingGuests.get(i);
			boolean exists = false;

			for (int j = 0; j < existingGuests.size(); j++)
			{
				Guest existingGuest = existingGuests.get(j);
				if (incomingGuest.getFirstName().equals(existingGuest.getFirstName()) && incomingGuest.getLastName().equals(existingGuest.getLastName())) 
				{
					incomingGuests.get(i).setId(existingGuest.getId()); // Set Id for return
					exists = true;
					break;
				}
			}

			if (!exists)
			{
				newGuests.add(incomingGuest);
			}
		}

		newGuests = InsertQueries.insertGuests(newGuests);
		for (int i = 0; i < newGuests.size(); i++)
		{
			Guest newGuest = newGuests.get(i);
			for (int j = 0; j < incomingGuests.size(); j++)
			{
				if (newGuest.getFirstName().equals(incomingGuests.get(j).getFirstName()) && newGuest.getLastName().equals(incomingGuests.get(j).getLastName()))
				{
					incomingReservation.getGuests().get(j).setId(newGuest.getId());
				}
			}
		}
		UpdateQueries.updateGuestsRoom(existingGuests);

		String guestIds = "";
		for (int i = 0; i < incomingReservation.getGuests().size(); i++)
		{
			guestIds += incomingReservation.getGuests().get(i).getId();
			if (i < incomingReservation.getGuests().size()-1)
			{
				guestIds += ",";
			}
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String insertReservation = "INSERT INTO " + RESERVATIONS_TABLE_NAME + "(" + CHECK_IN_COLUMN + "," + CHECK_OUT_COLUMN + "," + USER_ID_COLUMN + "," + GUEST_IDS_COLUMN + ") VALUES ('" + sdf.format(incomingReservation.getCheckIn()) + "', '" + sdf.format(incomingReservation.getCheckOut()) + "', " + incomingReservation.getUser().getId() + ",'" + guestIds + "')";

		try
		{
			Statement statement = connection.createStatement();

			statement.execute(insertReservation);
			connection.commit();

			ResultSet keys = statement.getGeneratedKeys();
			if (keys.next())
			{
				incomingReservation.setId((int) keys.getLong(1));
				return incomingReservation;
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
	public static Voucher createVoucher(Timestamp issue_d, Timestamp expiry_d, String temp, double num, User creatorUser)
	{
		try
		{
			Statement statement = connection.createStatement();
			String selectVoucher = "INSERT INTO " + VOUCHER_TABLE_NAME + "(" + ISSUE_DATE_COLUMN + "," + EXPIRY_DATE_COLUMN + "," + TYPE_COLUMN + "," + AMOUNT_COLUMN + "," + USER_ID_COLUMN + ") VALUES ('" + issue_d + "','" + expiry_d + "','" + temp + "'," + num + "," + creatorUser.getId() + ")";
			statement.addBatch(selectVoucher);
			statement.executeBatch();
			connection.commit();
			ResultSet keys = statement.getGeneratedKeys();
			int lastKey = -1;
			if (keys.next())
			{
				lastKey = (int) keys.getLong(1);
			}
			Voucher voucherBoi = new Voucher(lastKey, issue_d, expiry_d, temp, num, creatorUser);
			return voucherBoi;
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
		return null;
	}

	/**
	 * Query for inserting newGuests into the system
	 * @param newGuests list of guests to be inserted
	 * @return updated lists of guests 
	 */
	private static List<Guest> insertGuests(List<Guest> newGuests)
	{
		try
		{
			Statement statement = connection.createStatement();
			
			for (int i = 0; i < newGuests.size(); i++)
			{
				Guest newGuest = newGuests.get(i);

				String insertGuests = "INSERT INTO " + GUESTS_TABLE_NAME + "(" + FIRST_NAME_COLUMN + "," + LAST_NAME_COLUMN + "," + ROOM_ID_COLUMN + ") VALUES ('" + newGuest.getFirstName() + "','" + newGuest.getLastName() + "'," + newGuest.getRoom().getId() + ")";
				statement.addBatch(insertGuests);
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
			for (int i = newGuests.size() - 1; i >= 0; i--)
			{
				newGuests.get(i).setId(lastKey - (newGuests.size() - 1 - i));
			}
			return newGuests;
		}
		catch(SQLException e)
		{
			System.out.println(e);
		}

		return new ArrayList<>();
	}

	/**
	 * Function for inserting rooms into the system
	 * @param rooms a list of rooms being added to the system
	 * @return updated list of rooms where all Room objects got their database ids
	 */
	public static List<Room> insertRooms(List<Room> rooms)
	{
		try
		{
			Statement statement = connection.createStatement();

			for (int i = 0; i < rooms.size(); i++)
			{
				Room room = rooms.get(i);

				String insertRooms = "INSERT INTO " + ROOMS_TABLE_NAME + "(" + TYPE_COLUMN + "," + PERKS_COLUMN + "," + NUMBER_OF_BEDS_COLUMN + "," + RATE_COLUMN + ") VALUES('" + room.getType() + "', '" + room.getPerks() + "', '" + room.getNumberOfBeds() + "'," + room.getRate() + ")";
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
				rooms.get(i).setId(lastKey - (rooms.size() - 1 - i));
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
	 * Function for adding a user into the system
	 * @param user the user being added into the system
	 * @return a updated user object with its database id
	 */
	public static User insertUser(User user)
	{
		try
		{
			String insertUser = "INSERT INTO " + USERS_TABLE_NAME + "(" + TYPE_COLUMN + "," + EMAIL_COLUMN + "," + USERNAME_COLUMN + "," + PASSWORD_COLUMN + ") VALUES('" + user.getType() + "', '" + user.getEmail() + "', '" + user.getUsername() + "', '" + user.getPassword() + "')";

			Statement statement = connection.createStatement();
			statement.execute(insertUser);
			connection.commit();

			ResultSet keys = statement.getGeneratedKeys();
			if (keys.next())
			{
				user.setId((int) keys.getLong(1));
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
	 * Function for inserting a room into the system
	 * @param room being added to the system
	 * @return inserted Room object
	 */
	public static Room insertRoom(Room room)
	{
		try
		{
			Statement statement = connection.createStatement();

			String insertRoom = "INSERT INTO " + ROOMS_TABLE_NAME + "(" + TYPE_COLUMN + "," + PERKS_COLUMN + "," + NUMBER_OF_BEDS_COLUMN + "," + RATE_COLUMN + ") VALUES('" + room.getType() + "', '" + room.getPerks() + "', '" + room.getNumberOfBeds() + "'," + room.getRate() + ")";

			statement.execute(insertRoom);
			connection.commit();

			ResultSet keys = statement.getGeneratedKeys();
			if (keys.next())
			{
				room.setId((int) keys.getLong(1));
				return room;
			}
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
		return null;
	}
}
