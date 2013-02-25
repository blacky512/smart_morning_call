<?php
	session_start();
// 
	include "./connect.php";

	$temp=json_decode(file_get_contents('php://input'), ture);
	print_r($temp);
//	$temp=$_POST;

	$response=array();

//	like 점수를 제외한 다른 요소들은 단말기로 부터
//  like 는 디비에서 가져온다.

//  테스트 모드
/*
	$id='test';
	$x= '180';	// 180초 걸림
	$stamp = 1;
*/

	$id = $temp['id'];
	$x = $temp['x']; 	// 알람 수신까지 소요시간 
	$stamp=$temp['stamp'];	//전화 30초 유지 시간

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

	// 수신시간 - 알람 시간
	if($x <= 10){
		$time_point = 10;
	}
	else if($x <= 30){
		$time_point = 5;
	}
	else if($x <= 45){
		$time_point = 3;
	}
	else if($x <= 59){
		$time_point = 1;
	}
	else if(!$x){
		$response['result']='false';
		$response['message']='x_secon 받아오기 실패';
		echo json_encode($response);
	}

	$query="select like from like_db where id='$id'";
	$result=mysql_query($query);
	$row=array();
	$row=mysql_fetch_array($result);
	$like=$row['like'];

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

	$today_point = $like_point + $stamp_point + $time_point;


	$sql = "select point from random_db where id='".$id."'"; // 알람시간
	$result= mysql_query($sql);
	$row=array();
	$row=mysql_fetch_array($result);
	$point = $row['point'];

	$point = $today_point + $point;

	$response['today_point']=$today_point;
	$response['point'] = $point;
	$response['result']='true';
	$response['message']= '전체 포인트 갱신 성공';
	echo json_encode($response);

?>