<?php
	session_start();
	include "./connect.php";
	
	$temp = json_decode(file_get_contents('php://input'), true);
	print_r($temp);
//	$temp = $_POST;

	$id=$temp['id'];
	$pw=$temp['pw'];

	$response = array();

	if(!$id){
		$response['result'] = 'false';
		$response['message'] = 'give me ID';
		echo json_encode($response);
		exit;
	}

	if(!$pw){
		$response['result'] = 'false';
		$response['message'] = 'give me PW';
		echo json_encode($response);
			exit;
	}

	$sql = "select * from random_db where id='".$id."'";
	$result=mysql_query($sql);
	$num_match=mysql_num_rows($result);

	if(!$num_match){
		$response['result'] = 'false';
		$response['message'] = 'That is not registered ID';
		echo json_encode($response);
	}
	else
	{
		$row = mysql_fetch_array($result);
		$db_pw=$row["pw"];
		if($pw != $db_pw){
			$response['result'] = 'false';
			$response['message'] = 'Wrong Password';
			echo json_encode($response);
			exit;
		}
		else{

			$_SESSION['id']=$id;
			$response['result'] = 'true';
			$response['message'] = 'Login Success';
//			$response['server_ip_address']= $_SERVER['SERVER_ADDR']; 	// 현재의 스크립트가 실행되고 있는 서버의 IP address
//			$response['user_ip_address']=$_SERVER['REMOTE_ADDR'];	//현재 페이지를 보고 있는 사용자의 IP address


			echo json_encode($response);

			mysql_close();
		}
	}
?>