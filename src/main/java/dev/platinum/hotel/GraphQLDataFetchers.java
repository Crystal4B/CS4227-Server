package dev.platinum.hotel;

import com.google.common.collect.ImmutableMap;

import org.springframework.stereotype.Component;

import graphql.schema.DataFetcher;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class GraphQLDataFetchers
{
	public DataFetcher getReservationByIDataFetcher()
	{
		return dataFetchingEnvironment -> {
			int reservationId = dataFetchingEnvironment.getArgument("id");
			return Store.selectReservationById(reservationId);
		};
	}

	public DataFetcher createReservation()
	{
		return dataFetchingEnvironment -> {
			// GraphQL converst object arguments into Maps
			Map<String, String> reservationDateMap = dataFetchingEnvironment.getArgument("reservationDate");
			Map<String, String> arrivalDateMap = dataFetchingEnvironment.getArgument("arrivalDate");
			Map<String, String> departureDateMap = dataFetchingEnvironment.getArgument("departureDate");
			Map<String, String> numberOfOccupantsMap = dataFetchingEnvironment.getArgument("numberOfOccupants");
			Map<String, String> roomsMap = dataFetchingEnvironment.getArgument("rooms");

			// Generate Reservation Object
			int id = Store.insertReservation();
			return id;
		};
	}
}
