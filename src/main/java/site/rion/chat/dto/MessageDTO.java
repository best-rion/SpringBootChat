package site.rion.chat.dto;

import java.text.SimpleDateFormat;

import site.rion.chat.model.Message;

public class MessageDTO
{
	private String sender;
	private String content;
	private String time;
	private boolean seen;
	
	public MessageDTO(Message message)
	{
		this.sender = message.getSender();
		this.content = message.getContent();
		this.seen = message.isSeen();
		
		SimpleDateFormat df = new SimpleDateFormat("MMM d yyyy, hh:mm aaa");
		this.time = df.format(message.getTime());
	}
	
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}

	public boolean isSeen() {
		return seen;
	}

	public void setSeen(boolean seen) {
		this.seen = seen;
	}
	
}