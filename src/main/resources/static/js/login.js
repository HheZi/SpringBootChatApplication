$(document).ready(() => {
	if((new URLSearchParams(window.location.search)).has("error")){
		showMessage("Wrong input", "error-message");
	}
	else if((new URLSearchParams(window.location.search)).has("logout")){
		showMessage("You have logout", "success-message");
	}
});