package com.example.ramji.android.gshare;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.ramji.android.gshare.following.FollowingPreferenceActivity;
import com.example.ramji.android.gshare.provider.GshareContract;
import com.example.ramji.android.gshare.provider.GshareProvider;
import com.google.firebase.iid.FirebaseInstanceId;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "MainActivity";
    private static final int LOADER_ID_MESSAGES = 0;

    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    GshareAdapter mAdapter;

    static final String[] MESSAGES_PROJECTION = {
            GshareContract.COLUMN_AUTHOR,
            GshareContract.COLUMN_MESSAGE,
            GshareContract.COLUMN_DATE,
            GshareContract.COLUMN_AUTHOR_KEY
    };

    static final int COL_NUM_AUTHOR = 0;
    static final int COL_NUM_MESSAGE = 1;
    static final int COL_NUM_DATE = 2;
    static final int COL_NUM_AUTHOR_KEY = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.gshare_recycler_view);

        // Use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // Use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //Add dividers
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        // Specify an adapter
        mAdapter = new GshareAdapter();
        mRecyclerView.setAdapter(mAdapter);

        // Start the loader
        getSupportLoaderManager().initLoader(LOADER_ID_MESSAGES, null, this);

        //Some dummy codes to view messages in app
        ContentValues values1 = new ContentValues();
        values1.put(GshareContract.COLUMN_DATE,5879L);
        values1.put(GshareContract.COLUMN_AUTHOR_KEY,GshareContract.RAMJI_KEY);
        values1.put(GshareContract.COLUMN_AUTHOR,"Ramji");
        values1.put(GshareContract.COLUMN_MESSAGE,"Message from Ramji");
        getContentResolver().insert(GshareProvider.GshareMessages.CONTENT_URI, values1);
//
//        //Grap details from FCM
//        Bundle extras = getIntent().getExtras();
//        if (extras != null && extras.containsKey("test")){
//            Log.d(TAG,"Contains: "+ extras.getString("test"));
//        }
//
//        // Get token from the ID Service you created and show it in a log
//
//        String token = FirebaseInstanceId.getInstance().getToken();
//        String msg = getString(R.string.message_token_format, token);
//        Log.d(TAG, msg);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_following_preferences) {
            // Opens the following activity when the menu icon is pressed
            Intent startFollowingActivity = new Intent(this, FollowingPreferenceActivity.class);
            startActivity(startFollowingActivity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Loader call backs
     */

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // This method generates a selection off of only the current followers
        String selection = GshareContract.createSelectionForCurrentFollowers(
                PreferenceManager.getDefaultSharedPreferences(this));
        Log.d(TAG, "Selection is " + selection);
        return new CursorLoader(this, GshareProvider.GshareMessages.CONTENT_URI,
                MESSAGES_PROJECTION, selection, null, GshareContract.COLUMN_DATE + " DESC");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mAdapter.swapCursor(data);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);

    }
}
