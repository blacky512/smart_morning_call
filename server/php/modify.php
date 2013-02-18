<?php
	header("Content-Type: text/html; charset=UTF-8");
	session_start();
	include "./connect.php";

//	$temp = json_decode(file_get_contents("php://input"), true);
		//print_r($temp);

	$temp=$_POST;	

	$id =$_SESSION['id'];
	$pw=$temp['pw'];
	$pw2=$temp['pw2'];
	$sex=$temp['sex'];
	$age=$temp['age'];


	$response=array();

	// 중복아이디 검사



	if($pw!=$pw2){

		$response['result']='false';
		$response['message']='비밀번호와 비번 확인이 다릅니다.';
		echo json_encode($response);	

		exit;		
	}

	$query="update random_db set pw='$pw', sex='$sex', age='$age' where id='$id'";

	$k=mysql_query($query);

	if($k=='0'){
		$response['result']='false';
		$response['message']='회원 정보 수정 실패';		
		exit;
	}

	$response['result']='true';
	$response['message']='회원 정보 수정 성공';
	$response['server_ip_address']= $_SERVER['SERVER_ADDR']; 	// 현재의 스크립트가 실행되고 있는 서버의 IP address
	$response['user_ip_address']=$_SERVER['REMOTE_ADDR'];	//현재 페이지를 보고 있는 사용자의 IP address
	
	echo json_encode($response);

	mysql_close();

?>