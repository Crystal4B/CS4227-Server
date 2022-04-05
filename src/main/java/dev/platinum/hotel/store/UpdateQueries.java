package dev.platinum.hotel.store;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import dev.platinum.hotel.types.Guest;
import dev.platinum.hotel.types.Reservation;
import dev.platinum.hotel.types.User;
import dev.platinum.hotel.types.Voucher;


/**
 * UpdateQueries class handles all update queries for the store
 */
public class UpdateQueries extends StoreComponent
{
	/**
	 * Update function for changing the Guest roomId
	 * @param existingGuests the list of guests being updated
	 */
	public static void updateGuestsRoom(List<Guest> existingGuests)
	{
		if (existingGuests.size() == 0)
		{
			return;
		}

		String updateGuestsRoomIds = "UPDATE " + GUESTS_TABLE_NAME + " SET " + ROOM_ID_COLUMN + " = (CASE " + ID_COLUMN;
		for (Guest guest : existingGuests)
		{
			updateGuestsRoomIds += " WHEN " + guest.getId() + " THEN " + guest.getRoom().getId() + " ";
		}
		updateGuestsRoomIds += "ELSE " + ROOM_ID_COLUMN + " END)";

		try
		{
			Statement statement = connection.createStatement();
			statement.executeUpdate(updateGuestsRoomIds);
			
			connection.commit();
		}
		catch(SQLException e)
		{
			System.out.println(e);
		}
	}

	/**
	 * Update function for changing a voucher status
	 * @param voucherId of the voucher being updated
	 * @param newVoucher the new voucher to replace the old voucher
	 * @return A complete voucher object for graphQL parsing
	 */
	public static Voucher updateVoucher(int voucherId, Voucher newVoucher)
	{
		try
		{
			Statement statement = connection.createStatement();
			String selectVoucher = "UPDATE " + VOUCHER_TABLE_NAME + " SET " + ISSUE_DATE_COLUMN + "= '" + newVoucher.getIssueDate() + "'," + EXPIRY_DATE_COLUMN + "='" + newVoucher.getExpiryDate() + "'," + TYPE_COLUMN + "='" + newVoucher.getType() + "'," + USER_ID_COLUMN + "=" + newVoucher.getCreator().getId() +  "," + RESERVATION_ID_COLUMN + "=" + newVoucher.getAvailability().getId() + " Where " + ID_COLUMN + "=" + voucherId;
			statement.addBatch(selectVoucher);
			statement.executeBatch();
			connection.commit();
			return newVoucher;
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
		return null;
	}

	/**
	 * Update function for changing a reservations paid status
	 * @param id of the reservation being updated
	 * @param paid the new status
	 * @return A complete reservation object for graphQL parsing
	 */
	public static Reservation updateReservationPaid(int id, boolean paid)
	{
		String updateReservationPaid = "Update " + RESERVATIONS_TABLE_NAME + " SET " + PAID_COLUMN + " = " + paid + " WHERE " + ID_COLUMN + "=" + id + " RETURNING *";
		try
		{
			Statement statement = connection.createStatement();
			ResultSet results = statement.executeQuery(updateReservationPaid);

			Reservation reservation = null;
			if (results.next())
			{
				// results.getTimestamp("column_name") returns 1970-01-01
				Timestamp checkIn = Timestamp.valueOf((String) results.getObject(CHECK_IN_COLUMN));
				Timestamp checkOut = Timestamp.valueOf((String) results.getObject(CHECK_OUT_COLUMN));
				boolean resultPaid = results.getBoolean(PAID_COLUMN);

				int userId = results.getInt(USER_ID_COLUMN);

				User resultUser = SelectQueries.selectUserById(userId);
				
				String guestIds = results.getString(GUEST_IDS_COLUMN);
				int guestIdsArr[] = Arrays.stream(guestIds.split(",")).mapToInt(Integer::parseInt).toArray();
				List<Guest> guests = SelectQueries.selectGuestsByIds(guestIdsArr);

				reservation = new Reservation(id, checkIn, checkOut, resultUser, guests, resultPaid);
				
			}
			results.close();
			connection.commit();

			return reservation;
		}
		catch(SQLException e)
		{
			System.out.println(e);
		}

		return null;
	}

	/**
	 * Update function for user passwords
	 * @param id of the user being updated
	 * @param newPassword for the user
	 * @return complete user after update
	 */
	public static User updateUserPassword(int id, String newPassword)
	{
		String updateUserPassword = "Update " + USERS_TABLE_NAME + " SET " + PASSWORD_COLUMN + " = '" + newPassword + "'," + DEFAULT_PASSWORD_COLUMN + " = " + false + " WHERE " + ID_COLUMN + " = " + id + " RETURNING *";
		try
		{
			Statement statement = connection.createStatement();
			ResultSet results = statement.executeQuery(updateUserPassword);

			User user = null;
			if (results.next())
			{
				String type = results.getString(TYPE_COLUMN);
				String username = results.getString(USERNAME_COLUMN);
				String email = results.getString(EMAIL_COLUMN);
				String password = results.getString(PASSWORD_COLUMN);

				user = new User(id, type, email, username);
				user.setPassword(password);
				
			}
			results.close();
			connection.commit();

			return user;
		}
		catch(SQLException e)
		{
			System.out.println(e);
		}

		return null;
	}
	
}
