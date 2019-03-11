package edu.iastate.cs309.jr2.CatchTheCacheServer.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	boolean existsByUsername(String username);

	List<User> findAllByAuthority(int authority);

	User findByUsername(String username);
}
