$(document).ready(function(){

	$('#join_btn2').click(function(){
		var val = {};

		val['id']=$("#id").val();
		val['pw']=$("#pw").val();
		val['pw2']=$("#pw2").val();
		val['sex']=$(".sex:checked").val();
		val['age']=$("#age").val();

		$.ajax({
			type: "POST",
			dataType: "JSON",	
			url: "./php/join.php",
			data: val,
			success: function(result){
				console.log(result);
				if(result.result=="true"){
					alert(result.message);
					window.location.href='./random.html';					
				}
				else{
					alert(result.message);
				}
			},	// end of success
			error: function(request, status, error){
				alert("join.js; 서버 통신 ajax 에러");
				console.log(request.status);
				console.log(request.responseText);
				console.log(status);
				console.log(error);
			}	// end of error
		}); 	// end of ajax
	});		// end of click
});		// end of ready		