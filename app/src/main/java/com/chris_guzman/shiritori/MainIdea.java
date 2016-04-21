package com.chris_guzman.shiritori;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MainIdea extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList myDataset = new ArrayList<String>();
    private EditText ideaTxt;
    public static final String TAG = "ideas";

    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Begin Couchbase Events App");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_idea);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myDataset.add("Apple");
        myDataset.add("Banana");
        myDataset.add("Cherry");

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);

        ideaTxt = (EditText) super.findViewById(R.id.idea_edit_txt);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idea = ideaTxt.getText().toString();
                addToListAndRefreshEditTxt(idea);
            }
        });

        ideaTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    String idea = ideaTxt.getText().toString();
                    addToListAndRefreshEditTxt(idea);
                    return true;
                }
                return false;
            }
        });
    }


    private void addToListAndRefreshEditTxt(String idea) {
        if (!TextUtils.isEmpty(idea) && idea.length() > 1) {
            myDataset.add(0, idea);
            mAdapter = new MyAdapter(myDataset);
            mRecyclerView.setAdapter(mAdapter);
            ideaTxt.setText(idea.substring(idea.length() - 1).toUpperCase());
            ideaTxt.setSelection(1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_idea, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
