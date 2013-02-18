package whitepaper.smcall.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SmcallDB {
	private static final String 	DATABASE_NAME		= "smcall.db";
	private static final int 		DATABASE_VERSION	= 1;
	private SQLiteDatabase 			mDB;
	private DatabaseHelper 			mDBHelper;
	private Context 				mCtx;
	
	private class DatabaseHelper extends SQLiteOpenHelper{
		public DatabaseHelper(Context context, String name, CursorFactory factory, int version){
			super(context, name, factory, version);
		}		
		@Override
		public void onCreate(SQLiteDatabase db){
			db.execSQL(SmcallDB_Info.BaseInfo._CREATE);
			/* @ �����ϵ���
			db.execSQL(SmcallDB_Info.MornCallInfo._CREATE);
			db.execSQL(SmcallDB_Info.Friends._CREATE);			
			db.execSQL(SmcallDB_Info.WithFriends._CREATE);
			*/
		}		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
			db.execSQL("DROP TABLE IF EXISTS " + SmcallDB_Info.BaseInfo._TABLENAME);
			db.execSQL("DROP TABLE IF EXISTS " + SmcallDB_Info.MornCallInfo._TABLENAME);
			db.execSQL("DROP TABLE IF EXISTS " + SmcallDB_Info.Friends._TABLENAME);			
			db.execSQL("DROP TABLE IF EXISTS " + SmcallDB_Info.WithFriends._TABLENAME);
			onCreate(db);
		}
	}//�����ͺ��̽����� ��
	
	public SmcallDB(Context context){
		this.mCtx	= context;
	}
	
	
	// OPEN & CLOSE DB
	public SmcallDB open() throws SQLException{
		mDBHelper	= new DatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
		mDB			= mDBHelper.getWritableDatabase();
		return this;
	}
	
	public void close(){
		mDB.close();
	}
	
	
	// �⺻���� ���̺� ���� SQL
	public Cursor getAccount(){		
		return mDB.query(SmcallDB_Info.BaseInfo._TABLENAME, null, null, null, null, null, null);
	}
	public boolean insertAccount(String id, String pw){	// ���� ����
		
		int	cnt	= getCnt(SmcallDB_Info.BaseInfo._TABLENAME); 
		
		if(cnt != 0){	// �⺻������ ���� �ϳ��� �־�� �ϱ� ������ insert�� �ƹ��͵� �����
			return false;
		}
		
		ContentValues values = new ContentValues();
		values.put(SmcallDB_Info.BaseInfo.ID, id);
		values.put(SmcallDB_Info.BaseInfo.PW, pw);
		
		mDB.insert(SmcallDB_Info.BaseInfo._TABLENAME, null, values);
		return true;
	}
	
	public boolean initAccount(){
		
		int cnt = getCnt(SmcallDB_Info.BaseInfo._TABLENAME);
		
		if(cnt == 0) return true;
		else{
			Cursor	c	= mDB.query(SmcallDB_Info.BaseInfo._TABLENAME, null, null, null, null, null, null);
			if(mDB.delete(SmcallDB_Info.BaseInfo._TABLENAME, "_id="+c.getString(0), null) != 0){
				return true;
			}else{
				return false;
			}
		}
	}
	
	public int getCnt(String tableName){
		Cursor	c	= mDB.query(tableName, null, null, null, null, null, null);
		return c.getCount();
	}
	

}