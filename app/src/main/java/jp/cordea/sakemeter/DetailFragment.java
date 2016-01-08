package jp.cordea.sakemeter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import jp.cordea.sakemeter.model.Drink;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class DetailFragment extends Fragment {

    @Bind(R.id.combined_chart)
    CombinedChart combinedChart;

    public DetailFragment() {
        // Required empty public constructor
    }

    public static DetailFragment newInstance() {
        DetailFragment fragment = new DetailFragment();
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
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, view);

        combinedChart.setDescription("");
        combinedChart.setDrawGridBackground(true);
        combinedChart.setDrawBarShadow(false);

        combinedChart.setDrawOrder(new CombinedChart.DrawOrder[] {CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.LINE});

        CombinedData data = new CombinedData();
        Params params = getLineData();

        if (params != null) {
            data.setData(params.getLineData());
            data.setData(params.getBarData());
        }

        combinedChart.invalidate();
        return view;
    }

    private Params getLineData() {
        BarData barData = new BarData();
        LineData lineData = new LineData();

        List<Entry> lineEntries = new ArrayList<>();
        List<BarEntry> barEntries = new ArrayList<>();
        Realm realm = Realm.getInstance(getContext());
        RealmResults<Drink> drinkRealmResults = realm.where(Drink.class).findAll();
        List<Drink> drinks = Arrays.asList(drinkRealmResults.toArray(new Drink[drinkRealmResults.size()]));
        Collections.sort(drinks, (drink, t1) -> drink.getDate().compareTo(t1.getDate()));

        Map<Date, Integer> limitMap = new LinkedHashMap<>();
        Map<Date, Integer> barMap = new LinkedHashMap<>();
        for (Drink drink : drinks) {
            Date date = drink.getDate();
            if (limitMap.containsKey(date)) {
                limitMap.put(date, limitMap.get(date) + drink.getLimit());
                barMap.put(date, barMap.get(date) + drink.getVot());
                continue;
            }
            limitMap.put(date, drink.getLimit());
            barMap.put(date, drink.getVot());
        }

        if (limitMap.size() < 2 || barMap.size() < 2) {
            return null;
        }

        int i = 0;
        for (Map.Entry<Date, Integer> entry : limitMap.entrySet()) {
            lineEntries.add(new Entry(entry.getValue(), i));
            ++i;
        }
        i = 0;
        for (Map.Entry<Date, Integer> entry : barMap.entrySet()) {
            barEntries.add(new BarEntry(entry.getValue(), i));
            ++i;
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "");
        LineDataSet lineDataSet = new LineDataSet(lineEntries, "");

        barData.addDataSet(barDataSet);
        lineData.addDataSet(lineDataSet);
        return new Params(barData, lineData);
    }

    @Getter
    @AllArgsConstructor
    private class Params {
        BarData barData;
        LineData lineData;
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
