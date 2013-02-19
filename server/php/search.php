<?
	session_start();
	include "./connect.php";

	$id= $_SESSION['id'];

	$sql = "select * from random_db where id = '$id'";
	$result = mysql_query($sql);
	$row= mysql_fetch_array($result);

	$response=array();

	if(!$row){
		$response['result']='false';
		$response['message']='회원정보 가져오기 실패';
		echo json_encode($response);

		exit;
	}

	$response['pw']=$row['pw'];
	$response['sex']=$row['sex'];
	$response['age']=$row['age'];

	$response['result']='true';
	$response['message']="회원정보 가져오기 성공";
//	$response['server_ip_address']= $_SERVER['SERVER_ADDR']; 	// 현재의 스크립트가 실행되고 있는 서버의 IP address
//	$response['user_ip_address']=$_SERVER['REMOTE_ADDR'];	//현재 페이지를 보고 있는 사용자의 IP address

	echo json_encode($response);

	mysql_close();

?>