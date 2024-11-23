package site.rion.chat.ws;

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


@Controller
public class MessageController
{
	@Autowired
	SaveMessage saveMessage;
	
	@Autowired
	MessageRepository messageRepository;
	
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	
	@MessageMapping("/send-to/{receiver}")
	public void messaging(
			@Payload ReceivedMessage receivedMessage,
			@DestinationVariable String receiver,
			final Principal principal
			) throws Exception
	{	
		Message message = saveMessage.save( principal.getName(), receiver, HtmlUtils.htmlEscape(receivedMessage.getContent()) );
	
		simpMessagingTemplate.convertAndSendToUser( receiver , "/queue/private", message);

		Thread.sleep(100); // Wait 0.1sec to update seen status in database. See "SeenController" class, how it updates.
		
		message = messageRepository.findById(message.getId()); // Send the message back to me with correct seen status
		
		simpMessagingTemplate.convertAndSendToUser( principal.getName() , "/queue/private", message);
	}  
}