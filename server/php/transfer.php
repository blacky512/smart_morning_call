<?php
	session_start();
	include "./connect.php";

/*  알람 2분전 
		서버는 스마트폰들에게 서로의 아이피와 역할을
		분담(서버/클라이언트)시킨다.
*/

	$temp=$_REQUEST;

	$id=$temp['id'];
	$sb_id=$temp['sb_id'];
	$time=$temp['time'];

	$transfer=array();

	$query="select id, ip_public, ip_virtual from random_db where id='".$sb_id."'";

	$result=mysql_query($query);

	$row = mysql_fetch_array($result);
	$transfer['id']=$row['id'];
	$transfer['ip_public']=$row['ip_public'];
	$transfer['ip_virtual']=$row['ip_virtual'];

	echo json_encode($transfer);

?>