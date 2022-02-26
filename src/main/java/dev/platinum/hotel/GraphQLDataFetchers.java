package dev.platinum.hotel;

import org.springframework.stereotype.Component;

import graphql.schema.DataFetcher;

import java.sql.Timestamp;
import java.util.Map;

/**
 * The GraphQLDataFetchers class for handling incoming requests
 * @author Marcin Sęk
 */
@Component
public class GraphQLDataFetchers
{
	public DataFetcher<Reservation> getReservationByIDataFetcher()
	{
		return dataFetchingEnvironment -> {
			String reservationId = dataFetchingEnvironment.getArgument("id");
			return Store.selectReservationById(reservationId.toString());
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

			Reservation incomingReservation = new Reservation(reservationDate, arrivalDate, departureDate, numberOfOccupants);

			// Generate Reservation Object
			return Store.insertReservation(incomingReservation);
		};
	}
}
