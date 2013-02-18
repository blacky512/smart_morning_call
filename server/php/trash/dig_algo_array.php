<?php
	session_start();
	include "./connect.php";


	$f_array=array();   // 여자 회원 배열 생성  
	$m_array=array();   // 남자 회원 배열 생성 

	$f_sql = "select $id, $ip_public, $ip_private 
			from random_db
			where acc='1' and time='$time' and sex='0'
			order by point desc";

	$m_sql = "select $id, $ip_public, $ip_private 
			from random_db
			where acc='1' and time='$time' and sex='1'
			order by point desc";

	$f_result=mysql_query($f_sql);
	$m_result=mysql_query($m_sql);

	$f_num=mysql_num_rows($f_result);
	$m_num=mysql_num_rows($m_result);

	if($f_num == $m_mum){
		for($i=0; $i<$f_num; $i++){
			$f_array[$i] = array();
			$m_array[$i] = array();

			$f_array[$i][‘call’] = ‘true’;
			$m_array[$i][‘call’] = ‘true’;

			$f_row['$i']=mysql_fetch_array($f_result);
			$m_row['$i']=mysql_fetch_array($m_result);

			// f_array 값 넣기

			$f_array['$i']['id']=$f_row['$i']['id'];
			$f_array['$i']['ip_public']=$f_row['$i']['ip_public'];
			$f_array['$i']['ip_private']=$f_row['$i']['ip_private'];

			// m_array 값 넣기

			$m_array['$i']['id']=$m_row['$i']['id'];
			$m_array['$i']['ip_public']=$m_row['$i']['ip_public'];
			$m_array['$i']['ip_private']=$m_row['$i']['ip_private'];

			// f_array <- m_array 로 transfer

			$f_array['$i']['sb_id']=$m_row['$i']['sb_id'];
			$f_array['$i']['sb_ip_public']=$m_row['$i']['sb_ip_public'];
			$f_array['$i']['sb_ip_private']=$m_row['$i']['sb_ip_private'];

			// m_array <- f_array 로 transfer

			$m_array['$i']['sb_id']=$f_row['$i']['sb_id'];
			$m_array['$i']['sb_ip_public']=$f_row['$i']['sb_ip_public'];
			$m_array['$i']['sb_ip_private']=$f_row['$i']['sb_ip_private'];
		
			echo "<br> f_array[i][id]: ".$f_array['$i']['id'];
			echo "<br> f_array[i][ip_public]: ".$f_array['$i']['ip_public'];
			echo "<br> f_array[i][ip_private]: ".$f_array['$i']['$ip_private'];

			echo "<br> m_array[i][id]: ".$m_array['$i']['id'];
			echo "<br> m_array[i][ip_public]: ".$m_array['$i']['ip_public'];
			echo "<br> m_array[i][ip_private]: ".$m_array['$i']['$ip_private'];

		}	// end of for		
		exit;
	}	// end of if

	if($f_num < $m_mum){
		for($i=0; $i<$f_num; $i++){

			$f_row['$i']=mysql_fetch_array($f_result);
			$m_row['$i']=mysql_fetch_array($m_result);

			// f_array 값 넣기

			$f_array['$i']['id']=$f_row['$i']['id'];
			$f_array['$i']['ip_public']=$f_row['$i']['ip_public'];
			$f_array['$i']['ip_private']=$f_row['$i']['ip_private'];

			// m_array 값 넣기

			$m_array['$i']['id']=$m_row['$i']['id'];
			$m_array['$i']['ip_public']=$m_row['$i']['ip_public'];
			$m_array['$i']['ip_private']=$m_row['$i']['ip_private'];
		} 	// end of for

		// m_array 의 마지막 값 통화 불가			
		$k=$f_num+1;	
		$m_array['$k']['call']=$m_row['$k']['call'];
		$m_array['$k']['call']='false';

	} 	// end of if


	if($f_num > $m_mum){
		for($i=0; $i<$m_num; $i++){

			$f_row['$i']=mysql_fetch_array($f_result);
			$m_row['$i']=mysql_fetch_array($m_result);

			// f_array 값 넣기

			$f_array['$i']['id']=$f_row['$i']['id'];
			$f_array['$i']['ip_public']=$f_row['$i']['ip_public'];
			$f_array['$i']['ip_private']=$f_row['$i']['ip_private'];

			// m_array 값 넣기

			$m_array['$i']['id']=$m_row['$i']['id'];
			$m_array['$i']['ip_public']=$m_row['$i']['ip_public'];
			$m_array['$i']['ip_private']=$m_row['$i']['ip_private'];
		}

		// f_array 의 마지막 값 통화 불가			
		$k=$m_num+1;	
		$f_array['$k']['call']=$f_row['$k']['call'];
		$f_array['$k']['call']='false';


	} 	// end of if


?>
