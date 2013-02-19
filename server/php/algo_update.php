<?php
	session_start();
	include "./connect.php";

/* 알람 시간 5분전에 단말기에서 서버로 id, ip 정보 전송
    데이터베이스에서 이를 갱신한다.*/

	$temp = json_decode(file_get_contents('php://input') , true);
	print_r($temp);
//	$temp = $_POST;

	$time=$temp['time'];

	$id=$temp['id'];
	$ip_public=$temp['ip_public'];
	$ip_private=$temp['ip_private'];

	$response=array();

	// database에 ip_public, ip_private 갱신

	$query="update random_db 
			set ip_public='$ip_public', ip_private='$ip_private'
			where id='$id'";

	$k=mysql_query($query);

	if($k=='0'){
		$response['result']='false';
		$response['message']='public ip, private ip 갱신 실패';
		exit;
	}

	$response['result']='true';
	$response['message']='public ip, private ip 갱신 성공';
//	$response['server_ip_address']= $_SERVER['SERVER_ADDR']; 	// 현재의 스크립트가 실행되고 있는 서버의 IP address
//	$response['user_ip_address']=$_SERVER['REMOTE_ADDR'];	//현재 페이지를 보고 있는 사용자의 IP address

	echo json_encode($response);


?>