package jp.cordea.sakemeter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by CORDEA on 2015/03/29.
 */
public class SakeMeterActivity extends ActionBarActivity implements View.OnClickListener {
    private static final String _LOG_TAG = "SakeMeterVerbose";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sakemeter);

        String[] arraySpinner = new String[] {"Beer", "Sake", "Wine"};
        addItems(arraySpinner);

        final Button button = (Button) findViewById(R.id.sake_ok);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Log.i(_LOG_TAG, "onclick");
        switch (view.getId()) {
            case R.id.sake_ok :
                Spinner sakeSpinner = (Spinner)findViewById(R.id.sake_spinner);
                String  sake        = (String) sakeSpinner.getSelectedItem().toString();
                addTableRow(sake, loopSake(sake));
                Log.i(_LOG_TAG, "sake_ok push");
            break;
        }
    }

    private void addTableRow(String sake, int[] intArray) {
        int count = intArray[0];
        int index = intArray[1];

        TableLayout tableLayout = (TableLayout)findViewById(R.id.sake_log);
        //setContentView(tableLayout);

        int padding = 10;
        TextView sake_tv    = new TextView(this);
        TextView pad_tv     = new TextView(this);
        TextView count_tv   = new TextView(this);
        TextView[] tvArray  = {sake_tv, count_tv, pad_tv};

        for (TextView tv : tvArray) {
            tv.setPadding(padding+10, padding, padding+10, padding);
            tv.setTextSize(20);
        }

        sake_tv.setText(sake);
        pad_tv.setText(" : ");
        ++count;
        count_tv.setText(Integer.toString(count));

        TableRow tableRow = new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        tableRow.setLayoutParams(lp);

        tableRow.addView(sake_tv);
        tableRow.addView(pad_tv);
        tableRow.addView(count_tv);

        if (index != -1) tableLayout.removeViewAt(index);
        tableLayout.addView(tableRow);
    }

    private int[] loopSake(String sake) {
        TableLayout tl = (TableLayout)findViewById(R.id.sake_log);
        int[] intArray  = {0, -1};

        int count = (int) tl.getChildCount();
        for (int k = 0; k < count; k++) {
            TableRow tr = (TableRow) tl.getChildAt(k);
            // 0: sake name
            TextView sake_tv = (TextView) tr.getChildAt(0);
            if (sake == sake_tv.getText().toString()) {
                // 2: count
                TextView count_tv = (TextView) tr.getChildAt(2);
                intArray[0] = Integer.parseInt(count_tv.getText().toString());
                intArray[1] = k;
                return intArray;
            }
        }
        Log.i(_LOG_TAG, Integer.toString(count));
        return intArray;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void addItems(String[] array) {
        Spinner spinner = (Spinner)findViewById(R.id.sake_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, array);
        spinner.setAdapter(adapter);
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
