package jp.cordea.sakemeter;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.HashMap;

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

public class SakeLimitActivity extends ActionBarActivity implements View.OnClickListener {
    private int height = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sakelimit);



        final Button button = (Button) findViewById(R.id.register_b);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_b :
                controlCache.writeCache(false, getCacheDir(), "sakeMeter.limit.csv");
                addTableRow();
            break;
        }
    }

    private void addTableRow() {
        HashMap<String, String> hashMap = controlCache.readCache(false, getCacheDir(), "sakeMeter.limit.csv");
        TableLayout tableLayout = (TableLayout)findViewById(R.id.limit_table);
        int padding = 10;
        //setContentView(tableLayout);

        removeRows();

        for (String sake : general.sakeArray) {
            Log.i("addTableRow", findViewById(R.id.limit_table).toString());

            TextView sake_tv        = new TextView(this);
            TextView limit_tv       = new TextView(this);
            TextView[] tvArray      = {sake_tv, limit_tv};

            String limit = "0";
            if (hashMap.containsKey(sake)) limit = hashMap.get(sake);

            for (TextView tv : tvArray) {
                tv.setPadding(padding + 10, padding, padding + 10, padding);
                //tv.setBackgroundResource(R.drawable.border);
                tv.setHeight(height);
                tv.setTextSize(18);
            }
            Log.i("addTableRow", Integer.toString(sake_tv.getHeight()));

            EditText et = new EditText(this);
            et.setInputType(InputType.TYPE_CLASS_NUMBER);
               et.setHeight(height);
            et.setPadding(padding, padding, padding + 10, padding);
            //et.setBackgroundResource(R.drawable.border);

            sake_tv.setText(sake);
            limit_tv.setText(limit);

            TableRow tableRow           = new TableRow(this);
            TableRow.LayoutParams lp    = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            tableRow.setLayoutParams(lp);

            tableRow.addView(sake_tv);
            tableRow.addView(limit_tv);
            tableRow.addView(et);
            tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
            Log.i("addTableRow", sake + ": " + limit);

            tableLayout.addView(tableRow);
        }
    }

    private void removeRows() {
        TableLayout tableLayout = (TableLayout)findViewById(R.id.limit_table);

        int count = tableLayout.getChildCount();
        for (int k = 1; k < count; k++) tableLayout.removeViewAt(1);
    }

    public HashMap<String, String> readRowItems() {
        HashMap<String, String> hashMap = new HashMap<>();
        TableLayout tl = (TableLayout)findViewById(R.id.limit_table);

        int count = tl.getChildCount();
        for (int k = 1; k < count; k++) {
            TableRow tr         = (TableRow) tl.getChildAt(k);
            TextView sake_tv    = (TextView) tr.getChildAt(0);
            EditText limit_tv   = (EditText) tr.getChildAt(2);
            hashMap.put(sake_tv.getText().toString(), limit_tv.getText().toString());
        }
        return hashMap;
    }

    public Integer calcTolerance(HashMap<String, String> hashMap) {
        HashMap<String, Integer> sakeMap = general.sakeMap();
        int acceptable               = 0;
        // ref. http://stackoverflow.com/questions/1066589/iterate-through-a-hashmap
        for (String sake : hashMap.keySet()) {
            int count = Integer.parseInt(hashMap.get(sake));
            int freq  = sakeMap.get(sake);
            if (acceptable < count * freq) acceptable = count * freq;
        }
        return acceptable;
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        TextView tv = (TextView) findViewById(R.id.species);
        height = tv.getHeight();
        Log.i("onWindowFocusChanged", Integer.toString(height));

        addTableRow();
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
