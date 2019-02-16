package chat.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
	
	
		@Autowired
		ChatRepository chatRepository;
		
		public List<Chat> getAll() {
			
			return chatRepository.findAll();
		}
		
		public Chat getById(Integer ID)
		{
			return chatRepository.getOne(ID);
		}
		
		public ResponseEntity<ChatCreateResponse> create(ChatCreateRequest request) {
			if (request == null) {
				throw new NullPointerException();
			}

			ChatCreateResponse response = new ChatCreateResponse();
			response.setMessage("Successfully added chat");
			

			if (response.getSuccess()) {
				Chat u = new Chat();
				chatRepository.save(u);
			}

			return new ResponseEntity<ChatCreateResponse>(response, HttpStatus.OK);
		}

		private boolean validateID(Integer id)
		{
			return !chatRepository.exists(id);
		}
	
}
