let stompClient;
let username;
let currentGroupSocketUrl;
let currentGroupName;

document.addEventListener("DOMContentLoaded", connect)

function onMessageReceived(resp){
	const message = JSON.parse(resp.body);
	addMessage(message);
}




function connect(){
	stompClient = Stomp.over(new SockJS('/ws'));
    stompClient.connect({}, onConnected);
}

function onConnected(){
	$.get("/api/users/auth")
	.done((resp) => {
		username = resp;
		
		
		$.get(`/chat?username=${resp}`)
	    .then((data) => {
			data.forEach((group) => {
		        const row = `<div class="group-item" onclick="getChat('${group.groupName}', '${group.groupSocketUrl}')">
		                   <img src="https://via.placeholder.com/40" alt="Group Icon">
		                   <div class="group-details">
		                       <div class="group-name">${group.groupName}</div>
		                       <div class="last-message">Last message</div>
		                   </div>
		               </div>`;
				$("#group-container").append(row);			
	    	stompClient.subscribe(group.groupSocketUrl, onMessageReceived);
			})
	    })
	})
	
}

function addMessage(message){
	let tagPos = (username === message.sender) ? "float-right" : "float-left";
	const row = `<div class="message ${tagPos}">
                  <div class="message-content">
                   <span class="sender-name">${message.sender}</span>
                        <p>${message.content}</p>
                        <span class="timestamp">${message.timestamp}</span>
                 </div>
              </div>`;
	$(".chat-body").append(row);
}


function getChat(groupName, groupSocketUrl){
	$(".chat-body").text("");
	$("#sendBut").prop('disabled', false);
	[currentGroupSocketUrl, currentGroupName] = [groupSocketUrl, groupName];
	$("#chatName").text(groupName);

	$.get(`chat/messages?groupName=${groupName}`)
	.done((data) =>{
		data.forEach((message) => {
			addMessage(message)
		})
	});
}

$("#sendBut").on("click", (e) => {
    if($("sendInput").val())
    	return;
    const message = {
		content: $("#sendInput").val(),
		sender: username,
		groupName: currentGroupName
	}
	stompClient.send("/app" + currentGroupSocketUrl, {}, JSON.stringify(message))
	$("sendInput").val("");
});

$("#search-input").on("input", function(){
			if($(this).val()){
				$("#search-dropdown").val("");
				return;
			}
						
            $.get(`/api/users?username=${$(this).val()}`)
            .done(function(data){
				$("#search-dropdown").text("")
				data.forEach(user => {
                    const item  = `<a href="#" class="dropdown-item text-center">
                        <img src="${user.avatarUrl}" alt="avatar" class="rounded-circle" width="30" height="30">
                        <span class="text-white">${user.username}</span>
                    </a>`;
                    $("#search-dropdown").append(item);
                }); 
			})
            .fail(function(jqXHR, textStatus, errorThrown) {
                if (jqXHR.status >= 400 && jqXHR.status < 500) {
                    $("#error-message").removeClass("d-lg-none").text(jqXHR.responseJSON.message).addClass("show").addClass("show");
                } else {
                    $("#error-message").removeClass("d-lg-none").text("An unexpected error occurred. Please try again later.").addClass("show");
                }
            })
        })
