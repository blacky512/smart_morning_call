package whitepaper.smcall.alarm;

public class AlarmStr {
	
	// ID
	public static String 		id;
	
	public static String		private_ip;
	public static String		public_ip;
	
	// �ð�
	public static int			time_hour = -1;
	public static int			time_minute = -1;
	public static long			time_long	= 0;
	
	// �ݺ�����
	public static boolean[]	repeat = new boolean[7];
	
	// ��ġ����  //�̼���, �ð�����
	public static boolean		match_only_opposite;
	//public boolean		match_range;
	
	// ����� �˸� Ÿ��
	public static boolean		type_sound;
	public static int			type_sound_volume;
	public static boolean		type_vibe;
	
	// Ȱ�� ����
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
