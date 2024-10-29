package site.rion.chat.ws;

public class MessageToSend {
	private int id;
	private String sender;
	private String content;
	private String receiver;
	
	public MessageToSend() {
	}
	
	public MessageToSend(String sender, String receiver, String content) {
		this.sender = sender;
		this.receiver= receiver;
	    this.content = content;
	}
	
	public String getContent() {
	    return content;
	}

	public String getSender() {
		return sender;
	}
	public String getReceiver() {
		return receiver;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}