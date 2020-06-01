package com.iescampanillas.arassistant.database;

import android.provider.BaseColumns;

public class CategoriesContract {
    public static abstract class CategoriesEntry implements BaseColumns {
        //Table name
        public static final String TABLE_NAME = "categories";

        //Table columns
        public static final String CAT_ID = "catId";
        public static final String CAT_COLOR = "catColor";
        public static final String CAT_ICON = "catIcon";
        public static final String CAT_NAME = "catName";
        public static final String CAT_EN = "en";
    }
}
