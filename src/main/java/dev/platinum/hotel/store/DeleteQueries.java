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
 * Store class for hanadling delete queries
 * @author Marcin Sęk
 */
public class DeleteQueries extends StoreComponent
{
	/**
	 * Function for removing a user from the system
	 * @param user the user being removed from the system
	 * @return the removed user object, or null if unsuccessful
	 */
	public static User deleteUser(User user)
	{
		try
		{
			String deleteUser = "DELETE FROM " + USERS_TABLE_NAME + " WHERE " + ID_COLUMN + " = '" + user.getId() + "' RETURNING *";

			Statement statement = connection.createStatement();
			ResultSet results = statement.executeQuery(deleteUser);

			User resultUser = null;
			if (results.next())
			{
				int id = results.getInt(ID_COLUMN);
				String type = results.getString(TYPE_COLUMN);
				String email = results.getString(EMAIL_COLUMN);

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

	public static Voucher removeVoucher(int voucherId)
	{
		try
		{
			Statement statement = connection.createStatement();
			String deleteVoucher = "DELETE FROM " + VOUCHER_TABLE_NAME + " WHERE " + ID_COLUMN + "=" + "'" + voucherId + "'" + " RETURNING *";
			ResultSet results = statement.executeQuery(deleteVoucher);
			if (results.next())
			{
				int id = results.getInt(ID_COLUMN);
				String type = results.getString(TYPE_COLUMN);
				double amount = results.getDouble(AMOUNT_COLUMN);
				Timestamp issue_date = results.getTimestamp(ISSUE_DATE_COLUMN);
				Timestamp expiry_date = results.getTimestamp(EXPIRY_DATE_COLUMN) ;
				
				int userId = results.getInt(USER_ID_COLUMN);
				User creator = 	SelectQueries.selectUserById(userId);

				int reservationId = results.getInt(RESERVATION_ID_COLUMN);

				Reservation available = new Reservation(reservationId);

				Voucher voucherBoi = new Voucher(id,issue_date, expiry_date, type, amount, creator, available);
				results.close();				
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
	 * Function for removing rooms from the system
	 * @param rooms a list of rooms being removed from the system
	 * @return the list of removed room objects, or null if unsuccessful
	 */
	public static ArrayList<Room> deleteRooms(List<Room> rooms)
	{
		try
		{
			String deleteRooms = "DELETE FROM " + ROOMS_TABLE_NAME + " WHERE " + ID_COLUMN + " IN (";
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

			Statement statement = connection.createStatement();
			ResultSet results = statement.executeQuery(deleteRooms);
			
			ArrayList<Room> deletedRooms = new ArrayList<>();
			while (results.next())
			{
				int id = results.getInt(ID_COLUMN);
				String type = results.getString(TYPE_COLUMN);
				String perks = results.getString(PERKS_COLUMN);
				int numberOfBeds = results.getInt(NUMBER_OF_BEDS_COLUMN);
				int rate = results.getInt(RATE_COLUMN);

				deletedRooms.add(new Room(id, type, perks, numberOfBeds, rate));
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

	/**
	 * Function for removing a reservation from the system
	 * @param id the id of the reservation being removed
	 * @return the removed reservation object, null if unsuccessful
	 */
	public static Reservation deleteReservation(String id)
	{
		try
		{
			String deleteReservation = "DELETE FROM " + RESERVATIONS_TABLE_NAME + " WHERE " + ID_COLUMN + " = '" + id + "' RETURNING *";

			Statement statement = connection.createStatement();
			ResultSet results = statement.executeQuery(deleteReservation);
			
			Reservation resultReservation = null;
			if (results.next())
			{
				int deletedId = results.getInt(ID_COLUMN);

				// results.getTimestamp("column_name") returns 1970-01-01
				Timestamp checkIn = Timestamp.valueOf((String) results.getObject(CHECK_IN_COLUMN));
				Timestamp checkOut = Timestamp.valueOf((String) results.getObject(CHECK_OUT_COLUMN));

				int userId = results.getInt(USER_ID_COLUMN);
				User user = SelectQueries.selectUserById(userId);

				String guestIds = results.getString(GUEST_IDS_COLUMN);
				int guestIdsArr[] = Arrays.stream(guestIds.split(",")).mapToInt(Integer::parseInt).toArray();
				List<Guest> guests = SelectQueries.selectGuestsByIds(guestIdsArr);

				resultReservation = new Reservation(deletedId, checkIn, checkOut, user, guests);
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

	/**
	 * Function for removing rooms from the system
	 * @param room being removed from the system
	 * @return removed room object, or null if unsuccessful
	 */
	public static Room deleteRoom(Room room)
	{
		String deleteRoom = "DELETE FROM " + ROOMS_TABLE_NAME + " WHERE " + ID_COLUMN + " = " + room.getId() + " RETURNING *";

		try
		{
			Statement statement = connection.createStatement();
			ResultSet results = statement.executeQuery(deleteRoom);

			Room result = null;
			if (results.next())
			{
				int id = results.getInt(ID_COLUMN);
				String type = results.getString(TYPE_COLUMN);
				String perks = results.getString(PERKS_COLUMN);
				int numberOfBeds = results.getInt(NUMBER_OF_BEDS_COLUMN);
				int rate = results.getInt(RATE_COLUMN);

				result = new Room(id, type, perks, numberOfBeds, rate);
			}
			results.close();
			connection.commit();

			return result;
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
		return null;
	}
}
