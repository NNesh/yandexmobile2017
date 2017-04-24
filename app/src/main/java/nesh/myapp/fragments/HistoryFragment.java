package nesh.myapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nesh.myapp.R;
import nesh.myapp.objects.TrResult;

/**
 * Created by nesh on 4/23/17.
 */

public class HistoryFragment extends PlaceholderFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.history_main, container, false);

        final ListView listView = ((ListView) rootView.findViewById(R.id.history_list));

        List<String> list = new ArrayList<>();
        List<TrResult> listObj = TrResult.find(TrResult.class, "type_rec = ?", TrResult.HISTORY_TYPE);

        Iterator<TrResult> iterator = listObj.iterator();
        while(iterator.hasNext()) {
            TrResult object = iterator.next();
            if ((object.getText() != null) && (object.getLang() != null) && (object.getOriginalText() != null)) {
                list.add(object.getOriginalText() + " - " + object.getText() + " (" + object.getLang() + ")");
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(), R.layout.item_layout, R.id.item_translate, list);
        listView.setAdapter(adapter);

        return rootView;
    }
}
