package site.rion.chat.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import site.rion.chat.service.SaveMessage;
import site.rion.chat.ws.MessageToSend;
import site.rion.chat.ws.ReceivedMessage;


@Controller
public class MessageController
{
	@Autowired
	SaveMessage saveMessage;
	
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	
	  @MessageMapping("/receiver/{receiver}")
	  public void messaging(
			  @Payload ReceivedMessage receivedMessage,
			  @DestinationVariable String receiver,
			  final Principal principal
			  ) throws Exception
	  {
		  MessageToSend messageToSend =  new MessageToSend( principal.getName(), receiver, HtmlUtils.htmlEscape(receivedMessage.getContent()) );
		  
		  int id = saveMessage.save( principal.getName(), receiver, HtmlUtils.htmlEscape(receivedMessage.getContent()) );
		 
		  messageToSend.setId(id);

		  simpMessagingTemplate.convertAndSendToUser( receiver , "/queue/private", messageToSend);
		  
		  Thread.sleep(10);
		  
		  simpMessagingTemplate.convertAndSendToUser( principal.getName() , "/queue/private", messageToSend);
	  }
	  
	  
}