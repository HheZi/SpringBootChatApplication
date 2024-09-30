function showMessage(message, idTag){
    $("#" + idTag).text(message).slideDown("slow");
	setTimeout(function() {
    	$("#" + idTag).slideUp("slow");
    }, 5000);
}
