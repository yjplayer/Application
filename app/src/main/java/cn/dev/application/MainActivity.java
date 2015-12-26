package cn.dev.application;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.dev.application.utils.ImageUtils;
import cn.dev.application.utils.SPUtils;


/*
ldpi
mdpi
hdpi
xhdpi
xxhdpi
*/

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

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        Toast.makeText(this,"//"+displayMetrics.densityDpi,Toast.LENGTH_LONG).show();

        SPUtils.logSP();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.a, options);
        Log.i("ImageUtils",options.outWidth+"\t"+options.outHeight);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.a);
        ImageUtils.logBitmapInfo(bitmap);
        ImageView imageView = (ImageView) findViewById(R.id.iv);
        imageView.setImageBitmap(bitmap);

        ImageUtils.logBitmapInfo(imageView);

    }

    private void test() {
        TextView textView1 = (TextView) findViewById(R.id.tv_01);
        TextView textView2 = (TextView) findViewById(R.id.tv_02);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int w = displayMetrics.widthPixels;
        int h = displayMetrics.heightPixels;

//        textView1.setText("TextView 1：" + Math.sqrt((320 * 320) + (480 * 480)) / 3.2);
        textView1.setText("w:"+w+"h:"+h);

        WindowManager windowManager = (WindowManager)getSystemService(
                Context.WINDOW_SERVICE);
        Display d = windowManager.getDefaultDisplay();
        textView2.setText("w:"+d.getWidth()+"h:"+d.getHeight());

        textView1.setTextSize(25);

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
