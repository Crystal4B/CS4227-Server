package dev.platinum.hotel;

import com.google.common.io.Resources;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

/**
 * The GraphQLProvider class for initializing GraphQL
 * @author Marcin Sęk
 */
@Component
public class GraphQLProvider
{
	private GraphQL graphQL;

	@Bean
	public GraphQL graphQL()
	{
		return graphQL;
	}

	@PostConstruct
	public void init() throws IOException
	{
		URL url = Resources.getResource("schema.graphqls");
		String sdl = Resources.toString(url, Charset.defaultCharset());
		GraphQLSchema graphQLSchema = buildSchema(sdl);
		this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
	}

	@Autowired
	GraphQLDataFetchers graphQLDataFetchers;

	/**
	 * Function for creating the GraphQL Schema
	 * @param sdl the schema in string format
	 * @return the built GraphQLSchema
	 */
	private GraphQLSchema buildSchema(String sdl)
	{
		TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(sdl);
		RuntimeWiring runtimeWiring = buildWiring();
		SchemaGenerator schemaGenerator = new SchemaGenerator();
		return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
	}

	/**
	 * Function for creating the Graph wiring of the schema
	 * @return the built wiring for the schema
	 */
	private RuntimeWiring buildWiring()
	{
		return RuntimeWiring.newRuntimeWiring()
			.type(newTypeWiring("Query")
				.dataFetcher("validateVoucher", graphQLDataFetchers.validateVoucher()))
			.type(newTypeWiring("Query")
				.dataFetcher("reservationById", graphQLDataFetchers.getReservationByIdDataFetcher()))
			.type(newTypeWiring("Query")
				.dataFetcher("reservationsByUser", graphQLDataFetchers.getReservationsByUserDataFetcher()))
			.type(newTypeWiring("Query")
				.dataFetcher("allRooms", graphQLDataFetchers.getAllRooms()))
			.type(newTypeWiring("Query")
				.dataFetcher("roomById", graphQLDataFetchers.getRoomByIdDataFetcher()))
			.type(newTypeWiring("Query")
				.dataFetcher("availableRoomsByDates", graphQLDataFetchers.getAvailableRoomsByDatesDataFetcher()))
			.type(newTypeWiring("Query")
				.dataFetcher("loginUser", graphQLDataFetchers.loginUserDataFetcher()))
			.type(newTypeWiring("Mutation")
				.dataFetcher("createReservation", graphQLDataFetchers.createReservation()))
			.type(newTypeWiring("Mutation")
				.dataFetcher("updateReservationPaid", graphQLDataFetchers.updateReservationPaid()))
			.type(newTypeWiring("Mutation")
				.dataFetcher("removeReservation", graphQLDataFetchers.removeReservation()))
			.type(newTypeWiring("Mutation")
				.dataFetcher("createRoom", graphQLDataFetchers.createRoom()))
			.type(newTypeWiring("Mutation")
				.dataFetcher("createRooms", graphQLDataFetchers.createRooms()))
			.type(newTypeWiring("Mutation")
				.dataFetcher("removeRoom", graphQLDataFetchers.removeRoom()))
			.type(newTypeWiring("Mutation")
				.dataFetcher("removeRooms", graphQLDataFetchers.removeRooms()))
			.type(newTypeWiring("Mutation")
				.dataFetcher("createUser", graphQLDataFetchers.createUser()))
			.type(newTypeWiring("Mutation")
				.dataFetcher("removeUser", graphQLDataFetchers.removeUser()))
			.type(newTypeWiring("Mutation")
				.dataFetcher("createVoucher", graphQLDataFetchers.createVoucher()))
			.type(newTypeWiring("Mutation")
				.dataFetcher("removeVoucher", graphQLDataFetchers.removeVoucher()))
			.type(newTypeWiring("Mutation")
				.dataFetcher("updateVoucher", graphQLDataFetchers.updateVoucher()))
			.scalar(GraphQLScalarTypes.createDateScalar())
			.build();
	}
}
