package whitepaper.smcall.db;

import android.provider.BaseColumns;

public class SmcallDB_Info implements BaseColumns{
	
	// 기본정보 테이블
	public static class BaseInfo{
		// Name
		public static final String _TABLENAME 	= "baseinfo";
		
		// Columns
		public static final String ID			= "id";
		public static final String PW	 		= "pw";
			
		// Create
		public static final String _CREATE 		=
				"create table " + _TABLENAME 
				+ "("+ _ID +" integer primary key autoincrement, "
				+ ID + " text not null, "
				+ PW + " text not null);";
	}
	// 모닝콜 정보 테이블
	public static class MornCallInfo{
		public static final String _TABLENAME	= "morncallinfo";
		
		
		// Colums
		public static String HOUR		= "hour";
		public static String MINUTE		= "minute";
		
		public static String RP_0		= "rp0";
		public static String RP_1		= "rp1";
		public static String RP_2		= "rp2";
		public static String RP_3		= "rp3";
		public static String RP_4		= "rp4";
		public static String RP_5		= "rp5";
		public static String RP_6		= "rp6";
		
		public static String TYPE_SOUND	= "type_sound";
		public static String TYPE_VIBE	= "type_vibe";
		
		public static String ALIVE		= "alive";
		
		
		//public static final String  
		
		public static final String _CREATE		= 
				"create table " + _TABLENAME
				+ "("+ _ID +" integer primary key autoincrement, "
				+ HOUR + " text not null, "
				+ MINUTE + " text not null, "
				+ RP_0 + " text not null, "
				+ RP_1 + " text not null, "
				+ RP_2 + " text not null, "
				+ RP_3 + " text not null, "
				+ RP_4 + " text not null, "
				+ RP_5 + " text not null, "
				+ RP_6 + " text not null, "
				+ TYPE_SOUND + " text not null, "
				+ TYPE_VIBE + " text not null, "
				+ ALIVE + " text not null"
				+");";
	}
	
	public static class Records{
		// Colums
		public static String MONTH		= "month";
		public static String DATE		= "date";
		
		public static String HOUR		= "hour";
		public static String MINUTE		= "minute";
		
		public static String POINT		= "point";
		public static String LODL		= "lodl";
						
		public static final String _TABLENAME	= "records";
		
		public static final String _CREATE		= 
				"create table " + _TABLENAME
				+ "("+ _ID +" integer primary key autoincrement, "
				+ MONTH + " text not null, "				
				+ DATE + " text not null, "
				+ HOUR + " text not null, "
				+ MINUTE + " text not null, "
				+ POINT + " text not null, "
				+ LODL + " text"				
				+");";
	}
	
	// 친구 테이블
	public static class Friends{
		public static final String _TABLENAME	= "friends";
		
		public static final String _CREATE		= null;		
	}
	
	// 친구와 모닝콜 정보 테이블
	public static class WithFriends{
		public static final String _TABLENAME	= "withfriends";
		
		public static final String _CREATE		= null;
	}

}
