<?php
	header("Content-Type: text/html; charset=UTF-8");
	session_start();
	include "./connect.php";
	
	$temp = $_REQUEST;
	//print_r($temp);

	$id=$temp['id'];
	$pw=$temp['pw'];
	$pw2=$temp['pw2'];
	$sex=$temp['sex'];
	$age=$temp['age'];


	$acc=0;
	$rank=1;
	$point=0;

	$ip_public;
	$time;
	$ip_virtual;	
	$sb_id;

	$response=array();

	// 중복아이디 검사

	$sql="select * from random_db where id='$id'";
	$result=mysql_query($sql);
	$exist_id=mysql_num_rows($result);

	if($exist_id !=0){

		$response['result']='false';
		$response['message']='해당 아이디가 존재합니다.';
		echo json_encode($response);	

		exit;
	}

	if($pw!=$pw2){

		$response['result']='false';
		$response['message']='비밀번호와 비번 확인이 다릅니다.';
		echo json_encode($response);	

		exit;		
	}

	$query="insert into random_db(id, pw, acc, rank, point, sex, age, ip_public, time, ip_virtual, sb_id) 
			value('$id','$pw', '$acc', '$rank', '$point', '$sex', '$age', '$ip_public', '$time', '$ip_virtual', '$sb_id')";
	$k=mysql_query($query);

	$_SESSION['id']=$id;

	$response['result']='true';
	$response['message']='회원가입 성공';

	echo json_encode($response);

	mysql_close();

?>