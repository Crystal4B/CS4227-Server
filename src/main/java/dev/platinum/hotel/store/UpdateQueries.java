package dev.platinum.hotel.store;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import dev.platinum.hotel.types.Guest;

public class UpdateQueries extends StoreComponent
{

	public static void updateGuestsRoom(List<Guest> existingGuests)
	{
		if (existingGuests.size() == 0)
		{
			return;
		}

		String updateGuestsRoomIds = "UPDATE " + GUESTS_TABLE_NAME + " SET " + ROOM_ID_COLUMN + "(CASE " + ID_COLUMN;
		for (Guest guest : existingGuests)
		{
			updateGuestsRoomIds += "WHEN " + guest.getId() + " THEN " + guest.getRoom().getId() + " ";
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
	
}
