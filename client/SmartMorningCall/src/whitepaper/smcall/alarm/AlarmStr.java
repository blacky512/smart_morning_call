package whitepaper.smcall.alarm;

public class AlarmStr {
	
	// ID
	public static String 		id;
	
	public static String		private_ip;
	public static String		public_ip;
	
	// 시간
	public static int			time_hour = -1;
	public static int			time_minute = -1;
	public static long			time_long	= 0;
	
	// 반복설정
	public static boolean[]	repeat = new boolean[7];
	
	// 매치조건  //이성만, 시간범위
	public static boolean		match_only_opposite;
	//public boolean		match_range;
	
	// 모닝콜 알림 타입
	public static boolean		type_sound;
	public static int			type_sound_volume;
	public static boolean		type_vibe;
	
	// 활성 유무
	public static boolean		alive;
	
	
	public AlarmStr(){
		
	}
	
	public static String getTime(){
		String strHour	= String.format("%02d", AlarmStr.time_hour);
        String strMin	= String.format("%02d", AlarmStr.time_minute);
        
        String ret = strHour+":"+strMin+":"+"00";
        return ret;		
	}
}
