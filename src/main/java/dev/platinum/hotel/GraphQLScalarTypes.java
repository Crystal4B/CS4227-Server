package dev.platinum.hotel;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;

public class GraphQLScalarTypes
{
	public static final GraphQLScalarType createDateScalar()
	{
		return GraphQLScalarType.newScalar()
			.name("Date")
			.description("Java LocalDate as scalar")
			.coercing(new Coercing<LocalDate,String>()
			{
				@Override
				public String serialize(Object dataFetcherResult)
				{
					if (dataFetcherResult instanceof LocalDate)
					{
						return dataFetcherResult.toString();
					}
					throw new CoercingSerializeException("Expected a LocalDate object.");
				}

				@Override
				public LocalDate parseValue(Object input)
				{
					try
					{
						if (input instanceof String)
						{
							return LocalDate.parse((String) input);
						}
						throw new CoercingParseValueException("Expected a String");
					}
					catch (DateTimeParseException e)
					{

						throw new CoercingParseValueException(String.format("Not a valid date: '%s'.", e));
					}
				}

				@Override
				public LocalDate parseLiteral(Object input)
				{
					try
					{
						if (input instanceof StringValue)
						{
							return LocalDate.parse(((StringValue) input).getValue());
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
