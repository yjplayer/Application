package cn.dev.application;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import cn.dev.application.utils.UIUtils;

/**
 * Created by air on 2015/12/28.
 */
public class KeyboardActivity extends AppCompatActivity {

    public static final String TAG = "KeyboardActivity";
    public EditText editText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_keyboard);
        editText = (EditText) findViewById(R.id.et_01);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;

                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    handled = true;
                }

                Log.i(TAG, handled + "\tactionId:" + actionId);
                return handled;
            }
        });
    }

    int i = 1;
    public void click(View view) {
        i++;
        if (i%2 == 0){
            Log.i(TAG,"hide");
            UIUtils.hideSoftKeyboard(editText);

        }else{
            Log.i(TAG,"show");
            UIUtils.showSoftKeyboard(editText);

        }
    }
}
