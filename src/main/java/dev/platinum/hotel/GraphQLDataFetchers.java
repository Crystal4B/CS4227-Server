package dev.platinum.hotel;

import org.springframework.stereotype.Component;

import dev.platinum.hotel.store.DeleteQueries;
import dev.platinum.hotel.store.InsertQueries;
import dev.platinum.hotel.store.SelectQueries;
import dev.platinum.hotel.types.Guest;
import dev.platinum.hotel.types.Reservation;
import dev.platinum.hotel.types.Room;
import dev.platinum.hotel.types.User;
import graphql.schema.DataFetcher;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The GraphQLDataFetchers class for handling incoming requests
 * @author Marcin Sęk
 */
@Component
public class GraphQLDataFetchers
{
	/**
	 * The DataFetcher handling ReservationById requests
	 * @return a Reservation Object or null if not found
	 */
	public DataFetcher<Reservation> getReservationByIdDataFetcher()
	{
		return dataFetchingEnvironment -> {
			int reservationId = dataFetchingEnvironment.getArgument("id");
			return SelectQueries.selectReservationById(reservationId);
		};
	}

	/**
	 * The DataFetcher handling ReservationsByUser requests
	 * @return a List of Reservations visible to the user
	 */
	public DataFetcher<List<Reservation>> getReservationsByUserDataFetcher()
	{
		return dataFetchingEnvironment -> {
			Map<String, Object> data = dataFetchingEnvironment.getArgument("input");
			int id = Integer.parseInt((String) data.get("id"));
			String type = (String) data.get("type");
			String email = (String) data.get("email");
			User user = new User(id, type, email);

			return SelectQueries.selectReservationsByUser(user);
		};
	}

	/**
	 * The DataFetcher handling RoomById requests
	 * @return a Room Object or null if not found
	 */
	public DataFetcher<Room> getRoomByIdDataFetcher()
	{
		return dataFetchingEnvironment -> {
			int roomId = dataFetchingEnvironment.getArgument("id");
			return SelectQueries.selectRoomById(roomId);
		};
	}

	/**
	 * The DataFetcher handling AvailableRoomsByDates requests
	 * @return a List of Room Objects or null if not found
	 */
	public DataFetcher<List<Room>> getAvailableRoomsByDatesDataFetcher()
	{
		return dataFetchingEnvironment -> {
			Timestamp checkIn = dataFetchingEnvironment.getArgument("checkIn");
			Timestamp checkOut = dataFetchingEnvironment.getArgument("checkOut");
			return SelectQueries.selectAvailableRoomsByDates(checkIn, checkOut);
		};
	}

	/**
	 * The DataFetcher handling loginUser requests
	 * @return the User Object that has logged in or null if unsuccessful
	 */
	public DataFetcher<User> loginUserDataFetcher()
	{
		return dataFetchingEnvironment -> {
			Map<String, Object> data = dataFetchingEnvironment.getArgument("input");
			String email = (String) data.get("email");
			String password = (String) data.get("password");

			return SelectQueries.selectUserByLogin(new User(email, password));
		};
	}

	/**
	 * The DataFetcher handling createReservations requests
	 * @return the inserted Reservation Object or null if unsuccessful
	 */
	@SuppressWarnings("unchecked")
	public DataFetcher<Reservation> createReservation()
	{
		return dataFetchingEnvironment -> {
			Map<String, Object> data = dataFetchingEnvironment.getArgument("input");
			Timestamp checkIn = (Timestamp) data.get("checkIn");
			Timestamp checkOut = (Timestamp) data.get("checkOut");

			Map<String, String> userMap = (Map<String, String>) data.get("user");
			int userId = Integer.parseInt(userMap.get("id"));
			User user = new User(userId);

			List<Map<String, String>> guestsMap = (List<Map<String, String>>) data.get("guests");
			List<Guest> guests = new ArrayList<>();
			for (Map<String, String> map : guestsMap)
			{
				// Get Mandatory fields
				String firstName = map.get("firstName");
				String lastName = map.get("lastName");
				int roomId = Integer.parseInt(map.get("roomId"));

				Guest guest = new Guest(firstName, lastName, new Room(roomId));

				guests.add(guest);
			}
			Reservation incomingReservation = new Reservation(checkIn, checkOut, user, guests);

			return InsertQueries.insertReservation(incomingReservation);
		};
	}

	/**
	 * The DataFetcher handling createRoom requests
	 * @return inserted Room object or null if unsuccessful
	 */
	public DataFetcher<Room> createRoom()
	{
		return dataFetchingEnvironment -> {
			Map<String, Object> roomsMap = dataFetchingEnvironment.getArgument("input");
			String type = (String) roomsMap.get("type");
			String perks = (String) roomsMap.get("perks");
			Integer numberOfBeds = (Integer) roomsMap.get("numberOfBeds");
			Integer rate = (Integer) roomsMap.get("rate");

			Room room = new Room(type, perks, numberOfBeds, rate);

			return InsertQueries.insertRoom(room);
		};
	}

	/**
	 * The DataFetcher handling createRooms requests
	 * @return a list of inserted Room objects or null if unsuccessful
	 */
	public DataFetcher<List<Room>> createRooms()
	{
		return dataFetchingEnvironment -> {
			List<Map<String, Object>> roomsMap = dataFetchingEnvironment.getArgument("input");
			List<Room> rooms = new ArrayList<>();
			for (Map<String, Object> map : roomsMap)
			{
				String type = (String) map.get("type");
				String perks = (String) map.get("perks");
				Integer numberOfBeds = (Integer) map.get("numberOfBeds");
				Integer rate = (Integer) map.get("rate");

				rooms.add(new Room(type, perks, numberOfBeds, rate));
			}

			return InsertQueries.insertRooms(rooms);
		};
	}

	/**
	 * The DataFetcher handling createUser requests
	 * @return the inserted User object or null if unsuccessful
	 */
	public DataFetcher<User> createUser()
	{
		return dataFetchingEnvironment -> {
			Map<String, Object> data = dataFetchingEnvironment.getArgument("input");
			String email = (String) data.get("email");

			// Validate the email address is available
			boolean available = SelectQueries.checkEmailAvailablity(email);
			if (available)
			{
				String type = (String) data.get("type");
				String username = (String) data.get("username");
				String password = (String) data.get("password");
				return InsertQueries.insertUser(new User(type, email, username, password));
			}
			return null;
		};
	}

	/**
	 * The DataFetcher handling removeUser requests
	 * @return the removed User object or null if unsuccessful
	 */
	public DataFetcher<User> removeUser()
	{
		return dataFetchingEnvironment -> {
			Map<String, String> data = dataFetchingEnvironment.getArgument("input");
			int id = Integer.parseInt(data.get("id"));
			return DeleteQueries.deleteUser(new User(id));
		};
	}

	/**
	 * The DataFetcher handling removeRoom requests
	 * @return removed Room object or null if unsuccessful
	 */
	public DataFetcher<Room> removeRoom()
	{
		return dataFetchingEnvironment -> {
			Map<String, Object> roomsMap = dataFetchingEnvironment.getArgument("input");
			int id =  Integer.parseInt((String) roomsMap.get("id"));

			Room room = new Room(id);

			return DeleteQueries.deleteRoom(room);
		};
	}

	/**
	 * The DataFetcher handling removeRooms requests
	 * @return a list of removed Room objects or null if unsuccessful
	 */
	public DataFetcher<List<Room>> removeRooms()
	{
		return dataFetchingEnvironment -> {
			List<Map<String, Object>> roomsMap = dataFetchingEnvironment.getArgument("input");
			List<Room> rooms = new ArrayList<>();
			for (Map<String, Object> map : roomsMap)
			{
				int id =  Integer.parseInt((String) map.get("id"));

				rooms.add(new Room(id));
			}

			return DeleteQueries.deleteRooms(rooms);
		};
	}

	/**
	 * The DataFetcher handling removeReservation requests
	 * @return the removed Reservation Object or null if unsuccessful
	 */
	public DataFetcher<Reservation> removeReservation()
	{
		return dataFetchingEnvironment -> {
			Map<String, Object> data = dataFetchingEnvironment.getArgument("input");
			String id = (String) data.get("id");

			return DeleteQueries.deleteReservation(id);
		};
	}

	/**
	 * The DataFetcher handling allRooms requests
	 * @return the complete list of rooms in the hotel
	 */
	public DataFetcher<List<Room>> getAllRooms()
	{
		return dataFetchingEnvironment -> {
			return SelectQueries.selectAllRooms();
		};
	}
}
