<?php
// 
	include "./connect.php";

	$temp=json_decode(file_get_contents('php://input'), ture);
//	print_r($temp);
//	$temp=$_POST;

	$like=$temp['like'];	// 상대의 좋아요 싫어요 
	$x = $temp['x']; 	// 알람 수신시간 

	$response=array();



	// 안쓰는 아이 
	

?>