$(document).ready(function(){
	$("#modify_btn").click(function(){
		window.location.href='./modify.html';
	});

	$("#logout_btn").click(function(){
		$.ajax({

			// ajax로 logout 페이지 이동

		}); 	// end of ajax
	});		// end of logout

	$("#random_btn").click(function(){
		window.location.href="./random.html";
	});

	$("#friends_btn").click(function(){
		window.location.href="./friends.html";
	});

	$("#random_set_btn").click(function (){
		// 알람 세팅 이후 동작 세팅 

	});
})