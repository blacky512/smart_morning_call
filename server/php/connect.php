<?php
	$conn=mysql_connect('localhost', 'root', 'apmsetup');	// db 연결 부분

	if($conn){
		//echo "db CONNECT success \n";

		$db=mysql_select_db("smart_morningcall", $conn);	 // db 선택 부분

		if($db){
			//echo "DB connect success \n";
		}
		else{
			//echo "DB connect fail \n";
		}
	}
	else{
		//echo "db CONNECT fail \n";
	}

?>