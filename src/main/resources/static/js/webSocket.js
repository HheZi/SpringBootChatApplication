let stompClient;
let username;
let currentGroupSocketUrl;
let currentGroupName;

document.addEventListener("DOMContentLoaded", connect)

function onMessageReceived(resp){
	const message = JSON.parse(resp.body);
	var tag = $(`#${message.groupName.replaceAll(" ", "_")} .last-message`);
	if(message.groupName === currentGroupName){
		displayMessage(message);	
		tag.text(message.content)
	}
	else{
		tag.text("");
		tag.append(`${message.content} <span class="badge badge-pill badge-light float-right">!</span>`);
	}
	$("#group-container").prepend($(`#${message.groupName.replaceAll(" ", "_")}`)).prependTo($("#group-container"));
}

function connect(){
	stompClient = Stomp.over(new SockJS('/ws'));
    stompClient.connect({}, onConnected);
}

function onConnected(){
	$.get("/api/users/auth")
	.done((resp) => {
		username = resp;
		$("#username").text(username);
		
		$.get(`/chat/groups?username=${resp}`)
	    .then((data) => {
			data.forEach((group) => {
		        const row = `<div class="group-item" onclick="getChat('${group.groupName}', 
		        '${group.groupSocketUrl}')" id="${group.groupName.replaceAll(" ", "_")}">
		                   <img src="https://via.placeholder.com/40" alt="Group Icon">
		                   <div class="group-details">
		                       <div class="group-name">${group.groupName}</div>
		                       <div class="last-message">${group.lastMessage}</div>
		                   </div>
		               </div>`;
				$("#group-container").append(row);			
	    	stompClient.subscribe(group.groupSocketUrl, onMessageReceived);
			})
	    })
	})
	
}

function displayMessage(message){
	let tagPos = (username === message.sender) ? "float-right" : "float-left";
	const row = `<div class="message ${tagPos}">
                  <div class="message-content">
                   <span class="sender-name">${message.sender}</span>
                        <p>${message.content}</p>
                        <span class="timestamp">${message.timestamp}</span>
                 </div>
              </div>`;
	$(".chat-body").append(row);
	$(".chat-body").scrollTop($(".chat-body").prop('scrollHeight'));
}


function getChat(groupName, groupSocketUrl){
	if(groupName === currentGroupName)
		return;
	
	$(`#${groupName.replaceAll(" ", "_")} .badge`).remove();
	$("#sendInput").val("");
	$(".chat-body").text("");
	$("#sendBut").prop('disabled', false);
	[currentGroupSocketUrl, currentGroupName] = [groupSocketUrl, groupName];
	$("#chatName").text(groupName);

	$.get(`chat/messages?groupName=${groupName}`)
	.done((data) =>{
		data.forEach((message) => {
			displayMessage(message)
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
	$("#sendInput").val("");
});

$("#search-input").on("keypress", function(e){
			if(!(e.keyCode == 13))
				return;
            $.get(`/api/users?username=${$(this).val()}`)
            .done(function(data){
				$(".search-dropdown").text("");
				if(data.length == 0){
					$(".search-dropdown").append("<div class='text-center m-3'>User is not found</div>");					
				}
				data.forEach(user => {
                    const item  = `<a href="#" class="dropdown-item text-center">
                        <img src="${user.avatarUrl}" alt="avatar" class="rounded-circle" width="30" height="30">
                        <span class="text-white">${user.username}</span>
                    </a>`;
                    $(".search-dropdown").append(item);
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
        
$("#search-input").on("focusout", function(e){
	$(".search-dropdown").text("");
})