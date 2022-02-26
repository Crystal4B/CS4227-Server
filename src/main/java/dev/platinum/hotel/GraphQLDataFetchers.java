package dev.platinum.hotel;

import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Component;

import graphql.schema.DataFetcher;

import java.time.LocalDate;
import java.util.Map;

@Component
public class GraphQLDataFetchers
{
	public DataFetcher getReservationByIDataFetcher()
	{
		return dataFetchingEnvironment -> {
			LocalDate date = LocalDate.now();
			System.out.println(date.toString());
			String reservationId = dataFetchingEnvironment.getArgument("id");
			return Store.selectReservationById(reservationId.toString());
		};
	}

	public DataFetcher createReservation()
	{
		return dataFetchingEnvironment -> {
			// GraphQL converst object arguments into Maps
			Map<String, Object> data = dataFetchingEnvironment.getArgument("input");
			LocalDate reservationDate = (LocalDate) data.get("reservationDate");
			LocalDate arrivalDate = (LocalDate) data.get("arrivalDate");
			LocalDate departureDate = (LocalDate) data.get("departureDate");
			int numberOfOccupants = (int) data.get("numberOfOccupants");

			// Generate Reservation Object
			int id = Store.insertReservation(reservationDate, arrivalDate, departureDate, numberOfOccupants);
			return id;
		};
	}
}
