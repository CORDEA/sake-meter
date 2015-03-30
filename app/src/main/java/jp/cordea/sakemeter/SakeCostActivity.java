package jp.cordea.sakemeter;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by CORDEA on 2015/03/30.
 */
public class SakeCostActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sakecost);

        fixLayout(getDisplaySize());
    }

    private void fixLayout(Point size) {
        int width = size.x;
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.beer_progress);
        TextView textView = (TextView) findViewById(R.id.beer_text);

        progressBar.getLayoutParams().width = ((width / 3) * 2)  - 100;
        textView.setWidth((width / 3) * 1);
    }

    private Point getDisplaySize() {
        // ref. http://stackoverflow.com/questions/5008059/change-the-width-of-a-progressbar-added-at-runtime
        Display display = getWindowManager().getDefaultDisplay();
        Point size      = new Point();
        display.getSize(size);
        return size;
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
