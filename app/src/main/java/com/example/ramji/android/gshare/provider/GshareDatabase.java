package com.example.ramji.android.gshare.provider;


import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;


@Database(version = GshareDatabase.VERSION)
public class GshareDatabase {

    public static final int VERSION = 1;

    @Table(GshareContract.class)
    public static final String GSHARE_MESSAGES = "gshare_messages";
}
