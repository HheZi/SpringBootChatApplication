let stompClient;
let usernameOfCurrentUser;
let currentGroupSocketUrl;
let currentChatName;
let usersInGroups = [];

function onMessageReceived(resp){
	const message = JSON.parse(resp.body);
	var tag = $(`#${message.chatName.replaceAll(" ", "_")} .last-message`);
	if(message.chatName === currentChatName){
		displayMessage(message);	
		tag.text(`${message.sender}: ${message.content}`)
	}
	else{
		tag.text("");
		tag.append(`${message.sender}: ${message.content} <span class="badge badge-pill badge-light float-right">!</span>`);
	}
	$("#group-container").prepend($(`#${message.chatName.replaceAll(" ", "_")}`)).prependTo($("#group-container"));
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
		usernameOfCurrentUser = usernameResp;
		stompClient.subscribe("/user/chat/creation", onGroupReceived)
		$("#username").text(usernameResp);
		
		$.get(`/chat/groups`)
	    .then((data) => {
			data.forEach((group) => {
		        displayChat(group)
			})
	    })
	})
	
}

function createGroup(groupName, description, usersInGroups, chatType){
	stompClient.send("/app/chat/creation", {}, JSON.stringify({
		'chatName': groupName,
		'description': description,
		'usersName': usersInGroups,
		'chatType': chatType
	}))
}

function displayMessage(message){
	let tagPos = (usernameOfCurrentUser === message.sender) ? "float-right" : "float-left";
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

function calculateChatName(chatName, chatType){
	if(chatType === 'PRIVATE'){
		return chatName.split("_").find((userName) => userName !== usernameOfCurrentUser);
	}
	else
		return chatName;
	
}

function displayChat(chat){
	let groupName = calculateChatName(chat.chatName, chat.chatType);
	
	const row = `<div class="group-item" onclick="getChat('${chat.chatName}', '${chat.chatType}', '${chat.groupSocketUrl}', 
		        '${chat.groupSocketUrl}')" id="${chat.chatName.replaceAll(" ", "_")}">
		                   <img src="https://via.placeholder.com/40" alt="Group Icon">
		                   <div class="group-details">
		                       <div class="group-name">${groupName}</div>
		                       <div class="last-message">${chat.lastMessage}</div>
		                   </div>
		               </div>`;
	$("#group-container").append(row);			
	stompClient.subscribe(chat.groupSocketUrl, onMessageReceived);
}


function getChat(groupName, chatType, groupSocketUrl){
	if(groupName === currentChatName)
		return;
	
	$(`#${groupName.replaceAll(" ", "_")} .badge`).remove();
	$("#sendInput").val("");
	$(".chat-body").text("");
	$("#sendBut").prop('disabled', false);
	$("#sendInput").prop('disabled', false);
	$("#chatName").text(calculateChatName(groupName, chatType));
	[currentChatName, currentGroupSocketUrl] = [groupName, groupSocketUrl];

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
                    const item  = `<a ${(inModal) ? `onclick='addToUsersInGroup("${user.username}")'` : 
                    			`onclick='getInfoAboutUser("${user.username}")'` } 
                    				class="dropdown-item text-center">
                        <img src="${user.avatarUrl}" alt="avatar" class="rounded-circle" width="30" height="30">
                        <span class="text-white">${user.username}</span>
                    </a>`;
                    $(dropdownClass).append(item);
                }); 
                if(!inModal)
               		$("#clearUsersDropdown").removeClass("d-none");
			})
            .fail(function(fail) {
                if (fail.status >= 400 && fail.status < 500) {
					showMessage(jqXHR.responseJSON.message, "error-message")
                } else {
					showMessage("An unexpected error occurred. Please try again later.", "error-message")
                }
            })
        }
        

function addToUsersInGroup(usernameToAdd){
	usersInGroups.push(usernameToAdd);
	const tag = `<span class="usersInGroupContainer mr-2" id="${usernameToAdd}User">
               <span>${usernameToAdd}</span> <a class="deleteBut" onclick="deleteUserFromGroup('${usernameToAdd}')">&times;</a>
    </span>`;
     $("#selectedUsers").append(tag);
     $(".groupUsers-search-dropdown").text("");	
	 $("#searchUsersInModal").val("");
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

function getInfoAboutUser(username){
	$("#profileModalFooter").text("");
	$("#profileDesc").addClass("text-dark");
	$("#profileUsername").addClass("text-dark");
	$('#profileDesc').attr('readonly', true);
	$('#profileUsername').attr('readonly', true);
	$.get(`/api/users/${username}`)
	.done((user) => {
		$("#profileUsername").val(user.username);
		$("#profileDesc").val(user.description || user.username + " doesn't have a profile description");
		$("#profileModal").modal("show");
		if(user.username === usernameOfCurrentUser){
			$("#profileDesc").val(user.description || "");
			$("#profileDesc").removeClass("text-dark");
			$("#profileUsername").removeClass("text-dark");
			$('#profileDesc').attr('readonly', false);
			$('#profileUsername').attr('readonly', false);
			$("#profileModalFooter").append(`<button type="button" class="btn btn-primary">Save changes</button>`);		
		}
		else{
			$("#profileModalFooter").append(`<button type="button" id="writeToUserBut" class="btn btn-primary">Write to ${user.username}</button>`);
			$("#writeToUserBut").on("click", function(){
				$.get("/chat/"+user.username)
				.done(() =>  {
					createGroup(`${user.username}_${usernameOfCurrentUser}`, null, [user.username, usernameOfCurrentUser], "PRIVATE")
					$("#profileModal").modal("hide");
				})
				.fail((resp) =>  {
					showMessage(resp.responseJSON.message,"error-message");
				})
			})
		}
	})
	.fail((fail) =>  {
		showMessage(fail.responseJSON.message, "error-message");
	})
}


$(document).ready(function () {
	connect();
	
	$("#sendBut").on("click", (e) => {
    if($("sendInput").val())
    	return;
    const message = {
		content: $("#sendInput").val(),
		sender: usernameOfCurrentUser,
		chatName: currentChatName
	}
	stompClient.send("/app" + currentGroupSocketUrl, {}, JSON.stringify(message))
	$("#sendInput").val("");
	});
	
	$("#searchUsers").on("keypress",(e) => searchUsers(e, ".search-dropdown", "#searchUsers", false))
	
	$("#clearUsersDropdown").on("click", function(){
		$("#searchDropdown").text("");
		$(this).addClass("d-none");
		$("#searchUsers").val("");
	});

	$("#searchUsersInModal").on("keypress",(e) => searchUsers(e, ".groupUsers-search-dropdown", "#searchUsersInModal", true))
	
    $('#openModalButton').click(function (e) {
        e.preventDefault();
        $('#createGroupModal').modal('show');
    });
    
    $('#closeModalButton, .btn-secondary').click(function () {
        $('#createGroupModal').modal('hide');
        usersInGroups = [];
    });

    $('#createGroupButton').click(function () {
		if(usersInGroups.length === 0){
			 showMessage("At least one user need to be in group", "error-message")
		}
		createGroup($('#groupNameInput').val(), $('#groupDescriptionInput').val(), usersInGroups, "GROUP")
		usersInGroups = [];
		$("#selectedUsers").text("");
		$('#groupNameInput').val("");
	    $('#groupDescriptionInput').val("");
	    showMessage("You have created the group!", "success-message");
    });
});


