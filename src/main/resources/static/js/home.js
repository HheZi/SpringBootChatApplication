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