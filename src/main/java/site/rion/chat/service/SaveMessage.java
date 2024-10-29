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
	
	public int save(String sender, String receiver, String content)
	{
		Message newMessage = new Message();
		
		Date time = new Date();
		
		newMessage.setSender(sender);
		newMessage.setReceiver(receiver);;
		newMessage.setContent(content);
		newMessage.setTime(time);
		newMessage.setSeen(false);
		
		messageRepository.save(newMessage);
		
		System.out.println(newMessage.getId());
		return newMessage.getId();
	}
}