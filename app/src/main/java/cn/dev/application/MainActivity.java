package cn.dev.application;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import cn.dev.application.loopjnet.API;
import cn.dev.application.loopjnet.Callback;
import cn.dev.application.loopjnet.HttpClient;
import cn.dev.application.loopjnet.Request;
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
    }

    private String url = "http://p.gdown.baidu.com/c10e754d2c07d06165a7fa77f828ff56253660db7aa48730409b871c662531415a979901a915832372946cd73b5af6284df8f986121bc76839ee05b565e7275c3b40c5e2767c18aa60de2434625777ed38f55195ab71a5f629a80630d3d92ea8b2c0ca7d85810c56f5e2eccd3fda703b97799b69371a48f5b978286c8a939045e76dce7129114084441670b5b360ff9c4cc7ad98926f35d09ad6041a841fd9d1c78aea881d6081919024f81b18c95d0ed9fa2e0d0d81805af7012ecdde03125f0daa4deff75c9c1c9d29645460a141c03c01ad0e57d30d09f92dc1b8d0437d6544e906e1f146c2282093102d7e6c0d3ef5a7e5aeae3de57bf5a94af9e413976f250cd3f662fedd0031352b9410f892b75aee705cfd2c4a490470e74d95835a9fabaea301eebf607f3ce0c87c8791a6f0a3ba11adb9f775ddb8420f96f1a4d67c";

    public void download(View view){
        RequestBody body = new RequestBody()
                .addParams("aaa","111")
                .addParams("array",new String[]{"aaa","bbb"});
        Request request = new Request.Builder()
                .url(API.BASE_URL)
                .body(body)
                .encrypt(true)
                .build();
        HttpClient.getInstance().newCall(request).execute(new Callback() {
            @Override
            public void onFailure(String errorDes) {

            }

            @Override
            public void onSuccess(String responseString) {

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
