package jp.cordea.sakemeter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import jp.cordea.sakemeter.model.Drink;
import jp.cordea.sakemeter.model.SakeInfo;
import rx.Observable;

public class MeterFragment extends Fragment {

    @Bind(R.id.button)
    Button button;

    @Bind(R.id.spinner)
    Spinner spinner;

    @Bind(R.id.pie_chart)
    PieChart pieChart;

    public MeterFragment() {
        // Required empty public constructor
    }

    public static MeterFragment newInstance() {
        MeterFragment fragment = new MeterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meter, container, false);
        ButterKnife.bind(this, view);

        pieChart.setUsePercentValues(true);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setDescription("");

        HashMap<Sake, SakeInfo> sakeInfoHashMap = SakeInfoUtils.getSakeInfo();
        final List<String> items =
                Observable
                        .from(sakeInfoHashMap.keySet())
                        .map(Enum::name)
                        .toList()
                        .toBlocking()
                        .first();

        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);

        button.setOnClickListener(view1 -> {
            String sake = (String) spinner.getSelectedItem();
            Realm realm = Realm.getInstance(getContext());
            Drink updateTarget = realm.where(Drink.class).equalTo("date", new LocalDate().toString()).equalTo("sake", sake).findFirst();

            realm.beginTransaction();
            int limit = 0, vot = 1;
            if (updateTarget != null) {
                limit = updateTarget.getLimit();
                vot = updateTarget.getVot() + 1;
                updateTarget.removeFromRealm();
            }

            Drink drink = new Drink();
            drink.setDate(new LocalDate().toString());
            drink.setSake(sake);
            drink.setLimit(limit);
            drink.setVot(vot);

            realm.copyToRealm(drink);
            realm.commitTransaction();
            realm.close();
            invalidateGraph(sakeInfoHashMap);
        });

        invalidateGraph(sakeInfoHashMap);
        return view;
    }

    private void showLimitExceedDialog() {
        new AlertDialog
                .Builder(getContext())
                .setTitle("")
                .setMessage("")
                .setCancelable(true)
                .show();
    }

    private void invalidateGraph(HashMap<Sake, SakeInfo> sakeInfoHashMap) {
        Realm realm = Realm.getInstance(getContext());

        RealmResults<Drink> drinks = realm.where(Drink.class).equalTo("date", new LocalDate().toString()).findAll();

        float limitVol = 0;
        float nowVol = 0;
        for (Drink drink : drinks) {
            Log.i("xxx", drink.toString());
            SakeInfo info = sakeInfoHashMap.get(Sake.valueOf(drink.getSake()));
            nowVol += (info.getAlcohol() / 100) * info.getVolume() * drink.getVot();
            limitVol += (info.getAlcohol() / 100) * info.getVolume() * drink.getLimit();
        }

        List<Entry> entryList = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        for (int i = 0; i < drinks.size(); i++) {
            entryList.add(new Entry((float)drinks.get(i).getVot(), i));
            labels.add(drinks.get(i).getSake());
        }
        realm.close();

        PieDataSet set = new PieDataSet(entryList, "");
        set.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(labels, set);

        pieChart.setData(data);
        pieChart.setCenterText(getCenterText(limitVol, nowVol));
        pieChart.invalidate();

        if (limitVol <= nowVol) {
            showLimitExceedDialog();
        }
    }

    private SpannableString getCenterText(float limit, float now) {
        SpannableString s = new SpannableString(String.format("Drink %.2f ml\nLimit %.2f ml", now, limit));
        s.setSpan(new RelativeSizeSpan(1.5f), 0, 9 + (Float.toString(now).length() + 2), 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 9 + (Float.toString(now).length() + 2), s.length(), 0);
        s.setSpan(new RelativeSizeSpan(.9f), 9 + (Float.toString(now).length() + 2), s.length(), 0);
        return s;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
