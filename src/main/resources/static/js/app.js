const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/gs-guide-websocket'
});

var alreadyConnected = false;
window.onload = ()=>{
	if (!alreadyConnected)
	{
		stompClient.activate();
	}
	window.scroll(0, document.documentElement.scrollHeight)
}

stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame +'End of frame');
    stompClient.subscribe('/user/queue/private', (greeting) => {
        showGreeting(JSON.parse(greeting.body));
    });
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
}

window.onbeforeunload = function () {
	stompClient.deactivate();
	setConnected(false);
	console.log("Disconnected");
};

function sendMessage() {
	
	if ($("#message").val() != '')
	{
		var receiver = $("#receiver").val();
		stompClient.publish({
		    destination: "/app/receiver/"+receiver,
		    body: JSON.stringify({'content': $("#message").val()})
		});

		$("#message").val('');
	}
}
function formatCurrentDate() {
    const now = new Date();

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
    return new Intl.DateTimeFormat('en-US', options).format(now).replace(',', '');
}

function showGreeting(message) {

	var d = formatCurrentDate();
	if (message.sender === $("#username").val())
	{
		if (message.receiver === $("#receiver").val())
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
									"<small  class=\"text-secondary\">"+d+"</small>"+
									"(<span>"+status+"</span>)"+
								"</td>"+
							  "</tr>");
		}
	}
	else
	{

		if (message.sender === $("#receiver").val())
		{
			$("#chat").append("<tr>"+
								"<td class=\"left\">"+
									"<p><span class=\"text\">"+message.content+"</span><p>"+
									"<small  class=\"text-secondary\">"+d+"</small>"+
								"</td>"+
							  "</tr>");
							  
		  // If it is printed on the left side. it means it has been seen

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