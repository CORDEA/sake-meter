package jp.cordea.sakemeter;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright [2015] [Yoshihiro Tanaka]
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author: Yoshihiro Tanaka<contact@cordea.jp>
 * Date  : 2015/03/29
 */

public class SakeMeterActivity extends ActionBarActivity implements View.OnClickListener {
    private static final    String _LOG_TAG     = "SakeMeterVerbose";
    private static          int    acceptable   = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sakemeter);

        addItems(general.sakeArray);
        tableInitialize();

        acceptable = convertHashMap(controlCache.readCache(true, getCacheDir(), "sakeMeter.limit.csv")).get("acceptable");

        final Button button = (Button) findViewById(R.id.sake_ok);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Log.i(_LOG_TAG, "onclick");
        switch (view.getId()) {
            case R.id.sake_ok :
                Spinner sakeSpinner                 = (Spinner)findViewById(R.id.sake_spinner);
                String  sake                        = sakeSpinner.getSelectedItem().toString();
                HashMap<String, Integer> limitMap   = convertHashMap(controlCache.readCache(false, getCacheDir(), "sakeMeter.limit.csv"));

                addTableRow(limitMap, sake, loopSake());
                Log.i(_LOG_TAG, "sake_ok push");
            break;
        }
    }

    private void tableInitialize() {
        HashMap<String, Integer>    meterMap    = convertHashMap(controlCache.readCache(false, getCacheDir(), "sakeMeter.meter.csv"));
        HashMap<String, Integer>    limitMap    = convertHashMap(controlCache.readCache(false, getCacheDir(), "sakeMeter.limit.csv"));
        HashMap<String, int[]>      hashMap     = new HashMap<>();
        int[] intArray = {-1, -1};
        for (String sake : meterMap.keySet()) {
            intArray[0] = meterMap.get(sake);
            hashMap.put(sake, intArray);
        }
        for (String sake : meterMap.keySet()) addTableRow(limitMap, sake, hashMap);
    }

    private void addTableRow(HashMap<String, Integer> limitMap, String sake, HashMap<String, int[]> hashMap) {
        Log.i("addTableRow", hashMap.toString());

        int count = hashMap.get(sake)[0];
        int index = hashMap.get(sake)[1];
        for (String str : hashMap.keySet()) Log.i(_LOG_TAG, str + ": " + Integer.toString(hashMap.get(str)[1]));

        TableLayout tableLayout = (TableLayout)findViewById(R.id.sake_log);

        int padding = 10;
        TextView time_tv    = new TextView(this);
        TextView sake_tv    = new TextView(this);
        TextView pad_tv     = new TextView(this);
        TextView count_tv   = new TextView(this);
        TextView[] tvArray  = {time_tv, sake_tv, pad_tv, count_tv};

        for (TextView tv : tvArray) {
            tv.setPadding(padding+10, padding, padding+10, padding);
            tv.setTextSize(25);
        }

        time_tv.setText(getTime());
        sake_tv.setText(sake);
        pad_tv.setText(" : ");
        ++count;
        count_tv.setText(Integer.toString(count));
        if (hashMap.containsKey(sake)) {
            int limit = limitMap.get(sake);
            for (TextView tv : tvArray) {
                if          (count == limit)        tv.setTextColor(Color.BLUE);
                else if     (count >  limit)        tv.setTextColor(Color.RED);
                if          (count >  limit + 2)    tv.setTextSize(25);
            }
        }

        int limit = 0;
        for (String s : hashMap.keySet()) limit += hashMap.get(s)[0] * general.sakeMap().get(s);
        if  (limit > acceptable) pushAlert(limit);

        TableRow tableRow           = new TableRow(this);
        TableRow.LayoutParams lp    = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        tableRow.setLayoutParams(lp);

        for (TextView tv : tvArray) tableRow.addView(tv);

        if (index != -1) tableLayout.removeViewAt(index);
        tableLayout.addView(tableRow);
    }

    private void pushAlert(int limit) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("WARNING")
                .setMessage(
                        "Registered limit: "    + Double.toString(acceptable / 10.0)    + " ml" + "\n" +
                        "Current intake: "      + Double.toString(limit / 10.0)         + " ml"
                )
                .show();
        // ref. http://stackoverflow.com/questions/6562924/changing-font-size-into-an-alertdialog
        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
        textView.setTextSize(25);
    }

    private String getTime() {
        Calendar c = Calendar.getInstance();
        int[] times  = {c.get(Calendar.MONTH)+1, c.get(Calendar.DATE), c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND)};
        String time_str  = "";
        String delimiter;
        for (int i = 0; i < times.length; i++) {
            if      (i == 0)    delimiter = "/";
            else if (i == 1)    delimiter = "-";
            else if (i <= 3)    delimiter = ":";
            else                delimiter = "";

            time_str += Integer.toString(times[i]) + delimiter;
        }
        return time_str;
    }

    private HashMap<String, Integer> convertHashMap(HashMap<String, String> hashMap) {
        HashMap<String, Integer> convertHashMap = new HashMap<String, Integer>();
        for (Map.Entry<String, String> map : hashMap.entrySet()) {
            convertHashMap.put(map.getKey(), (Integer.parseInt(map.getValue())));
        }
        return convertHashMap;
    }

    private HashMap<String, int[]> loopSake() {
        HashMap<String, int[]> hashMap = new HashMap<>();
        TableLayout tl = (TableLayout) findViewById(R.id.sake_log);

        int count = tl.getChildCount();
        for (int k = 0; k < count; k++) {
            TableRow tr = (TableRow) tl.getChildAt(k);
            // 1: sake name
            TextView sake_tv    = (TextView) tr.getChildAt(1);
            TextView count_tv   = (TextView) tr.getChildAt(3);

            int[] intArray = {Integer.parseInt(count_tv.getText().toString()), k};
            hashMap.put(sake_tv.getText().toString(), intArray);
        }
        return hashMap;
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
                R.layout.spinner_item, array);
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
