package dev.platinum.hotel.exceptions;

import java.util.List;

import graphql.ErrorClassification;
import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

/**
 * UserAlreadyExists exception to tell the client that a registration isn't possible
 * @author Marcin Sęk
 */
public class UserAlreadyExistsException extends RuntimeException implements GraphQLError
{
	/**
	 * Simple constructor for the Exception
	 * @param message being sent to the client
	 */
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
