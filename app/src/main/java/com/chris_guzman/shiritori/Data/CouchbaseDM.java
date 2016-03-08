package com.chris_guzman.shiritori.Data;

import android.content.Context;
import android.util.Log;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Manager;
import com.couchbase.lite.android.AndroidContext;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (c) 2015 OrderUp. All rights reserved.
 */
public class CouchbaseDM {
    private final Context context;
    private Database database;
    private Manager manager;
    public static final String DB_NAME = "ideas";
    public static final String TAG = "ideas";

    public CouchbaseDM(Context context) {
        this.context = context;
    }

    public Database getDatabaseInstance() throws CouchbaseLiteException {
        if ((this.database == null) & (this.manager != null)) {
            this.database = manager.getDatabase(DB_NAME);
        }
        return database;
    }
    public Manager getManagerInstance(Context context) throws IOException {
        if (manager == null) {
            manager = new Manager(new AndroidContext(context), Manager.DEFAULT_OPTIONS);
        }
        return manager;
    }

    public Document createCouchDBDocument(Context context) {
        Manager manager = null;
        Database database = null;
        try {
            manager = getManagerInstance(context);
            database = manager.getDatabase(DB_NAME);
            return database.createDocument();
        } catch (Exception e) {
            Log.e(TAG, "Error getting database", e);
            return null;
        }

        // Create the document
//        String documentId = createDocument(database).getId();
    /* Get and output the contents */
//        outputContents(database, documentId);
    /* Update the document and add an attachment */
//        updateDoc(database, documentId);
        // Add an attachment
//        addAttachment(database, documentId);
    /* Get and output the contents with the attachment */
//        outputContentsWithAttachment(database, documentId);
    }

    public void updateDoc(String documentId, String key, String value) {
        Document document = null;
        try {
            document = getDatabaseInstance().getDocument(documentId);
        }
        catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        try {
            // Update the document with more data
            Map<String, Object> updatedProperties = new HashMap<String, Object>();
            updatedProperties.putAll(document.getProperties());
            updatedProperties.put(key, value);
            // Save to the Couchbase local Couchbase Lite DB
            document.putProperties(updatedProperties);
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error putting", e);
        }
    }


    private static CouchbaseDM instance;

    public static CouchbaseDM init(Context context) {
        instance = new CouchbaseDM(context);
        return instance;
    }

    public static CouchbaseDM getInstance() {
        return instance;
    }

}
