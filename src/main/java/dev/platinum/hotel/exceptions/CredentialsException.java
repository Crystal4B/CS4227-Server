package dev.platinum.hotel.exceptions;

import java.util.List;

import javax.security.auth.login.CredentialException;

import graphql.ErrorClassification;
import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

public class CredentialsException extends CredentialException implements GraphQLError
{
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

