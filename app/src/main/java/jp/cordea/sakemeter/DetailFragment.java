package jp.cordea.sakemeter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import jp.cordea.sakemeter.model.Drink;
import jp.cordea.sakemeter.model.SakeInfo;
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

        combinedChart.setDrawOrder(new CombinedChart.DrawOrder[] {CombinedChart.DrawOrder.LINE, CombinedChart.DrawOrder.BAR});

        Params params = getLineData();
        CombinedData data = new CombinedData(new ArrayList<>(params.getXLabels()));

        data.setData(params.getLineData());
        data.setData(params.getBarData());

        combinedChart.setData(data);

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

        Map<Sake, SakeInfo> sakeInfoMap = SakeInfoUtils.getSakeInfo();

        Map<Date, Float> limitMap = new LinkedHashMap<>();
        Map<Date, Float> barMap = new LinkedHashMap<>();
        for (Drink drink : drinks) {
            Date date = drink.getDate();
            SakeInfo info = sakeInfoMap.get(Sake.valueOf(drink.getSake()));
            float limit = (info.getAlcohol() / 100) * info.getVolume() * drink.getLimit();
            float vot = (info.getAlcohol() / 100) * info.getVolume() * drink.getVot();

            if (limitMap.containsKey(date)) {
                limitMap.put(date, limitMap.get(date) + limit);
                barMap.put(date, barMap.get(date) + vot);
                continue;
            }
            limitMap.put(date, limit);
            barMap.put(date, vot);
        }

        Set<String> set = new HashSet<>();

        int i = 0;
        for (Map.Entry<Date, Float> entry : limitMap.entrySet()) {
            lineEntries.add(new Entry(entry.getValue(), i));
            set.add(entry.getKey().toString());
            ++i;
        }
        i = 0;
        for (Map.Entry<Date, Float> entry : barMap.entrySet()) {
            barEntries.add(new BarEntry(entry.getValue(), i));
            ++i;
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "");
        LineDataSet lineDataSet = new LineDataSet(lineEntries, "");

        barData.addDataSet(barDataSet);
        lineData.addDataSet(lineDataSet);
        return new Params(barData, lineData, set);
    }

    @Getter
    @AllArgsConstructor
    private class Params {
        BarData barData;
        LineData lineData;
        Set<String> xLabels;
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
