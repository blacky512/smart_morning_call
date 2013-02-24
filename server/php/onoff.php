<?php
	session_start();
	include "./connect.php";

	$temp = json_decode(file_get_contents('php://input') , true);
//	print_r($temp);
//	$temp = $_POST;

	$id=$temp['id'];
	$onoff = $temp['onoff'];

	$response=array();

	$query="update random_db 
			set onoff='$onoff' 
			where id='$id'";

	$k=mysql_query($query);

	if($k=='0'){
		$response['result']='false';
		$response['message']='on off 갱신 실패';
		exit;
	}

	$response['result']='true';
	$response['message']='on off 갱신 성공';

	
?>