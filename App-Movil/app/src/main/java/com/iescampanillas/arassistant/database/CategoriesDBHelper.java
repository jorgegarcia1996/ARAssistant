package com.iescampanillas.arassistant.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.iescampanillas.arassistant.R;
import com.iescampanillas.arassistant.constant.AppString;
import com.iescampanillas.arassistant.model.Category;
import com.iescampanillas.arassistant.utils.Generator;

public class CategoriesDBHelper extends SQLiteOpenHelper {
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "arassistant.db";

    public CategoriesDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create table Category
        db.execSQL("CREATE TABLE " + CategoriesContract.CategoriesEntry.TABLE_NAME + " ("
                + CategoriesContract.CategoriesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CategoriesContract.CategoriesEntry.CAT_ID + " TEXT NOT NULL,"
                + CategoriesContract.CategoriesEntry.CAT_NAME + " TEXT NOT NULL,"
                + CategoriesContract.CategoriesEntry.CAT_EN + " TEXT NOT NULL,"
                + CategoriesContract.CategoriesEntry.CAT_COLOR + " NUMBER NOT NULL,"
                + CategoriesContract.CategoriesEntry.CAT_ICON + " NUMBER NOT NULL,"
                + "UNIQUE (" + CategoriesContract.CategoriesEntry.CAT_ID + "))");

        //Insert initial data
        setDefaultCategories(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    /**
     * Insert the default categories in the database
     *
     * @param db The database to insert the data
     * */
    private void setDefaultCategories(SQLiteDatabase db) {
        String categoryTable = CategoriesContract.CategoriesEntry.TABLE_NAME;
        //Add generic categories
        addCategory(db, categoryTable, new Category(Generator.generateId(AppString.CAT_PREFIX),
                "Todos", "All", "", 0));
        addCategory(db, categoryTable, new Category(Generator.generateId(AppString.CAT_PREFIX),
                "Familia", "Family", "#FFBA0B", R.drawable.ic_family));
        addCategory(db, categoryTable, new Category(Generator.generateId(AppString.CAT_PREFIX),
                "Comida", "Food", "#1FBC39", R.drawable.ic_food));
        addCategory(db, categoryTable, new Category(Generator.generateId(AppString.CAT_PREFIX),
                "Salud", "Health", "#ED0F0F", R.drawable.ic_health));
        addCategory(db, categoryTable, new Category(Generator.generateId(AppString.CAT_PREFIX),
                "Rutina", "Routine", "#0F5DED", R.drawable.ic_routine));
        addCategory(db, categoryTable, new Category(Generator.generateId(AppString.CAT_PREFIX),
                "Otros", "Other", "#838383", R.drawable.ic_other));

    }

    /**
     * Insert a category in the database
     *
     * @param db The database to insert the data
     * @param cat the category to be inserted in the database
     * @param tableName The name of the table where insert the data
     * */
    private void addCategory(SQLiteDatabase db, String tableName, Category cat) {
        db.insert(
                tableName,
                null,
                cat.toContentValues());
    }

    /**
     * Get all categories from the database
     * */
    public Cursor getAllCategories() {
        Cursor c = getReadableDatabase()
                .query(
                        CategoriesContract.CategoriesEntry.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                );
        return c;
    }

    /**
     * Get a category by id
     *
     * @param id The id of the category
     * */
    public Cursor getCategoryById(String id) {
        Cursor c = getReadableDatabase()
                .query(
                        CategoriesContract.CategoriesEntry.TABLE_NAME,
                        null,
                        CategoriesContract.CategoriesEntry.CAT_ID + " LIKE ?",
                        new String[]{id},
                        null,
                        null,
                        null
                );
        return c;
    }

    /**
     * Get the color and icon of a category by the name and language
     *
     * @param name the name of the category
     * @param language The language of the system
     * */
    public Cursor getCategoryColorAndIconByName(String name, String language) {
        Cursor c = getReadableDatabase()
                .query(
                        CategoriesContract.CategoriesEntry.TABLE_NAME,
                        new String[] {CategoriesContract.CategoriesEntry.CAT_COLOR,
                                      CategoriesContract.CategoriesEntry.CAT_ICON},
                        language + " LIKE ?",
                        new String[]{name},
                        null,
                        null,
                        null
                );
        return c;
    }
}
