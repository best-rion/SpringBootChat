package site.rion.chat.controller;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import site.rion.chat.dto.MessageNotification;
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
		
		// ALL USER EXCEPT ME
		List<ChatUser> users = userRepository.findAllExcept(auth.getName());
		
		// I AM ONLINE NOW
		ChatUser currentUser = userRepository.findByUsername(auth.getName());
		currentUser.setActive(true);// For now this does nothing. I will find out a way to determine if a user is online.
		userRepository.save(currentUser);
		
		
		// MESSAGE NOTIFICATION( HOW MANY MESSAGE ARE PENDING) PER USER
		List<MessageNotification> notifications = new ArrayList<>();
		for (ChatUser user: users)
		{
			MessageNotification notification = new MessageNotification();
			
			notification.sender = user.getUsername();
			notification.numberOfMessages = messageRepository.numOfUnseenMessageBySender(auth.getName(), user.getUsername());
			
			notifications.add(notification);
		}
		
		model.addAttribute("notifications", notifications);
		return "home";
	}
	
	@GetMapping("/chat/{friend}")
	public String chat(Model model, @PathVariable String friend, Authentication auth)
	{	
		// SET UNSEEN TO SEEN, ONLY MY FRIEND'S MESSSAGES
		List<Message> unseenMessages = messageRepository.unseenMessageBySender(auth.getName(), friend);
		for (Message message: unseenMessages)
		{
			message.setSeen(true);
			messageRepository.save(message);
		}
		
		// LIST OF ALL MESSAGES, MINE AND MY FRIEND'S
		List<Message> messages = messageRepository.findByPeople(auth.getName(), friend);
		
		model.addAttribute("messages", messages);
		model.addAttribute("principal", auth.getName());
		model.addAttribute("friend", friend);
		return "chat";
	}
}