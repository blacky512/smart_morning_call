$(document).ready(function(){


	$.ajax ({
		type: "POST",
		dataType: "JSON",
		url: './php/search.php',
		success : function(result){
			console.log(result);
			if(result.result=="true"){

				//jquery 가 이미 값을 채움.
				
				$("#pw").val(result.pw);
				$(".sex[value=" + result.sex + "]").attr('checked','checked');
				$("#age").val(result.age);
				alert(result.message);
			}
			else{
				alert(result.message);
			}
		},	// end of success
		error: function(request, status, error){
			alert("random.js; 서버 통신 ajax 에러");
			console.log(request.status);
			console.log(request.responseText);
			console.log(status);
			console.log(error);
		}	// end of error
	});	// end of ajax

	$('#modify_btn').click(function(){
		var val = {};

		val['pw']=$("#pw").val();
		val['pw2'] = $("#pw2").val();
		val['sex']=$(".sex:checked").val();
		val['age']=$("#age").val();

		$.ajax({
			type: "POST",
			dataType: "JSON",	
			url: "./php/modify.php",
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
				alert("modify.js; 서버 통신 ajax 에러");
				console.log(request.status);
				console.log(request.responseText);
				console.log(status);
				console.log(error);
			}	// end of error
		}); 	// end of ajax
	});		// end of click
});		// end of ready		