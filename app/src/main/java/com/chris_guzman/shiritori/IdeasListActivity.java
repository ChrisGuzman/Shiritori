package com.chris_guzman.shiritori;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

public class IdeasListActivity extends BaseActivity {

    private static final String EMAIL = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ideas_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), IdeaGeneratorActivity.class);
                startActivity(intent);
            }
        });

        if (emailNotSaved()) {
            //showSetEmailDialog();
        }
    }

    private boolean emailNotSaved() {
        return TextUtils.isEmpty(getPreferences(MODE_PRIVATE).getString(EMAIL, null));
    }

    private void showSetEmailDialog() {
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        new AlertDialog.Builder(this).setMessage("Add your email to save your ideas")
            .setView(input)
            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override public void onClick(DialogInterface dialog, int which) {
                    saveEmail(input.getText().toString());
                }
            })
            .show();
    }

    private void saveEmail(String email) {
        getPreferences(MODE_PRIVATE).edit().putString(EMAIL, email).commit();
    }
}
