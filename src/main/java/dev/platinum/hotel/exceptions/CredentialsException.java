package dev.platinum.hotel.exceptions;

import java.util.List;

import javax.security.auth.login.CredentialException;

import graphql.ErrorClassification;
import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

/**
 * CredentialsException for notifying the client that the credentials are incorrect
 * @author Marcin Sęk
 */
public class CredentialsException extends CredentialException implements GraphQLError
{
	/**
	 * Simple constructor for exception
	 * @param message being sent to the client
	 */
	public CredentialsException(String message)
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
		return ErrorType.ValidationError;
	}
}

