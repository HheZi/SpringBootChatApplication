let stompClient;
let username;
let currentGroupSocketUrl;
let currentGroupName;
let usersInGroups = [];

function onMessageReceived(resp){
	const message = JSON.parse(resp.body);
	var tag = $(`#${message.groupName.replaceAll(" ", "_")} .last-message`);
	if(message.groupName === currentGroupName){
		displayMessage(message);	
		tag.text(`${message.sender}: ${message.content}`)
	}
	else{
		tag.text("");
		tag.append(`${message.sender}: ${message.content} <span class="badge badge-pill badge-light float-right">!</span>`);
	}
	$("#group-container").prepend($(`#${message.groupName.replaceAll(" ", "_")}`)).prependTo($("#group-container"));
}

function onGroupReceived(resp){
	const chat = JSON.parse(resp.body);
	displayChat(chat);
}

function connect(){
	stompClient = Stomp.over(new SockJS('/ws'));
    stompClient.connect({}, onConnected);
}

function onConnected(){
	$.get("/api/users/auth")
	.done((usernameResp) => {
		username = usernameResp;
		stompClient.subscribe("/user/"+usernameResp, onGroupReceived)
		$("#username").text(usernameResp);
		
		$.get(`/chat/groups?username=${usernameResp}`)
	    .then((data) => {
			data.forEach((group) => {
		        displayChat(group)
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

function displayChat(chat){
	const row = `<div class="group-item" onclick="getChat('${chat.groupName}', 
		        '${chat.groupSocketUrl}')" id="${chat.groupName.replaceAll(" ", "_")}">
		                   <img src="https://via.placeholder.com/40" alt="Group Icon">
		                   <div class="group-details">
		                       <div class="group-name">${chat.groupName}</div>
		                       <div class="last-message">${chat.lastMessage}</div>
		                   </div>
		               </div>`;
	$("#group-container").append(row);			
	stompClient.subscribe(chat.groupSocketUrl, onMessageReceived);
}


function getChat(groupName, groupSocketUrl){
	if(groupName === currentGroupName)
		return;
	
	$(`#${groupName.replaceAll(" ", "_")} .badge`).remove();
	$("#sendInput").val("");
	$(".chat-body").text("");
	$("#sendBut").prop('disabled', false);
	$("#sendInput").prop('disabled', false);
	[currentGroupSocketUrl, currentGroupName] = [groupSocketUrl, groupName];
	$("#chatName").text(groupName);

	$.get(`chat/messages?groupName=${groupName}`)
	.done((data) =>{
		data.forEach((message) => {
			displayMessage(message)
		})
	});
}

function searchUsers(e, dropdownClass, inputClass, inModal = false){
			if(!(e.keyCode == 13))
				return;
				
            $.get(`/api/users?username=${$(inputClass).val()}`)
            .done(function(data){
				$(dropdownClass).text("");
				if(data.length == 0){
					$(dropdownClass).append("<div class='text-center m-3'>User is not found</div>");					
				}
				data.forEach(user => {
                    const item  = `<a ${(inModal) ? `onclick='addToUsersInGroup("${user.username}")'` : ""} 
                    				class="dropdown-item text-center">
                        <img src="${user.avatarUrl}" alt="avatar" class="rounded-circle" width="30" height="30">
                        <span class="text-white">${user.username}</span>
                    </a>`;
                    $(dropdownClass).append(item);
                }); 
			})
            .fail(function(jqXHR, textStatus, errorThrown) {
                if (jqXHR.status >= 400 && jqXHR.status < 500) {
					showMessage(jqXHR.responseJSON.message, "error-message")
                } else {
					showMessage("An unexpected error occurred. Please try again later.", "error-message")
                }
            })
            .always(function(){
                setTimeout(function() {
                    $(".alert").removeClass("show");
                }, 5000);
            })
        }
        

function addToUsersInGroup(usernameToAdd){
	usersInGroups.push(usernameToAdd);
	const tag = `<span class="usersInGroupContainer mr-2" id="${usernameToAdd}User">
               <span>${usernameToAdd}</span> <a class="deleteBut" onclick="deleteUserFromGroup('${usernameToAdd}')">X</a>
    </span>`;
     $("#selectedUsers").append(tag);
     $(".groupUsers-search-dropdown").text("");	
}

function deleteUserFromGroup(usernameToDel){
	usersInGroups.splice(usersInGroups.indexOf(usernameToDel))
	$(`#${usernameToDel}User`).remove();
}

function showMessage(message, idTag){
	$("#" + idTag).removeClass("d-lg-none").text(message).addClass("show");	
	setTimeout(function() {
    	$(".alert").removeClass("show");
    }, 5000);
}


$(document).ready(function () {
	connect();
	
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
	
	$("#searchUsers").on("keypress",(e) => searchUsers(e, ".search-dropdown", "#searchUsers", false))
	
	$("#searchUsers").on("focusout", function(){
		$("#searchDropdown").text("");
	})

	$("#searchUsersInModal").on("keypress",(e) => searchUsers(e, ".groupUsers-search-dropdown", "#searchUsersInModal", true))
	
    $('#openModalButton').click(function (e) {
        e.preventDefault();
        $('#createGroupModal').modal('show');
    });

    $('#closeModalButton, .btn-secondary').click(function () {
        $('#createGroupModal').modal('hide');
    });

    $('#createGroupButton').click(function () {
		if(usersInGroups.length === 0){
			 $("#error-message").removeClass("d-lg-none").text("At least one user required").addClass("show");
		}
        $.post("/chat/groups",  JSON.stringify({
			groupName: $('#groupNameInput').val(),
			description: $('#groupDescriptionInput').val(),
			usersName: usersInGroups
		}))
		.done(() => {
        	usersInGroups = [];
			showMessage("You have created the group!", "success-message")
		})
		.fail((resp) => {
			showMessage(resp.responseJSON.message, "error-message")
		})
    });
});


