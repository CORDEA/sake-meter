package jp.cordea.sakemeter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.joda.time.LocalDate;

import java.util.Date;
import java.util.List;

import io.realm.Realm;
import jp.cordea.sakemeter.model.Drink;
import jp.cordea.sakemeter.model.EditListItem;

/**
 * Created by Yoshihiro Tanaka on 16/01/07.
 */
public class EditListAdapter extends ArrayAdapter<EditListItem> {

    private List<EditListItem> editListItems;

    public EditListAdapter(Context context, List<EditListItem> editListItems) {
        super(context, R.layout.list_item);
        this.editListItems = editListItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        EditListItem item = getItem(position);
        TextView textView = (TextView) convertView.findViewById(R.id.title);
        textView.setText(item.getTitle());
        textView = (TextView) convertView.findViewById(R.id.detail);
        textView.setText(item.getDetail());
        textView = (TextView)  convertView.findViewById(R.id.limit);
        textView.setText(Integer.toString(item.getLimit()));

        return convertView;
    }

    public void onLimitChanged(int position, int l) {
        Realm realm = Realm.getInstance(getContext());
        Drink updateTarget = realm.where(Drink.class)
                .equalTo("date", new LocalDate().toDate())
                .equalTo("sake", getItem(position).getTitle())
                .findFirst();

        realm.beginTransaction();

        int vot = 0;
        if (updateTarget != null) {
            vot = updateTarget.getVot();
            updateTarget.removeFromRealm();
        }

        Drink drink = new Drink();
        drink.setDate(new LocalDate().toDate());
        drink.setSake(getItem(position).getTitle());
        drink.setVot(vot);
        drink.setLimit(l);

        realm.copyToRealm(drink);
        realm.commitTransaction();
        realm.close();

        editListItems.get(position).setLimit(l);
        notifyDataSetChanged();
    }

    @Override
    public EditListItem getItem(int position) {
        return editListItems.get(position);
    }

    @Override
    public int getCount() {
        return editListItems.size();
    }
}
