package jp.cordea.sakemeter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import jp.cordea.sakemeter.model.Drink;
import jp.cordea.sakemeter.model.Limit;
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

            RealmQuery<Drink> query = realm.where(Drink.class).equalTo("sake", sake);

            Drink drink = new Drink();
            drink.setSake(sake);
            Drink q = query.findFirst();
            drink.setVot(q == null ? 1 : q.getVot() + 1);

            realm.executeTransaction(realm1 -> realm1.copyToRealmOrUpdate(drink));
            realm.close();
            invalidateGraph();
        });

        invalidateGraph();
        return view;
    }

    private void invalidateGraph() {
        Realm realm = Realm.getInstance(getContext());
        RealmResults<Drink> drinks = realm.where(Drink.class).findAll();

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
        pieChart.invalidate();
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
