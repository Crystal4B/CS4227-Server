package dev.platinum.hotel;

import java.sql.Timestamp;
import java.time.format.DateTimeParseException;

import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;

/**
 * The GraphQLScalarTypes class for initializing GraphQL Scalar Types
 * @author Marcin Sęk
 */
public class GraphQLScalarTypes
{
	/**
	 * Function for creating the Date scalar type for the schema
	 * @return the built scalar type for Date
	 */
	public static final GraphQLScalarType createDateScalar()
	{
		return GraphQLScalarType.newScalar()
			.name("Date")
			.description("Java SQL Timestamp as scalar")
			.coercing(new Coercing<Timestamp,String>()
			{
				@Override
				public String serialize(Object dataFetcherResult)
				{
					if (dataFetcherResult instanceof Timestamp)
					{
						return dataFetcherResult.toString();
					}
					throw new CoercingSerializeException("Expected a Date object.");
				}

				@Override
				public Timestamp parseValue(Object input)
				{
					try
					{
						if (input instanceof String)
						{
							return Timestamp.valueOf((String) input);
						}
						throw new CoercingParseValueException("Expected a String");
					}
					catch (DateTimeParseException e)
					{

						throw new CoercingParseValueException(String.format("Not a valid date: '%s'.", e));
					}
				}

				@Override
				public Timestamp parseLiteral(Object input)
				{
					try
					{
						if (input instanceof StringValue)
						{
							return Timestamp.valueOf(((StringValue) input).getValue());
						}
						throw new CoercingParseLiteralException("Expected a StringValue.");
					}
					catch (DateTimeParseException e)
					{
						throw new CoercingParseLiteralException(String.format("Not a valid date: '%s'.", e));
					}
				}
			})
			.build();
	}
}