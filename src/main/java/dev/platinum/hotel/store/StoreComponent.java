package dev.platinum.hotel.store;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * The store class is made for managing the data being stored on the server
 * @author Marcin Sęk
 */
@Component
public class StoreComponent
{
	// Common strings
	protected static final String ID_COLUMN = "id";
	protected static final String TYPE_COLUMN = "type";

	// Room table strings
	protected static final String ROOMS_TABLE_NAME = "rooms";
	protected static final String PERKS_COLUMN = "perks";
	protected static final String NUMBER_OF_BEDS_COLUMN = "number_of_beds";
	protected static final String RATE_COLUMN = "rate";
	
	// Guest table strings
	protected static final String GUESTS_TABLE_NAME = "guests";
	protected static final String FIRST_NAME_COLUMN = "first_name";
	protected static final String LAST_NAME_COLUMN = "last_name";
	protected static final String ROOM_ID_COLUMN = "room_id";
	
	// Reservation table strings
	protected static final String RESERVATIONS_TABLE_NAME = "reservations";
	protected static final String CHECK_IN_COLUMN = "check_in";
	protected static final String CHECK_OUT_COLUMN = "check_out";
	protected static final String USER_ID_COLUMN = "user_id";
	protected static final String GUEST_IDS_COLUMN = "guest_ids";
	
	// User table strings
	protected static final String USERS_TABLE_NAME = "users";
	protected static final String EMAIL_COLUMN = "email";
	protected static final String USERNAME_COLUMN = "username";
	protected static final String PASSWORD_COLUMN = "password";

	protected static Connection connection;
	
	/**
	 * Initialization for the store component
	 */
	@Bean
	public static void init()
	{
		try
		{
			DriverManager.registerDriver(new org.sqlite.JDBC());
			connection = DriverManager.getConnection("jdbc:sqlite:db.sqlite");
			connection.setAutoCommit(false);
			createTables();
		}
		catch(SQLException e)
		{
			System.out.println(e.toString());
		}
	}

	/**
	 * Function for setting up the sql tables if required
	 */
	private static void createTables()
	{
		try
		{
			Statement statement = connection.createStatement();
			String roomTable = (
				"CREATE TABLE IF NOT EXISTS " + ROOMS_TABLE_NAME + "(" + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT," + TYPE_COLUMN + " TEXT NOT NULL," + PERKS_COLUMN + " TEXT," + NUMBER_OF_BEDS_COLUMN + " INT NOT NULL," + RATE_COLUMN + " INT NOT NULL)"
			);
			String guestTable = (
				"CREATE TABLE IF NOT EXISTS " + GUESTS_TABLE_NAME + "(" + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT," + FIRST_NAME_COLUMN + " TEXT NOT NULL," + LAST_NAME_COLUMN + " TEXT NOT NULL," + ROOM_ID_COLUMN + " INTEGER NOT NULL, FOREIGN KEY(" + ROOM_ID_COLUMN + ") REFERENCES rooms(" + ID_COLUMN + "))"
			);
			String reservationTable = (
				"CREATE TABLE IF NOT EXISTS " + RESERVATIONS_TABLE_NAME + "(" + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT," + CHECK_IN_COLUMN + " DATETIME NOT NULL," + CHECK_OUT_COLUMN + " DATETIME NOT NULL," + USER_ID_COLUMN+ " INTEGER NOT NULL," + GUEST_IDS_COLUMN + " TEXT NOT NULL, FOREIGN KEY(" + USER_ID_COLUMN + ") REFERENCES users(" + ID_COLUMN + "))"
			); // guest_ids as text in format "id,id,id"
			String userTable = (
				"CREATE TABLE IF NOT EXISTS " + USERS_TABLE_NAME + "(" + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT," + TYPE_COLUMN + " TEXT NOT NULL," + EMAIL_COLUMN + " TEXT NOT NULL," + USERNAME_COLUMN + " TEXT NOT NULL," + PASSWORD_COLUMN + " TEXT NOT NULL)"
			);
			// TODO: figure out default admin requirements
			String insertAdminUser = (
				"INSERT INTO " + USERS_TABLE_NAME + "(" + TYPE_COLUMN + "," + EMAIL_COLUMN + "," + USERNAME_COLUMN + "," + PASSWORD_COLUMN + ") VALUES ('Staff', 'teamplatinumlimerick@gmail.com', 'Admin', 'password')"
			);

			statement.addBatch(roomTable);
			statement.addBatch(guestTable);
			statement.addBatch(reservationTable);
			statement.addBatch(userTable);
			statement.addBatch(insertAdminUser);
			statement.executeBatch();
			
			connection.commit();
		}
		catch(SQLException e)
		{
			System.out.println(e.toString());
		}
	}
}