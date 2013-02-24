<?php
	session_start();
// 
	include "./connect.php";

	$temp=json_decode(file_get_contents('php://input'), ture);
	print_r($temp);
//	$temp=$_POST;

	$response=array();

// 테스트 모드
/*
	$id='test';
	$like = 1;
	$x= '06:00:12';
	$stamp = 1;
*/
	$id = $temp['id'];
	$like=$temp['like'];	// 상대의 좋아요 싫어요 
	$x = $temp['x']; 	// 알람 수신시간 
	$stamp=$temp['stamp'];	//전화 30초 유지 시간

	$sql = "select time from random_db where id='".$id."'"; // 알람시간
	$result= mysql_query($sql);
	$row=array();
	$row=mysql_fetch_array($result);
	$time = $row['time'];



	// 상대방에게 좋아요 싫어요 받았을 때
	if($like == -1){
		$like_point = 10;
	}
	else if ($like == 0) {
		$like_point = 0;	
	}
	else if($like ==  1) {
		$like_point == -10;	
	}
	else if(!$like){
		$response['result']='false';
		$response['message']='like 받아오기 실패';
		echo json_encode($response);

	}

	// 전화 30초 유지
	if($stamp ==  1){
		$stamp_point = 30;
	}
	else if($stamp == 0){
		$stamp_point = -30;
	}
	else if(!$stamp){
		$response['result']='false';
		$response['message']='stamp 받아오기 실패';
		echo json_encode($response);

	}


	// x와 time 변수 -> hour, minute 으로 쪼개기
	$pattern=":";
	$arr_x=split($pattern, $x);
	$x_second=intval($arr_x[2]);
	// 수신시간 - 알람 시간
	if($x_second <= 10){
		$time_point = 10;
	}
	else if($x_second <= 30){
		$time_point = 5;
	}
	else if($x_second <= 45){
		$time_point = 3;
	}
	else if($x_second <= 59){
		$time_point = 1;
	}
	else if(!$x_second){
		$response['result']='false';
		$response['message']='x_secon 받아오기 실패';
		echo json_encode($response);
	}

	$today_point = $like_point + $stamp_point + $time_point;


	$sql = "select point from random_db where id='".$id."'"; // 알람시간
	$result= mysql_query($sql);
	$row=array();
	$row=mysql_fetch_array($result);
	$point = $row['point'];

	$point = $today_point + $point;

	$response['point'] = $point;
	$response['result']='true';
	$response['message']= '전체 포인트 갱신 성공';
	echo json_encode($response);

?>