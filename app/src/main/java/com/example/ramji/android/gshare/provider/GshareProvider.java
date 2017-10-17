package com.example.ramji.android.gshare.provider;


import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;



/**
 * Uses the Schematic (https://github.com/SimonVT/schematic) to create a content provider and
 * define
 * URIs for the provider
 */

@ContentProvider(
        authority = GshareProvider.AUTHORITY,
        database = GshareDatabase.class)

public final class GshareProvider {

    public static final String AUTHORITY = "com.example.ramji.android.gshare.provider.provider";

    @TableEndpoint(table = GshareDatabase.GSHARE_MESSAGES)
    public static class GshareMessages{
        @ContentUri(
                path = "messages",
                type = "vnd.android.cursor.dir/messages",
                defaultSort = GshareContract.COLUMN_DATE + " DESC"
        )
        public static final Uri CONTENT_URI = Uri.parse("content://"+ AUTHORITY + "/messages");
    }
}
