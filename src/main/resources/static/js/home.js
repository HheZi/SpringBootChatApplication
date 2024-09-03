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
		tag.text(`${message.content}`)
	}
	else{
		tag.text("");
		tag.append(`${message.content}<span class="badge badge-pill badge-light float-right">
		<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-envelope-exclamation-fill" viewBox="0 0 16 16">
		  <path d="M.05 3.555A2 2 0 0 1 2 2h12a2 2 0 0 1 1.95 1.555L8 8.414zM0 4.697v7.104l5.803-3.558zM6.761 8.83l-6.57 4.026A2 2 0 0 0 2 14h6.256A4.5 4.5 0 0 1 8 12.5a4.49 4.49 0 0 1 1.606-3.446l-.367-.225L8 9.586zM16 4.697v4.974A4.5 4.5 0 0 0 12.5 8a4.5 4.5 0 0 0-1.965.45l-.338-.207z"/>
		  <path d="M12.5 16a3.5 3.5 0 1 0 0-7 3.5 3.5 0 0 0 0 7m.5-5v1.5a.5.5 0 0 1-1 0V11a.5.5 0 0 1 1 0m0 3a.5.5 0 1 1-1 0 .5.5 0 0 1 1 0"/>
		</svg>`);
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
		$("#usernameProfileContainer").on('click', () => getInfoAboutUser(usernameOfCurrentUser))
		
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
	const row = `<div>
				<div class="message ${tagPos}">
                  <div class="message-content">
                   <span class="sender-name">${message.sender}</span>
                        <p>${message.content}</p>
                        <span class="timestamp">${message.timestamp}</span>
                 </div>
              </div></div>`;
           //  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" 
			//		fill="currentColor" class="bi bi-trash-fill " viewBox="0 0 16 16">
			//	  <path d="M2.5 1a1 1 0 0 0-1 1v1a1 1 0 0 0 1 1H3v9a2 2 0 0 0 2 2h6a2 2 0 0 0 2-2V4h.5a1 1 0 0 0 1-1V2a1 1 0 0 0-1-1H10a1 1 0 0 0-1-1H7a1 1 0 0 0-1 1zm3 4a.5.5 0 0 1 .5.5v7a.5.5 0 0 1-1 0v-7a.5.5 0 0 1 .5-.5M8 5a.5.5 0 0 1 .5.5v7a.5.5 0 0 1-1 0v-7A.5.5 0 0 1 8 5m3 .5v7a.5.5 0 0 1-1 0v-7a.5.5 0 0 1 1 0"/>
			//	</svg>
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
					'${chat.chatId}')" id="${chat.chatName.replaceAll(" ", "_")}">
		                   <img src="https://via.placeholder.com/40" alt="Group Icon">
		                   <div class="group-details">
		                       <div class="group-name">${groupName}</div>
		                       <div class="last-message">${chat.lastMessage}</div>
		                   </div>
		               </div>`;
	$("#group-container").append(row);			
	stompClient.subscribe(chat.groupSocketUrl, onMessageReceived);
}


function getChat(groupName, chatType, groupSocketUrl, chatId){
	if(groupName === currentChatName)
		return;
		
	$(`#${groupName.replaceAll(" ", "_")} .badge`).remove();
	$("#sendInput").val("");
	$(".chat-body").text("");
	$("#sendBut").prop('disabled', false);
	$("#sendInput").prop('disabled', false);
	$("#chatName").text(calculateChatName(groupName, chatType));
	[currentChatName, currentGroupSocketUrl] = [groupName, groupSocketUrl];
	
	$.get(`chat/messages/${chatId}`)
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
					showMessage(jqXHR.responseJSON.message || "Something went wrong", "error-message")
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
			$("#profileModalFooter").append(`<button type="button" id="logoutBut" class="btn btn-primary">Logout</button>`);
			$("#profileModalFooter").append(`<button type="button" id="saveUserProfile" class="btn btn-primary">Save changes</button>`);
			$("#logoutBut").on('click', function(){
				document.location.href = "/logout";
			})
			$("#saveUserProfile").on('click', function(){
				$.ajax({
				    url: '/api/users/'+usernameOfCurrentUser,
				    type: 'PUT',
					contentType: "application/json",
				    data: JSON.stringify({
						username: $("#profileUsername").val(),
						description: $("#profileDesc").val()
					}),
				    dataType: "json",
				    success: function() {
				        showMessage("You update a profile", "success-messagew")
				    },
				    error: function(resp) {
						showMessage(resp.responseJSON.message, "error-message")
			        }
				});
			})		
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
					showMessage(resp.responseJSON.message, "error-message");
				})
			})
		}
	})
	.fail((fail) =>  {
		showMessage(fail.responseJSON.message || "Something went wrong", "error-message");
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


