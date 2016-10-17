package com.chris_guzman.shiritori;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.Toast;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class IdeaGeneratorActivity extends BaseActivity {
    public static final String IDEAS_CHILD = "ideas";
    public static final String ANONYMOUS = "anonymous";
    private static final String TAG = "IdeaGeneratorActivity";

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private EditText ideaTxt;

    // Firebase instance variables
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<Idea, IdeaViewHolder> mFirebaseAdapter;
    private SharedPreferences mSharedPreferences;
    private String mUsername;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String mPhotoUrl;
    private GoogleApiClient mGoogleApiClient;

    public static class IdeaViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public IdeaViewHolder(View v) {
            super(v);
            mTextView = (TextView) itemView.findViewById(R.id.item_idea_name_txt);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idea_generator);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mUsername = ANONYMOUS;

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        } else {
            mUsername = mFirebaseUser.getDisplayName();
            if (mFirebaseUser.getPhotoUrl() != null) {
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            }
        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
            .enableAutoManage(this /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                @Override public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                    Log.d(TAG, "onConnectionFailed:" + connectionResult);
                    Toast.makeText(IdeaGeneratorActivity.this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
                }
            })
            .addApi(Auth.GOOGLE_SIGN_IN_API)
            .build();

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //mRecyclerView.setHasFixedSize(true);

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

        // New child entries
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Idea, IdeaViewHolder>(Idea.class, R.layout.layout_idea_name, IdeaViewHolder.class, mFirebaseDatabaseReference.child(IDEAS_CHILD)) {
            @Override
            protected void populateViewHolder(IdeaViewHolder viewHolder, Idea model, int position) {
                viewHolder.mTextView.setText(model.getName());
            }
        };

        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
            }
        });

        mRecyclerView.setAdapter(mFirebaseAdapter);
    }

    private void addToListAndRefreshEditTxt(String ideaName) {
        if (!TextUtils.isEmpty(ideaName) && ideaName.length() > 1) {
            Idea idea = new Idea(ideaName);
            mFirebaseDatabaseReference.child(IDEAS_CHILD).push().setValue(idea);

            ideaTxt.setText(ideaName.substring(ideaName.length() - 1).toUpperCase());
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
