package jp.cordea.sakemeter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import jp.cordea.sakemeter.model.EditListItem;
import jp.cordea.sakemeter.model.Limit;
import rx.Observable;


public class EditFragment extends Fragment {

    @Bind(R.id.listview)
    ListView listView;

    private EditListAdapter adapter;

    public EditFragment() {
        // Required empty public constructor
    }

    public static EditFragment newInstance() {
        EditFragment fragment = new EditFragment();
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
        View view = inflater.inflate(R.layout.fragment_edit, container, false);
        ButterKnife.bind(this, view);

        List<EditListItem> items = new ArrayList<>();
        Realm realm = Realm.getInstance(getContext());
        for (String sake : SakeInfoUtils.getStringSakeList()) {
            Limit limit = realm.where(Limit.class).equalTo("sake", sake).findFirst();
            EditListItem editListItem = new EditListItem(sake, "", limit == null ? 0 : limit.getLimit());
            items.add(editListItem);
        }
        realm.close();

        adapter = new EditListAdapter(getContext(), items);
        listView.setAdapter(adapter);

        CharSequence[] dialogItems =
                Observable
                        .range(0, 100)
                        .map(Object::toString)
                        .toList()
                        .toBlocking()
                        .first()
                        .toArray(new CharSequence[100]);

        listView.setOnItemClickListener(
                (adapterView, view1, pos, l) ->
                        new AlertDialog.Builder(getContext())
                                .setTitle("")
                                .setItems(dialogItems,
                                        (dialogInterface, i) -> {
                                            adapter.onLimitChanged(pos, Integer.parseInt(dialogItems[i].toString()));
                                            dialogInterface.dismiss();
                                        })
                                .create()
                                .show()
        );

        return view;
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
