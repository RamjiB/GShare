package com.example.ramji.android.gshare.provider;


import android.content.SharedPreferences;
import android.util.Log;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.ConflictResolutionType;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

public class GshareContract {
    private static final String TAG = "GshareContract";

    @DataType(DataType.Type.INTEGER)
    @PrimaryKey(onConflict = ConflictResolutionType.REPLACE)
    @AutoIncrement
    public static final String COLUMN_ID = "_id";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String COLUMN_AUTHOR = "author";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String COLUMN_AUTHOR_KEY = "authorKey";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String COLUMN_MESSAGE = "message";

    @DataType(DataType.Type.INTEGER)
    @NotNull
    public static final String COLUMN_DATE = "date";

    //Topic keys as matching what is found in the database
    public static final String RAMJI_KEY = "key_ramji";
    public static final String YESHWENTH_KEY = "key_yeshwenth";
    public static final String PAVITHRAN_KEY = "key_pavithran";
    public static final String SINDHU_KEY = "key_sindhu";
    public static final String VIJI_KEY = "key_viji";
    public static final String TEST_ACCOUNT_KEY = "key_test";

    public static final String[] INSTRUCTOR_KEYS = {
            RAMJI_KEY,YESHWENTH_KEY,PAVITHRAN_KEY,SINDHU_KEY,VIJI_KEY
    };

    /**
     * Creates a SQLite SELECTION parameter that filters just the rows for the authors you are
     * currently following.
     */
    public static String createSelectionForCurrentFollowers(SharedPreferences preferences){
        StringBuilder stringBuilder = new StringBuilder();
        //Automatically add the test account

        stringBuilder.append(COLUMN_AUTHOR_KEY).append(" IN ('").append(TEST_ACCOUNT_KEY).append("'");

        for (String key : INSTRUCTOR_KEYS){
            if (preferences.getBoolean(key,false)){
                stringBuilder.append(",");
                stringBuilder.append("'").append(key).append("'");
            }
        }
        stringBuilder.append(")");

        Log.d(TAG,stringBuilder.toString());
        return stringBuilder.toString();
    }
}
