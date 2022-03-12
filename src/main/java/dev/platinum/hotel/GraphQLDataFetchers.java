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

// TODO: UPDATE DATAFETCHERS TO ADD GUESTS

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
			Timestamp arrivalDate = dataFetchingEnvironment.getArgument("arrivalDate");
			Timestamp departureDate = dataFetchingEnvironment.getArgument("departureDate");
			return SelectQueries.selectAvailableRoomsByDates(arrivalDate, departureDate);
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

			Map<String, Integer> userMap = (Map<String, Integer>) data.get("user");
			int userId = userMap.get("id");
			User user = new User(userId);

			List<Map<String, Integer>> guestsMap = (List<Map<String, Integer>>) data.get("guests");
			List<Guest> guests = new ArrayList<>();
			for (Map<String, Integer> map : guestsMap)
			{
				int id = map.get("id");
				guests.add(new Guest(id));
			}

			Reservation incomingReservation = new Reservation(checkIn, checkOut, user, guests);

			return InsertQueries.insertReservation(incomingReservation);
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
			String type = (String) data.get("type");
			String email = (String) data.get("email");
			String username = (String) data.get("username");
			String password = (String) data.get("password");
			return InsertQueries.insertUser(new User(type, email, username, password));
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
}
