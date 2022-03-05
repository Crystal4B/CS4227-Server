package dev.platinum.hotel;

import org.springframework.stereotype.Component;

import dev.platinum.hotel.types.Reservation;
import dev.platinum.hotel.types.Room;
import dev.platinum.hotel.types.User;
import graphql.schema.DataFetcher;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;

/**
 * The GraphQLDataFetchers class for handling incoming requests
 * @author Marcin Sęk
 */
@Component
public class GraphQLDataFetchers
{
	public DataFetcher<Reservation> getReservationByIdDataFetcher()
	{
		return dataFetchingEnvironment -> {
			String reservationId = dataFetchingEnvironment.getArgument("id");
			return Store.selectReservationById(reservationId);
		};
	}

	public DataFetcher<Room> getRoomByIdDataFetcher()
	{
		return dataFetchingEnvironment -> {
			String roomId = dataFetchingEnvironment.getArgument("id");
			return Store.selectRoomById(roomId);
		};
	}

	public DataFetcher<ArrayList<Room>> getAvailableRoomsByDatesDataFetcher()
	{
		return dataFetchingEnvironment -> {
			Timestamp arrivalDate = dataFetchingEnvironment.getArgument("arrivalDate");
			Timestamp departureDate = dataFetchingEnvironment.getArgument("departureDate");
			return Store.selectAvailableRoomsByDates(arrivalDate, departureDate);
		};
	}

	public DataFetcher<User> loginUserDataFetcher()
	{
		return dataFetchingEnvironment -> {
			Map<String, Object> data = dataFetchingEnvironment.getArgument("input");
			String email = (String) data.get("email");
			String password = (String) data.get("password");
			return Store.selectUserByLogin(new User(email, password));
		};
	}

	public DataFetcher<Reservation> createReservation()
	{
		return dataFetchingEnvironment -> {
			Map<String, Object> data = dataFetchingEnvironment.getArgument("input");
			Timestamp arrivalDate = (Timestamp) data.get("arrivalDate");
			Timestamp departureDate = (Timestamp) data.get("departureDate");
			ArrayList<Map<String, String>> roomsMap = (ArrayList<Map<String, String>>) data.get("rooms");
			ArrayList<Room> rooms = new ArrayList<>();
			for (Map<String, String> map : roomsMap)
			{
				String id = map.get("id");
				rooms.add(new Room(id));
			}

			Reservation incomingReservation = new Reservation(arrivalDate, departureDate, rooms);

			return Store.insertReservation(incomingReservation);
		};
	}

	public DataFetcher<ArrayList<Room>> createRooms()
	{
		return dataFetchingEnvironment -> {
			ArrayList<Map<String, Object>> roomsMap = dataFetchingEnvironment.getArgument("input");
			ArrayList<Room> rooms = new ArrayList<>();
			for (Map<String, Object> map : roomsMap)
			{
				String type = (String) map.get("type");
				String name = (String) map.get("name");
				String perks = (String) map.get("perks");
				Integer numberOfBeds = (Integer) map.get("numberOfBeds");
				Integer rate = (Integer) map.get("rate");
				ArrayList<Map<String, String>> occupantsMap = (ArrayList<Map<String, String>>) map.get("occupants");
				ArrayList<User> occupants = new ArrayList<>();
				for (Map<String, String> userMap : occupantsMap)
				{
					String id = userMap.get("id");
					occupants.add(new User(id));
				}

				rooms.add(new Room(name, type, perks, numberOfBeds, rate, occupants));
			}

			return Store.insertRooms(rooms);
		};
	}

	public DataFetcher<User> createUser()
	{
		return dataFetchingEnvironment -> {
			Map<String, Object> data = dataFetchingEnvironment.getArgument("input");
			String type = (String) data.get("type");
			String email = (String) data.get("email");
			String username = (String) data.get("username");
			String password = (String) data.get("password");
			return Store.insertUser(new User(type, email, username, password));
		};
	}

	public DataFetcher<User> removeUser()
	{
		return dataFetchingEnvironment -> {
			Map<String, Object> data = dataFetchingEnvironment.getArgument("input");
			String id = (String) data.get("id");
			return Store.deleteUser(new User(id));
		};
	}

	public DataFetcher<ArrayList<Room>> removeRooms()
	{
		return dataFetchingEnvironment -> {
			ArrayList<Map<String, Object>> roomsMap = dataFetchingEnvironment.getArgument("input");
			ArrayList<Room> rooms = new ArrayList<>();
			for (Map<String, Object> map : roomsMap)
			{
				String id = (String) map.get("id");

				rooms.add(new Room(id));
			}

			return Store.deleteRooms(rooms);
		};
	}

	public DataFetcher<Reservation> removeReservation()
	{
		return dataFetchingEnvironment -> {
			Map<String, Object> data = dataFetchingEnvironment.getArgument("input");
			String id = (String) data.get("id");

			return Store.deleteReservation(id);
		};
	}
}
