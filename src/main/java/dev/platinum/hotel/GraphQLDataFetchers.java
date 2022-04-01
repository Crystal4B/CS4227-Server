package dev.platinum.hotel;

import org.springframework.stereotype.Component;

import dev.platinum.hotel.exceptions.CredentialsException;
import dev.platinum.hotel.exceptions.UserAlreadyExistsException;
import dev.platinum.hotel.store.DeleteQueries;
import dev.platinum.hotel.store.InsertQueries;
import dev.platinum.hotel.store.SelectQueries;
import dev.platinum.hotel.store.UpdateQueries;
import dev.platinum.hotel.types.Guest;
import dev.platinum.hotel.types.Reservation;
import dev.platinum.hotel.types.Room;
import dev.platinum.hotel.types.User;
import dev.platinum.hotel.types.Voucher;
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
	 * The DataFetcher handling validateVoucher requests
	 * @return and validates Voucher visible to the user
	 */
	public DataFetcher<Voucher> validateVoucher()
	{
		return dataFetchingEnvironment -> {
			int voucherId = Integer.parseInt(dataFetchingEnvironment.getArgument("id"));

			return SelectQueries.selectVoucherById(voucherId) ;
		};
	}

	/**
	 * The DataFetcher handling createVoucher requests
	 * @return the created Voucher visible to the user
	 */
	public DataFetcher<Voucher> createVoucher()
	{
		return dataFetchingEnvironment -> {
			Map<String, Object> data = dataFetchingEnvironment.getArgument("input");
			Timestamp issue_d = (Timestamp) data.get("issue_date");
			Timestamp expiry_d = (Timestamp) data.get("expiry_date");
			String type = (String) data.get("type");
			double amount = (double) data.get("amount");
			Map<?,?> userMap =  (Map<?,?>) data.get("creator");
			String userId = (String) userMap.get("id");
			User user = new User(Integer.parseInt(userId));

			return InsertQueries.createVoucher(issue_d, expiry_d, type, amount, user) ;
		};
	}

	/**
	 * The DataFetcher handling removeVoucher requests
	 * @return the deleted of Voucher visible to the user
	 */
	public DataFetcher<Voucher> removeVoucher()
	{
		return dataFetchingEnvironment -> {
			int voucherId = Integer.parseInt(dataFetchingEnvironment.getArgument("id"));

			return DeleteQueries.removeVoucher(voucherId) ;
		};
	}

	/**
	 * The DataFetcher handling updateVoucher requests
	 * @return the updated Voucher visible to the user
	 */
	public DataFetcher<Voucher> updateVoucher()
	{
		return dataFetchingEnvironment -> {
			int voucherId = Integer.parseInt(dataFetchingEnvironment.getArgument("id"));
			Map<String, Object> data = dataFetchingEnvironment.getArgument("voucher");
			Voucher voucher = SelectQueries.selectVoucherById(voucherId);
			if(data.containsKey("issue_date")){
				voucher.setIssueDate((Timestamp) data.get("issue_date"));	
			}
			if(data.containsKey("expiry_date")){
				voucher.setExpiryDate((Timestamp) data.get("expiry_date"));
			}
			if(data.containsKey("type")){
				voucher.setType((String) data.get("type"));
			}
			if(data.containsKey("amount")){
				voucher.setAmount((double) data.get("amount"));
			}
			if(data.containsKey("creator")){
				Map<?,?> userMap =  (Map<?,?>) data.get("creator");
				String userId = (String) userMap.get("id");
				User user = new User(Integer.parseInt(userId));
				voucher.setCreator(user);

			}
			if(data.containsKey("available")){
				Map<?,?> reservationMap =  (Map<?,?>) data.get("available");
				String reservationId = (String) reservationMap.get("id");
				Reservation reservation = new Reservation(Integer.parseInt(reservationId));
				voucher.setAvailability(reservation);
			}
			return UpdateQueries.updateVoucher(voucherId, voucher) ;
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

			User user = SelectQueries.selectUserByLogin(new User(email, password));
			if (user == null)
			{
				throw new CredentialsException("The email or password is incorrect");
			}

			return SelectQueries.selectUserByLogin(new User(email, password));
		};
	}

	/**
	 * The DataFetcher handling createReservations requests
	 * @return the inserted Reservation Object or null if unsuccessful
	 */
	public DataFetcher<Reservation> createReservation()
	{
		return dataFetchingEnvironment -> {
			Map<String, Object> data = dataFetchingEnvironment.getArgument("input");
			Timestamp checkIn = (Timestamp) data.get("checkIn");
			Timestamp checkOut = (Timestamp) data.get("checkOut");

			Map<?, ?> userMap = (Map<?, ?>) data.get("user");
			int userId = Integer.parseInt((String) userMap.get("id"));
			User user = new User(userId);

			List<?> guestsList = (List<?>) data.get("guests");
			List<Guest> guests = new ArrayList<>();
			for (int i = 0; i < guestsList.size(); i++)
			{
				Map<?, ?> guestMap = (Map<?, ?>) guestsList.get(i);

				// Get Mandatory fields
				String firstName = (String) guestMap.get("firstName");
				String lastName = (String) guestMap.get("lastName");
				int roomId = Integer.parseInt((String) guestMap.get("roomId"));

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
			if (!available)
			{
				throw new UserAlreadyExistsException("A user already exists with this email");
			}

			// Register User
			String type = (String) data.get("type");
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

	public DataFetcher<Reservation> updateReservationPaid()
	{
		return dataFetchingEnvironment -> {
			Map<String, Object> data = dataFetchingEnvironment.getArgument("input");
			int id = Integer.parseInt((String) data.get("id"));
			boolean paid = (boolean) data.get("paid");

			return UpdateQueries.updateReservationPaid(id, paid);
		};
	}
}
