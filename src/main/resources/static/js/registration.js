$("#form").submit(function(e) {
    e.preventDefault();
    $.post("/api/users/new", $(this).serialize())
	.error(console.log("Went wrong"))
    .done(console.log("Done well"))
})