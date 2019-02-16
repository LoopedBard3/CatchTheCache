package chat.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
	
	
		@Autowired
		ChatRepository chatRepository;
		
		public List<Chat> getAll(){
			return chatRepository.findAll();
		}
		public List<String> getAllUser() {
			
			List<Chat> chats= chatRepository.findAll();
			List<String> results = new ArrayList<String>();
			for (Chat u : chats) {
				results.add(u.getUser());
			}
			return results;

		}
		
		public Chat getById(Integer ID)
		{
			return chatRepository.findByChatId(ID);
		}
		
		public ResponseEntity<ChatCreateResponse> create(ChatCreateRequest request) {
			if (request == null) {
				throw new NullPointerException();
			}

			ChatCreateResponse response = new ChatCreateResponse();
			response.setValidChatId(true);
			response.setValidCacheId(true);
			response.setMessage(
					"ChatId Valid: " + response.getValidChatId() + "; CacheId Valid: " + response.getValidCacheId());
			response.setSuccess(true);

			
			
				Chat u = new Chat(request.getChatId(),request.getUser(),request.getCacheId());
				chatRepository.save(u);
			

			return new ResponseEntity<ChatCreateResponse>(response, HttpStatus.OK);
		}

		private boolean validateChatId(Integer chatId)
		{
			return !chatRepository.existsByChatId(chatId);
		}
		
		
	
}
