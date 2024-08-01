$("#form").submit(function(e) {
            e.preventDefault();
            $.post("/api/users/new", $(this).serialize())
            .done(function(resp) {
                $("#success-message").removeClass("d-lg-none").text("You have created an account!").addClass("show");
            })
            .fail(function(jqXHR, textStatus, errorThrown) {
                if (jqXHR.status >= 400 && jqXHR.status < 500) {
                    $("#error-message").removeClass("d-lg-none").text(jqXHR.responseJSON.message).addClass("show");
                } else {
                    $("#error-message").removeClass("d-lg-none").text("An unexpected error occurred. Please try again later.").addClass("show");
                }
                
            })
            .always(function(){
                setTimeout(function() {
                    $(".alert").removeClass("show");
                }, 5000);
            })
        });