package whitepaper.smcall.db;

import android.provider.BaseColumns;

public class SmcallDB_Info implements BaseColumns{
	
	// �⺻���� ���̺�
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
	// ����� ���� ���̺�
	public static class MornCallInfo{
		public static final String _TABLENAME	= "morncallinfo";
		
		//public static final String  
		
		public static final String _CREATE		= null;
	}
	
	// ģ�� ���̺�
	public static class Friends{
		public static final String _TABLENAME	= "friends";
		
		public static final String _CREATE		= null;		
	}
	
	// ģ���� ����� ���� ���̺�
	public static class WithFriends{
		public static final String _TABLENAME	= "withfriends";
		
		public static final String _CREATE		= null;
	}

}