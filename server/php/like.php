<?php
// 
	include "./connect.php";

	$temp=json_decode(file_get_contents('php://input'), ture);
//	print_r($temp);
//	$temp=$_POST;

/*
	$id=$temp['id'];		// 나의 아이디
	$ur_id=$temp['ur_id'];		// 남의 아이디
	$like=$temp['like'];	// 내가 상대방에 대한 평가를 보냄.
*/							// 나는 너를 like 라 평가
// test mode
	$id = 'test';
	$ur_id= 'test2';
	$like=1;	

	$query="select date_format(NOW(), '%Y-%m-%d') date";
	$result=mysql_query($query);
	$row=mysql_fetch_array($result);
	$date=$row['date'];
	echo "<br>date: ".$date;

	$query="select time from random_db where id='$id'";
	$result=mysql_query($query);
	$row=array();
	$row=mysql_fetch_array($result);
	$time=$row['time'];
	echo "<br>time: ".$time;

	$response=array();

	$query = "INSERT INTO like_db ('id', 'ur_id', 'like', 'date`, 'time') 
					VALUES ('$id', '$ur_id', '$like', '$date', '$time')";

//	$query = "insert into like_db(id, ur_id, like, date, time)
//						values('$id', '$ur_id', '$like', '$date', '$time')";
	echo "<br><br>query: ".$query;
	$k=mysql_query($query);

	echo "<br><br> k: ".$k;
	
	if($k==0){
		$response['result']='false';
		$response['message']='like_db 등록 실패';
	}

	$response['result']='true';
	$response['message']='like_db 등록 성공';
	echo json_encode($response);

?>