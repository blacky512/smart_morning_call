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
	echo json_encode($response);

	mysql_close();

?>