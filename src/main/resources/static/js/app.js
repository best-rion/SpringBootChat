function formatCurrentDate(date)
{
    // Options for date and time formatting
    const options = {
        day: 'numeric',
        month: 'short',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
        hour12: true
    };

    // Format date
    return new Intl.DateTimeFormat('en-US', options).format(date).replace(',', '');
}

const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/gs-guide-websocket'
});

window.onload = ()=>{
	
	stompClient.activate();
	window.scroll(0, document.documentElement.scrollHeight)
	
	
	// format date
	
	var dates = document.getElementsByClassName("date");
	for (var i=0; i< dates.length; i++)
	{
		var d = new Date(dates[i].innerText)
		dates[i].innerHTML = formatCurrentDate(d);
	}
}

stompClient.onConnect = (frame) => {
    console.log('Connected: ' + frame +'End of frame');
    stompClient.subscribe('/user/queue/private', (message) => {
        showMessage(JSON.parse(message.body));
    });
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

window.onbeforeunload = function () {
	stompClient.deactivate();
	console.log("Disconnected");
};

function sendMessage() {
	
	if ($("#message").val() != '')
	{
		let friend = $("#friend").val();
		
		stompClient.publish({
		    destination: "/app/receiver/"+friend,
		    body: JSON.stringify({'content': $("#message").val()})
		});

		$("#message").val('');
	}
}

function showMessage(message)
{
	var d = formatCurrentDate(new Date());
	if (message.sender === $("#principal").val())
	{
		if (message.receiver === $("#friend").val()) // OTHERWISE MessageController.java WILL TRY TO PUSH MY REPLIES TO OTHER FRIENDS IN THI
		{
			// Has it been seen???
			var status = "Unseen"
			if (message.seen)
			{
				status = "Seen"
			}
			
			$("#chat").append("<tr>"+
								"<td class=\"right\">"+
									"<p><span class=\"text\">"+message.content+"</span><p>"+
									"<small  class=\"date\">"+d+"</small>"+
									"(<span class=\""+status.toLowerCase()+"\">"+status+"</span>)"+
								"</td>"+
							  "</tr>");
		}
	}
	else // SOMEONE ELSE SENT THIS MESSAGE
	{
		if (message.sender === $("#friend").val())
		{
			$("#chat").append("<tr>"+
								"<td class=\"left\">"+
									"<p><span class=\"text\">"+message.content+"</span><p>"+
									"<small  class=\"date\">"+d+"</small>"+
								"</td>"+
							  "</tr>");
							  
			//	If it has been printed on the left side. it means I(the recipient of this message) have seen it.
		  	  	fetch
		  	  	(
		  	  		"http://localhost:8080/seen",
		  	  		{
		  	  	        method: 'PUT',
		  	  	        headers: {'Content-Type': 'text/plain'},
		  	  	        body: message.id
		  	      	}
		  	  	)
		  	  	.then(response=>response.text())
		  	  	.then(body=>{
		  	  		if (body === "1")
		  	  		{
		  	  			console.log("seen success")
		  	  		} 
		  	  	})
		}
	}
}

$(function () {
	$("form").on('submit', (e) => e.preventDefault());
    $("#send").click(() => sendMessage());
});
