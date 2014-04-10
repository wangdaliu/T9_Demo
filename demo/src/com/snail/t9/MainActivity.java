package com.snail.t9;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import com.t9.T9SearchCache;


public class MainActivity extends Activity implements DialpadLayout.DialpadOnKeyDown, View.OnClickListener {

    private ListView mListView;
    private T9SearchCache mT9Search;
    private T9SearchCache.T9Adapter mT9Adapter;
    private T9SearchCache.Callback mT9Callback = new T9SearchCache.Callback() {
        @Override
        public void onLoadFinished() {
            searchContacts(mInput);
        }
    };
    private EditText mDigits;
    private String mInput;
    private DialpadLayout dialpadLayout;
    private ImageView keyBackspace;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);

        mT9Search = T9SearchCache.getInstance(this);
        mListView = (ListView) findViewById(R.id.search_list);
        mDigits = (EditText) findViewById(R.id.input);

        dialpadLayout = (DialpadLayout) findViewById(R.id.dialpad_layout);
        keyBackspace = (ImageView) findViewById(R.id.key_backspace);
        dialpadLayout.setOnKeyDown(this);

        keyBackspace.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mT9Search.refresh(mT9Callback);
    }

    @Override
    public void onStop() {
        super.onStop();
        mT9Search.cancelRefresh(mT9Callback);
    }


    /**
     * Initiates a search for the dialed digits
     * Toggles view visibility based on results
     */
    private void searchContacts(String input) {

        if (!TextUtils.isEmpty(input)) {
            final int length = input.length();
            if (length > 0) {
                mListView.setVisibility(View.VISIBLE);
                T9SearchCache.T9SearchResult result = mT9Search.search(input);

                if (result != null) {
                    if (mT9Adapter == null) {
                        mT9Adapter = mT9Search.createT9Adapter(this, result.getResults());
                        mT9Adapter.setNotifyOnChange(true);
                    } else {
                        mT9Adapter.clear();
                        mT9Adapter.addAll(result.getResults());
                    }
                    if (mListView.getAdapter() == null) {
                        mListView.setAdapter(mT9Adapter);
                    }
                } else {
                    mListView.setVisibility(View.INVISIBLE);
                }
            }
        } else {
            mListView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void dialpadOnKeyDown(int keyCode) {
        KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
        mDigits.onKeyDown(keyCode, event);
        final int length = mDigits.length();
        keyBackspace.setVisibility(length >0 ? View.VISIBLE : View.GONE);
        if (length == mDigits.getSelectionStart() && length == mDigits.getSelectionEnd()) {
            mDigits.setCursorVisible(false);
            mInput = mDigits.getText().toString();
            searchContacts(mInput);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.key_backspace:
                dialpadOnKeyDown(KeyEvent.KEYCODE_DEL);
                break;

            default:
                break;
        }
    }
}
