<?php
	session_start();
	include "./connect.php";

	$temp = json_decode(file_get_contents('php://input') , true);
	print_r($temp);
//	$temp = $_POST;
	$is_update= 'false';
//	print_r($is_update);

//	$is_update= $temp['is_update'];
	$id =$_SESSION['id'];

	if($is_update=="false"){
		$sql = "select * from random_db where id='$id'";
		$result = mysql_query($sql);
		$row = mysql_fetch_array($result);

		$response=array();

		if(!$row){
			$response['result']='false';
			$response['message']='기존 알람 정보 가져오기 실패';
			echo json_encode($response);

			exit;
		}

		$time=$row['time'];
		
		// time 변수 -> hour, minute 으로 쪼개기
		$pattern=":";
		$arr_time=split($pattern, $time);
		$hour=$arr_time[0];
		$minute=$arr_time[1];

		$response['hour']=$hour;
		$response['minute']=$minute;
		$response['result']='true';
		$response['message']='기존 알람 정보 가져오기 성공';
//		$response['server_ip_address']= $_SERVER['SERVER_ADDR']; 	// 현재의 스크립트가 실행되고 있는 서버의IP address
//		$response['user_ip_address']=$_SERVER['REMOTE_ADDR'];	//현재 페이지를 보고 있는 사용자의 IP address
		echo json_encode($response);

	} 	// end of if ( is_update == false)

	else if($is_update=="true"){

		$hour=$temp['hour'];
		$minute=$temp['minute'];

		if($hour<10){
			$hour='0'.$hour;
		}
		if($minute<10){
			$minute='0'.$minute;
		}

		$time=$hour.':'.$minute.':00';

		$response= array();

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
//		$response['server_ip_address']= $_SERVER['SERVER_ADDR']; 	// 현재의 스크립트가 실행되고 있는 서버의 IP address
//		$response['user_ip_address']=$_SERVER['REMOTE_ADDR'];	//현재 페이지를 보고 있는 사용자의 IP address

		echo json_encode($response);

		mysql_close();

	}	// end of else ( is_update = true)


	
?>