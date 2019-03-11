package edu.iastate.cs309.jr2.CatchTheCacheServer.chat;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.iastate.cs309.jr2.CatchTheCacheServer.models.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
	List<Message> findAllByChatId(int chatId);

	void deleteAllByChatId(int chatId);
}
