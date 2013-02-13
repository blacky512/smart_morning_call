<?php
	session_start();
	include "./connect.php";

	$temp=json_decode(urldecode($_REQUEST['data']));
	$id=$temp['id'];
	$pw=$temp['pw'];

	$response = array();

/*	if(!$id){
		$response['result'] = 'false';
		$response['message'] = '아이디를 입력하세요';
		echo json_encode($response);
		exit;
	}

	if(!$pw){
		$response['result'] = 'false';
		$response['message'] = '비밀번호를 입력하세요';
		echo json_encode($response);
			exit;
	}
*/
	$sql = "select * from random_db where id='".$id."'";
	$result=mysql_query($sql);
	$num_match=mysql_num_rows($result);

	if(!$num_match){
		$response['result'] = 'false';
		$response['message'] = '등록되지 않은 아이디 입니다';
		echo json_encode($response);
	}
	else
	{
		$row = mysql_fetch_array($result);
		$db_pw=$row["pw"];
		if($pw != $db_pw){
			$response['result'] = 'false';
			$response['message'] = '비밀번호가 틀립니다';
			echo json_encode($response);
			exit;
		}
		else{

			$_SESSION['id']=$id;
			$response['result'] = 'true';
			$response['message'] = '로그인 성공';

			echo json_encode($response);

			mysql_close();
		}
	}
?>