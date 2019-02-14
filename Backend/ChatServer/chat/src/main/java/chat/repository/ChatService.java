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

		public Chat create(Chat newChat) {
			// validate model
			// authenticate Chat, etc
			return chatRepository.save(newChat);
		}

		public List<Chat> getAll() {
			
			return chatRepository.findAll();
		}
		
		public Chat getById(int ID)
		{
			return chatRepository.getOne(ID);
		}
		
		public ResponseEntity<ChatCreateResponse> create(ChatCreateRequest request) {
			if (request == null) {
				throw new NullPointerException();
			}

			ChatCreateResponse response = new ChatCreateResponse();

			response.setValidID(validateChatname(request.getChat()));
			response.setValidPass(validatePassword(request.getPassword()));
			response.setMessage(
					"Chatname Valid: " + response.getValidChat() + "; Password Valid: " + response.getValidPass());
			response.setSuccess(response.getValidChat() && response.getValidPass());

			if (response.getSuccess()) {
				Chat u = new Chat();
				u.updateChat(request);
				chatRepository.save(u);
			}

			return new ResponseEntity<ChatCreateResponse>(response, HttpStatus.OK);
		}

		private boolean validateID(int id)
		{
			return !chatRepository.exists(id);
		}
	
}
