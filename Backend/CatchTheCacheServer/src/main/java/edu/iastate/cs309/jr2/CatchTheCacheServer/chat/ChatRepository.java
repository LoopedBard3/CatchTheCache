package edu.iastate.cs309.jr2.CatchTheCacheServer.chat;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends CrudRepository<Chat, Integer> {

}
