<?php
	session_start();
	include "./connect.php";

/* 알람 시간 5분전에 단말기에서 서버로 id, ip 정보 전송
    데이터베이스에서 이를 갱신한다.
*/

	//	$temp=json_decode($_POST['data']);

	$temp = $_POST;

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
		$response['message']='public ip, private ip 갱신 성공';
		exit;
	}

	$response['result']='true';
	$response['message']='public ip, private ip 갱신 성공';

	echo json_encode($response);


?>