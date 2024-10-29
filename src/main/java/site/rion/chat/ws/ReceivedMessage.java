package site.rion.chat.ws;

public class ReceivedMessage 
{
	private String content;
	
	public ReceivedMessage() {}
	public ReceivedMessage(String content)
	{
		this.setContent(content);
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}