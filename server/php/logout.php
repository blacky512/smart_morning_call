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
	echo json_encode($response);
?>