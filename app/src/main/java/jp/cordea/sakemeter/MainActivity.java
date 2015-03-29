package jp.cordea.sakemeter;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button sakemeter_b = (Button) findViewById(R.id.sakemeter_b);
        final Button limit_b = (Button) findViewById(R.id.limit_b);

        sakemeter_b.setOnClickListener(this);
        limit_b.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.sakemeter_b :
                Log.i("INFO", "enter sakemeter");
                intent = new Intent(this, jp.cordea.sakemeter.SakeMeterActivity.class);
                startActivity(intent);
            case R.id.limit_b :
                intent = new Intent(this, jp.cordea.sakemeter.SakeLimitActivity.class);
                startActivity(intent);
            break;
        }
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
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
