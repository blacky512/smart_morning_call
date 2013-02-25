<?php
	session_start();
	include "./connect.php";

	$temp = json_decode(file_get_contents('php://input') , true);
//	print_r($temp);
//	$temp = $_POST;

	$id ='test';
	$time="06:00:00";
	
//	$id=$temp['id'];
//	$time=$temp['time'];

	$f_sql = "select id, sex, ip_public, ip_virtual
			from random_db
			where acc='1' and onoff='1' and time='".$time."' and sex='0'
			order by point desc";

	$m_sql = "select id, sex, ip_public, ip_virtual
			from random_db
			where acc='1' and onoff='1' and time='".$time."' and sex='1'
			order by point desc";

	$f_result=mysql_query($f_sql);
	$m_result=mysql_query($m_sql);


	$f_num=mysql_num_rows($f_result);
	$m_num=mysql_num_rows($m_result);

	$num=max(intval($f_num), intval($m_num));

//	$user_id=$_SESSION['id'];

//	$user_id=$id;

	for($i=0; $i<$num; $i++){
		$f_row[$i]=mysql_fetch_array($f_result);
		$m_row[$i]=mysql_fetch_array($m_result);

		if($m_row[$i]['id'] == $id) {
			if( $f_row[$i]) {
				$sb_id = $f_row[$i]['id']; 	// 상대방의 아이디
				$sb_sex= $f_row[$i]['sex']; 	// 상대방 성별
				$secret = md5($id.$sb_id.$time);

				$response['sb_id']=$sb_id;
				$response['sb_sex']=$sb_sex;
				$response['secret']=$secret;
				$response['ip_public']=$f_row[$i]['ip_public'];
				$response['ip_virtual']=$f_row[$i]['ip_virtual'];
				$response['call'] = 'true';

				$response['result'] = 'true';
				$response['message'] = 'you can call to somebody';				

			} 
			else {
				$response['call'] = 'false';
				$response['result'] = 'true';
				$response['message'] = 'you cannot call to somebody';	
			}
			echo json_encode($response);			
		}	// end of big if

		else if ($f_row[$i]['id'] == $id) {
			if( $m_row[$i] ) {
				$sb_id = $m_row[$i]['id']; 	// 상대방의 아이디
				$sb_sex= $m_row[$i]['sex']; 	// 상대방 성별
				$secret = md5($id.$sb_id.$time);

				$response['sb_id']=$sb_id;
				$response['sb_sex']=$sb_sex;
				$response['secret']=$secret;
				$response['ip_public']=$m_row[$i]['ip_public'];
				$response['ip_virtual']=$m_row[$i]['ip_virtual'];
				$response['call'] = 'true';

				$response['result'] = 'true';
				$response['message'] = 'you can call to somebody';				

			}
			else {
				$response['call'] = 'false';
				$response['result'] = 'true';
				$response['message'] = 'you cannot call to somebody';				

			}

			echo json_encode($response);
		}	// end of big else		
	}	// end of for

?>