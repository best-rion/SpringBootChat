package site.rion.chat.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import site.rion.chat.model.Message;
import site.rion.chat.repository.MessageRepository;


@Service
public class SaveMessage
{
	@Autowired
	MessageRepository messageRepository;
	
	public Message save(String sender, String receiver, String content)
	{
		Message newMessage = new Message();
		
		newMessage.setSender(sender);
		newMessage.setReceiver(receiver);;
		newMessage.setContent(content);
		newMessage.setTime( new Date() );
		newMessage.setSeen(false);
		
		messageRepository.save(newMessage);
		return newMessage;
	}
}