package com.swjtu.youthapp.data;
import java.util.ArrayList;
import java.util.List;
import com.swjtu.youthapp.po.CategoryEntity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHandler extends SQLiteOpenHelper{
    //Database Version
    private static final int DATABASE_VERSION=1;
    //Database Name
    private static final String DATABASE_NAME="contactsmanager";
	 //constructor
    public DataBaseHandler(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String create_category="create table category(id INTEGER PRIMARY KEY,name TEXT";
		String create_news="create table news(id INTEGER PRIMARY KEY,category INTEGER,title TEXT,time TEXT,content TEXT)";
		String create_image="create table newsimage(id INTEGER PRIMARY KEY,newsid INTEGER,imagepath TEXT)";
		db.execSQL(create_category);
		db.execSQL(create_news);
		db.execSQL(create_image);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	      //Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS category");
        db.execSQL("DROP TABLE IF EXISTS news");
        db.execSQL("DROP TABLE IF EXISTS newsimage");
        //Create tables again
        onCreate(db);
	}
	
	  //Adding new category
    public void addCategory(CategoryEntity categoryEntity){
            SQLiteDatabase db=this.getWritableDatabase();
            
            ContentValues values=new ContentValues();
            values.put("id", categoryEntity.getId()); //Category ID
            values.put("name",categoryEntity.getName()); //Category Name
            //Inserting Row
            db.insert("category", null, values);
            db.close(); //Closing database connection
    }
    
    //Getting a single category
    public CategoryEntity getCategory(int id){
            SQLiteDatabase db=this.getReadableDatabase();
            Cursor cursor=db.query("category",
                            new String[]{"id","name"},
                            "id=?", new String[]{String.valueOf(id)}, 
                            null, null, null);
            CategoryEntity categoryEntity = new CategoryEntity();
           if(cursor.getCount()!=0){
        	   cursor.moveToFirst();
        	  categoryEntity.setId(cursor.getInt(0));
        	  categoryEntity.setName(cursor.getString(1));
           }
            //return category
            return categoryEntity;
    }
    
    //Getting All category
    public List<CategoryEntity> getAllCategory(){
            List<CategoryEntity> List=new ArrayList<CategoryEntity>();
            //Select All Query
            String selectQuery="SELECT * FROM category";
            SQLiteDatabase db=this.getReadableDatabase();
            Cursor cursor=db.rawQuery(selectQuery, null);
            
            //looping through all raws and adding to list
            if(cursor.moveToFirst()){
                    do{
                            CategoryEntity categoryEntity=new CategoryEntity();
                            categoryEntity.setId(Integer.parseInt(cursor.getString(0)));
                            categoryEntity.setName(cursor.getString(1));
                            //Adding cateory to list
                            List.add(categoryEntity);
                    }while(cursor.moveToNext());
            }
            //Close cursor
            cursor.close();
            //Close SQliteDatabase
            db.close();
            //return contact list
            return List;
    }
    
    

}
