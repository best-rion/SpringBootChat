package site.rion.chat.controller;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import site.rion.chat.dto.MessageDTO;
import site.rion.chat.dto.UnseenDTO;
import site.rion.chat.model.ChatUser;
import site.rion.chat.model.Message;
import site.rion.chat.repository.MessageRepository;
import site.rion.chat.repository.UserRepository;

@Controller
public class HomeController {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	MessageRepository messageRepository;
	
	@GetMapping("/")
	public String home(Model model, Authentication auth){

		String principalUsername = auth.getName();

		List<ChatUser> users = userRepository.findAll();

		ChatUser currentUser = null;
		for (ChatUser user: users)
		{
			if (user.getUsername().equals(principalUsername))
			{
				currentUser = user;
			}
		}
		users.remove(currentUser);
		
		currentUser.setActive(true);
		userRepository.save(currentUser);
		
		
		// Unseen Messages
		
		List<UnseenDTO> unseenDTOs = new ArrayList<>();
		for (ChatUser user: users)
		{
			int messageCount = messageRepository.numOfUnseenMessageBySender(principalUsername, user.getUsername());
			
			UnseenDTO unseenDTO = new UnseenDTO();
			unseenDTO.sender = user.getUsername();
			unseenDTO.numberOfMessages = messageCount;
			
			unseenDTOs.add(unseenDTO);
		}
		
		
		model.addAttribute("users", unseenDTOs);
		return "home";
	}
	
	@GetMapping("/chat/{receiver}")
	public String chat(Model model, @PathVariable String receiver, Authentication auth)
	{	// SET UNSEEN TO SEEN
		List<Message> unseenMessages = messageRepository.unseenMessageBySender(auth.getName(), receiver);
		for (Message message: unseenMessages)
		{
			message.setSeen(true);
			messageRepository.save(message);
		}
		
		// List of previous messages
		List<Message> messages = messageRepository.findByPeople(auth.getName(), receiver);
		List<MessageDTO> messageDTOs = new ArrayList<>();
		for (Message message: messages)
		{
			MessageDTO messageDTO = new MessageDTO(message);
			messageDTOs.add(messageDTO);
		}
		model.addAttribute("messages", messageDTOs);
		model.addAttribute("username", auth.getName());
		model.addAttribute("receiver", receiver);
		return "chat";
	}
}