package edu.iastate.cs309.jr2.CatchTheCacheServer.user;

public class UserNotFoundException extends RuntimeException {
	UserNotFoundException(Long id) {
		super("Could not find user " + id);
	}
}
