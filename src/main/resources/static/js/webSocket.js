let stompClient;


document.addEventListener("DOMContentLoaded", connect)

function onMessageReceived(message){

}


function connect(){
	stompClient = Stomp.over(new SockJS('/ws'));
    stompClient.connect({}, onConnected);
}

function onConnected(){
	$.get("/api/users/auth")
	.done(async (data) => {
		const username = data;
		console.log(`username is ${username}, data is ${data}`)
		
		$.get(`/chat?username=${username}`)
	    .then((data) => {
			data.forEach((group) => {
		        const row = `<div class="group-item">
		                   <img src="https://via.placeholder.com/40" alt="Group Icon">
		                   <div class="group-details">
		                       <div class="group-name">${group.groupName}</div>
		                       <div class="last-message">Last message</div>
		                   </div>
		               </div>`;
				$("#group-container").append(row);			
			})
	    	stompClient.connect(data.groupSocketUrl, onMessageReceived)
	    })
	})
	
}

function getChat(){
	
}
