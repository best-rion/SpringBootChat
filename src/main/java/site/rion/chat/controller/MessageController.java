package site.rion.chat.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import site.rion.chat.model.Message;
import site.rion.chat.repository.MessageRepository;
import site.rion.chat.service.SaveMessage;
import site.rion.chat.ws.ReceivedMessage;


@Controller
public class MessageController
{
	@Autowired
	SaveMessage saveMessage;
	
	@Autowired
	MessageRepository messageRepository;
	
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	
	@MessageMapping("/receiver/{receiver}")
	public void messaging(
			@Payload ReceivedMessage receivedMessage,
			@DestinationVariable String receiver,
			final Principal principal
			) throws Exception
	{	
		Message message = saveMessage.save( principal.getName(), receiver, HtmlUtils.htmlEscape(receivedMessage.getContent()) );
	
		simpMessagingTemplate.convertAndSendToUser( receiver , "/queue/private", message);
		// Send this message to friend
		Thread.sleep(100); // if he sees the message in 100 milliseconds, The database must have been updated.
		message = messageRepository.findById(message.getId()); // Update message object to get update from database
		simpMessagingTemplate.convertAndSendToUser( principal.getName() , "/queue/private", message);
		// Finally I got the message
	}  
}