package jp.cordea.sakemeter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by CORDEA on 2015/03/29.
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
                Intent intent = new Intent(this, jp.cordea.sakemeter.SakeMeterActivity.class);
                writeCache(false);
                addTableRow();
            break;
        }
    }

    private void addTableRow() {
        HashMap<String, String> hashMap = (HashMap<String, String>) controlCache.readCache(getCacheDir());
        TableLayout tableLayout = (TableLayout)findViewById(R.id.limit_table);
        int padding = 10;
        //setContentView(tableLayout);

        removeRows();

        for (Map.Entry<String, String> map : hashMap.entrySet()) {
            Log.i("addTableRow", findViewById(R.id.limit_table).toString());

            TextView sake_tv        = new TextView(this);
            TextView limit_tv       = new TextView(this);
            TextView[] tvArray      = {sake_tv, limit_tv};

            String  sake   = map.getKey();
            String  limit  = map.getValue();

            for (TextView tv : tvArray) {
                tv.setPadding(padding + 10, padding, padding + 10, padding);
                tv.setTextSize(20);
            }

            EditText et = new EditText(this);
            et.setInputType(InputType.TYPE_CLASS_NUMBER);

            sake_tv.setText(sake);
            limit_tv.setText(limit);

            TableRow tableRow           = new TableRow(this);
            TableRow.LayoutParams lp    = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            tableRow.setLayoutParams(lp);

            tableRow.addView(sake_tv);
            tableRow.addView(limit_tv);
            tableRow.addView(et);
            Log.i("addTableRow", sake + ": " + limit);

            tableLayout.addView(tableRow);
        }
    }

    private void removeRows() {
        TableLayout tableLayout = (TableLayout)findViewById(R.id.limit_table);

        int count = (int) tableLayout.getChildCount();
        for (int k = 1; k < count; k++) {
            tableLayout.removeViewAt(1);
        }
    }

    public HashMap<String, String> writeCache(boolean flag) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        String[] sakeArray = {"Beer", "Wine", "Sake"};

        if (flag) {
            for (String str : sakeArray) {
                hashMap.put(str, "0");
            }
        } else {
            hashMap = readRowItems();
        }

        try {
            File cacheDir = getCacheDir();
            File file = new File(cacheDir.getAbsolutePath(), "sakeMeter.csv");
            FileOutputStream fos = new FileOutputStream(file);
            try {
                if (!file.exists()) file.createNewFile();
                for (Map.Entry<String, String> hm : hashMap.entrySet()) {
                    String content = hm.getKey() + "," + hm.getValue().toString() + ":";
                    byte[] contentInBytes = content.getBytes();
                    fos.write(contentInBytes);
                    fos.flush();
                    Log.i("writeCache", content);
                }
                fos.close();
            } catch (IOException e) {}
        } catch (FileNotFoundException e) {}
        return hashMap;
    }

    private HashMap<String, String> readRowItems() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        TableLayout tl = (TableLayout)findViewById(R.id.limit_table);

        int count = (int) tl.getChildCount();
        for (int k = 1; k < count; k++) {
            TableRow tr = (TableRow) tl.getChildAt(k);
            TextView sake_tv    = (TextView) tr.getChildAt(0);
            EditText limit_tv   = (EditText) tr.getChildAt(2);
            hashMap.put(sake_tv.getText().toString(), limit_tv.getText().toString());
        }
        return hashMap;
    }

    private void recSakeLimit() {

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
