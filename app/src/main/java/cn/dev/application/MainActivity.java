package cn.dev.application;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import cn.dev.application.loopjnet.Callback;
import cn.dev.application.loopjnet.HttpClient;
import cn.dev.application.loopjnet.RequestBody;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        test();
        test();
        test();
        test();
    }

    private void test() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("string","http come");
            jsonObject.put("boolean",true);
            jsonObject.put("int","998");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = new RequestBody(jsonObject);
        HttpClient.getInstance().post(null, requestBody, new Callback() {
            @Override
            public void onFailure(String errorDes) {
                Log.i(HttpClient.TAG,"MainActivity onFailure");
            }

            @Override
            public void onSuccess(String responseString) {
                Log.i(HttpClient.TAG,"MainActivity onSuccess");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify f parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
