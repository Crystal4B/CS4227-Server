package dev.platinum.hotel.store;

import java.util.List;

import dev.platinum.hotel.types.Guest;

public class UpdateQueries extends StoreComponent
{

	public static List<Guest> updateGuestsRoom(List<Guest> existingGuests)
	{
		String updateGuestsRoomIds = "UPDATE " + GUESTS_TABLE_NAME + " SET " + ROOM_ID_COLUMN + "(CASE " + ID_COLUMN;
		for (Guest guest : existingGuests)
		{
			updateGuestsRoomIds += "WHEN " + guest.getId() + " THEN " + guest.getRoom().getId() + " ";
		}
		updateGuestsRoomIds += "ELSE " + ROOM_ID_COLUMN + " END)";

		return null;
	}
	
}
