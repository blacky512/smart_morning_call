$(document).ready(function(){

	$("#join_btn").click(function(){
		window.location.href='./join.html';
	});		// end of click

	$("#login_btn").click(function(){

		var val = {};

		val['id'] = $("#id").val();
		val['pw'] = $("#pw").val();

		console.log("val:"+val);

		$.ajax({
			type: "POST",
			dataType: "json",
			url: "./php/login.php",
			data: val,
			success: function(result){
				console.log("result:"+result);
				if(result.result=="true"){
					alert(result.message);
					//window.location.href='./random.html';
				}
				else{
					alert(result.message);
				}
			}, // end of success
			error: function(request, status, error){
				alert("index.js; 서버 통신 ajax 에러");
				console.log(request.status);
				console.log(request.responseText);
				console.log(status);
				console.log(error);
			}	// end of error
		});	// end of ajax
	});		// end of click
});	// end of ready;