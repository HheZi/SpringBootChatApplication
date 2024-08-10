let stompClient;
let username;

document.addEventListener("DOMContentLoaded", connect)

function onMessageReceived(message){

}


function connect(){
	stompClient = Stomp.over(new SockJS('/ws'));
    stompClient.connect({}, onConnected);
}

function onConnected(){
	$.get("/api/users/auth")
	.done(async (resp) => {
		username = resp;
		
		
		$.get(`/chat?username=${resp}`)
	    .then((data) => {
			data.forEach((group) => {
		        const row = `<div class="group-item" onclick="getChat('${group.groupName}')">
		                   <img src="https://via.placeholder.com/40" alt="Group Icon">
		                   <div class="group-details">
		                       <div class="group-name">${group.groupName}</div>
		                       <div class="last-message">Last message</div>
		                   </div>
		               </div>`;
				$("#group-container").append(row);			
			})
	    	stompClient.connect(data.groupSocketUrl, onMessageReceived);
	    })
	})
	
}

function getChat(groupName){
	$(".chat-body").text("");
	$("#chatName").text(groupName);
	$.get(`chat/messages?groupName=${groupName}`)
	.done((data) =>{
		data.forEach((mes) => {
			let tagPos;
			if(username === mes.sender){
				tagPos = "float-right";
			}
			else{
				tagPos = "float-left";
			}
			const row = `<div class="message ${tagPos}">
                        <div class="message-content">
                            <span class="sender-name">${mes.sender}</span>
                            <p>${mes.message}</p>
                            <span class="timestamp">${mes.timestamp}</span>
                        </div>
                    </div>`;
			$(".chat-body").append(row);
			
		})
	});
}

$("#search-input").on("input", function(){
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
