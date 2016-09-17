package pet.com.br.pet.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by iaco_ on 28/08/2016.
 */
public class ChatData extends SQLiteOpenHelper {

    static final String DB_NAME = "pet.db";
    static final int DB_VERSION = 1;
    public static final String TABLE_1 = "CHATMSG";
    public static final String C_ID_1 = "_id";
    public static final String C_CODIGO_1 = "CODIGO";
    public static final String C_USERNAME_1 = "USERNAME";
    public static final String C_MSG_1 = "MENSAGEM";
    public static final String C_USERNAME_2 = "OTHERUSERNAME";


    public ChatData(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        try{
            String sql = "create table " + TABLE_1 + " (" + C_ID_1 + " text, "
                    + C_CODIGO_1 + " text, " + C_USERNAME_1 + " text, " + C_MSG_1 + " text, " + C_USERNAME_2 + " text)";
            db.execSQL(sql);
        }catch(Exception e){
            Log.e("Error DbHelper", e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        try{
            db.execSQL("drop table if exists " + TABLE_1);
            onCreate(db);
        }catch(Exception e){
            Log.e("Error DbHelper", e.getMessage());
        }
    }

}
