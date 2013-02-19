<?php
	session_start();
	include "./connect.php";

	$temp = json_decode(file_get_contents('php://input') , true);
	print_r($temp);
//	$temp = $_POST;
	
	$id=$temp['id'];
//	$time="06:00:00";

	$time=$temp['time'];

	$f_sql = "select id, ip_public, ip_virtual
			from random_db
			where acc='1' and time='".$time."' and sex='0'
			order by point desc";

	$m_sql = "select id, ip_public, ip_virtual
			from random_db
			where acc='1' and time='".$time."' and sex='1'
			order by point desc";

	$f_result=mysql_query($f_sql);
	$m_result=mysql_query($m_sql);


	$f_num=mysql_num_rows($f_result);
	$m_num=mysql_num_rows($m_result);


	$num=max(intval($f_num), intval($m_num));


	$user_id=$_SESSION['id'];

//	$user_id='test';

	for($i=0; $i<$num; $i++){
		$f_row[$i]=mysql_fetch_array($f_result);
		$m_row[$i]=mysql_fetch_array($m_result);

		if($m_row[$i]['id'] == $user_id) {
			if( $f_row[$i]) {
				$response['id']=$f_row[$i]['id'];
				$response['ip_public']=$f_row[$i]['ip_public'];
				$response['ip_virtual']=$f_row[$i]['ip_virtual'];
				$response['call'] = 'true';
			} 
			else {
				$response['call'] = 'false';
			}
			echo json_encode($response);
			exit;			
		}

		else if ($f_row[$i]['id'] == $user_id) {
			if( $m_row[$i] ) {
					$response['id']=$m_row[$i]['id'];
					$response['ip_public']=$m_row[$i]['ip_public'];
					$response['ip_virtual']=$m_row[$i]['ip_virtual'];
					$response['call'] = 'true';
			}
			else {
				$response['call'] = 'false';
			}
			echo json_encode($response);
			exit;
		}		
	}

?>