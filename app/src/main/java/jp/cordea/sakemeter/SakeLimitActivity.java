package jp.cordea.sakemeter;

import android.graphics.Typeface;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

public class SakeLimitActivity extends ActionBarActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sakelimit);

        addTableRow();

        final Button button = (Button) findViewById(R.id.register_b);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_b :
                writeCache(false, getCacheDir(), general.limitFile);
                addTableRow();
            break;
        }
    }

    private void addTableRow() {
        HashMap<String, String> sakeMap     = controlCache.readCache(false, getCacheDir(), general.limitFile);
        TableLayout             tableLayout = (TableLayout) findViewById(R.id.limit_table);

        int padding = 10;
        //setContentView(tableLayout);

        removeRows();

        for (String sake : general.sakeArray) {
            Log.i("addTableRow", findViewById(R.id.limit_table).toString());

            TextView    sake_tv     = new TextView(this);
            TextView    limit_tv    = new TextView(this);
            TextView[]  tvArray     = {sake_tv, limit_tv};

            String limit = "0";

            if (sakeMap.containsKey(sake)) limit = sakeMap.get(sake);

            for (TextView tv : tvArray) {
                tv.setPadding(padding, padding, padding + 10, padding);
                //tv.setBackgroundResource(R.drawable.border);
                tv.setTextSize(15);
                tv.setTypeface(Typeface.SERIF);
            }
            Log.i("addTableRow", Integer.toString(sake_tv.getHeight()));

            EditText et = new EditText(this);

            et.setInputType(InputType.TYPE_CLASS_NUMBER);
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
        TableLayout tableLayout = (TableLayout) findViewById(R.id.limit_table);

        int count = tableLayout.getChildCount();
        for (int k = 1; k < count; k++) tableLayout.removeViewAt(1);
    }

    public HashMap<String, String> writeCache(boolean flag, File cacheDir, String fileName) {
        HashMap<String, String> hashMap = new HashMap<>();

        String  acceptable  = "0";

        if (flag) for (String sake : general.sakeArray) hashMap.put(sake, Integer.toString(0));
        else {
            hashMap     = readRowItems();
            acceptable  = calcTolerance(hashMap).toString();
        }

        try {
            File                file    = new File(cacheDir.getAbsolutePath(), fileName);
            FileOutputStream    fos     = new FileOutputStream(file);
            try {
                if (!file.exists()) file.createNewFile();
                for (Map.Entry<String, String> hm : hashMap.entrySet()) {
                    String content = hm.getKey() + "," + hm.getValue().toString() + ":";
                    byte[] contentInBytes = content.getBytes();

                    fos.write(contentInBytes);
                    fos.flush();
                }
                fos.write(acceptable.getBytes());
                fos.flush();

                fos.close();
            } catch (IOException e) {}
        } catch (FileNotFoundException e) {}
        return hashMap;
    }

    public HashMap<String, String> readRowItems() {
        HashMap<String, String> sakeMap = new HashMap<>();
        TableLayout             tl      = (TableLayout) findViewById(R.id.limit_table);

        int count = tl.getChildCount();
        for (int k = 1; k < count; k++) {
            TableRow tr             = (TableRow) tl.getChildAt(k);

            TextView sake_tv        = (TextView) tr.getChildAt(0);
            TextView oldLimit_tv    = (TextView) tr.getChildAt(1);
            EditText limit_tv       = (EditText) tr.getChildAt(2);

            String   limit          = limit_tv.getText().toString();
            int      oldLimit       = Integer.parseInt(oldLimit_tv.getText().toString());
            if (limit_tv.length() == 0) {
                if (oldLimit != 0)  limit = Integer.toString(oldLimit);
                else                limit = "0";
            }
            sakeMap.put(sake_tv.getText().toString(), limit);
        }
        return sakeMap;
    }

    public Integer calcTolerance(HashMap<String, String> hashMap) {
        HashMap<String, Integer> sakeMap = general.sakeMap();

        int acceptable = 0;

        // ref. http://stackoverflow.com/questions/1066589/iterate-through-a-hashmap
        for (String sake : hashMap.keySet()) {
            int count = Integer.parseInt(hashMap.get(sake));
            int freq  = sakeMap.get(sake);

            if (acceptable < count * freq) acceptable = count * freq;
        }
        return acceptable;
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
