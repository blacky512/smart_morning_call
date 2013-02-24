<?php
	session_start();
	include "./connect.php";

	$temp = json_decode(file_get_contents('php://input') , true);
//	print_r($temp);
//	$temp = $_POST;


//	$id =$_SESSION['id'];
	$id=$temp['id'];

	$hour=$temp['hour'];
	$minute=$temp['minute'];

	$response= array();

	if($hour<10){
		$hour='0'.$hour;
	}
	if($minute<10){
		$minute='0'.$minute;
	}

	$time=$hour.':'.$minute.':00';


	$query ="update random_db set time='$time' where id='$id'";
	$k=mysql_query($query);

	if($k=='0'){
		$response['result']='false';
		$response['message']='알람 시간 업데이트 실패';
		exit;
	}

	$response['time']=$time;
	$response['result']='true';
	$response['message']='알람 정보 업데이트';

	echo json_encode($response);

	mysql_close();

?>