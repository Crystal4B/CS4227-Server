package dev.platinum.hotel;

import org.springframework.stereotype.Component;

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

	public DataFetcher<Reservation> createReservation()
	{
		return dataFetchingEnvironment -> {
			// GraphQL converst object arguments into Maps
			Map<String, Object> data = dataFetchingEnvironment.getArgument("input");
			Timestamp reservationDate = (Timestamp) data.get("reservationDate");
			Timestamp arrivalDate = (Timestamp) data.get("arrivalDate");
			Timestamp departureDate = (Timestamp) data.get("departureDate");
			int numberOfOccupants = (int) data.get("numberOfOccupants");
			System.out.println(data.get("rooms").getClass());
			ArrayList<Map<String, String>> roomsMap = (ArrayList<Map<String, String>>) data.get("rooms");
			ArrayList<Room> rooms = new ArrayList<>();
			for (Map<String, String> map : roomsMap)
			{
				String id = map.get("id");
				rooms.add(new Room(id));
			}

			Reservation incomingReservation = new Reservation(reservationDate, arrivalDate, departureDate, numberOfOccupants);

			// Generate Reservation Object
			return Store.insertReservation(incomingReservation, rooms);
		};
	}

	public DataFetcher<ArrayList<Room>> createRooms()
	{
		return dataFetchingEnvironment -> {
			ArrayList<Map<String, Object>> roomsMap = dataFetchingEnvironment.getArgument("input");
			ArrayList<Room> rooms = new ArrayList<>();
			for (Map<String, Object> map : roomsMap)
			{
				String name = (String) map.get("name");
				String perks = (String) map.get("perks");
				Integer numberOfBeds = (Integer) map.get("numberOfBeds");
				Integer rate = (Integer) map.get("rate");

				rooms.add(new Room(name, perks, numberOfBeds, rate));
			}

			// Generate Reservation Object
			return Store.insertRooms(rooms);
		};
	}
}
