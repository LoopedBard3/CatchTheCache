package edu.iastate.cs309.jr2.CatchTheCacheServer.cache;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CacheRepository extends JpaRepository<Cache, Integer> {

	boolean existsByName(String name);

	boolean existsByLocation(double latitude, double longitude);

}
