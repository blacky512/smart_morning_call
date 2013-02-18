<?php
	session_start();

	$_SESSION = array();

	if(isset($_COOKIE[session_name()])){
		setcookie(session_name(), '', time()-42000, '/');
	}

	session_destroy();

	$response = array();

	$response['ressult']='true';
	$response['message']='로그아웃 성공';
	$response['server_ip_address']= $_SERVER['SERVER_ADDR']; 	// 현재의 스크립트가 실행되고 있는 서버의 IP address
	$response['user_ip_address']=$_SERVER['REMOTE_ADDR'];	//현재 페이지를 보고 있는 사용자의 IP address	
	
	echo json_encode($response);
?>