package dev.platinum.hotel.exceptions;

import java.util.List;

import graphql.ErrorClassification;
import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

public class UserAlreadyExistsException extends RuntimeException implements GraphQLError
{
	public UserAlreadyExistsException(String message)
	{
		super(message);
	}

	@Override
	public List<SourceLocation> getLocations()
	{
		return null;
	}

	@Override
	public ErrorClassification getErrorType()
	{
		return ErrorType.ExecutionAborted;
	}
}
