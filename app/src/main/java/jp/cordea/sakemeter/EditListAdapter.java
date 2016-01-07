package jp.cordea.sakemeter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import io.realm.Realm;
import jp.cordea.sakemeter.model.EditListItem;
import jp.cordea.sakemeter.model.Limit;

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
        Limit limit = new Limit();
        limit.setLimit(l);
        limit.setSake(getItem(position).getTitle());
        Realm realm = Realm.getInstance(getContext());
        realm.executeTransaction(realm1 -> realm1.copyToRealmOrUpdate(limit));
        realm.close();

        getItem(position).setLimit(l);
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
