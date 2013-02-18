<?php
	session_start();
	include "./connect.php";

/*  알람 5분전 
		폰에서 ID 와 IP를 JSON데이터 포맷하여 서버로 보낸다.
		서버는 2분 가량동안 아이피 정보들을 모으고 등급에 따라 매칭한다.
*/

	$temp=json_decode($_POST['match_ip']);

	$id=$temp['id'];
	$ip_public=$temp['ip_public'];
	$ip_virtual=$temp['ip_virtual'];
	$time=$temp['time'];

	$response=array();		// 결과 TF 받는다.




?>