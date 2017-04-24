package nesh.myapp.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import nesh.myapp.MainActivity;
import nesh.myapp.R;
import nesh.myapp.objects.TrResult;
import nesh.myapp.tasks.TranslateTask;
import nesh.myapp.tasks.callbacks.TranslateCallback;

/**
 * Created by nesh on 4/23/17.
 */

public class TranslateFragment extends PlaceholderFragment {
    private MainActivity.SectionsPagerAdapter adapter;
    private TranslateTask mTranslateTask = null;
    protected boolean fieldChanged = false;
    protected TrResult currentResult = null;
    protected TimerHistory timer = null;

    protected class TimerHistory extends CountDownTimer {
        public TimerHistory() {
            super(2000, 1);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            //Do nothing
        }

        @Override
        public void onFinish() {
            saveResult(TrResult.HISTORY_TYPE);
            Log.d("TIMER", "history");
        }
    }

    public TranslateFragment(MainActivity.SectionsPagerAdapter adapter) {
        this.adapter = adapter;
    }

    private void startTranslateTask(String s, String lang, TranslateCallback callback) {
        if (mTranslateTask == null) {
            mTranslateTask = new TranslateTask(s.toString(), lang, callback);
            mTranslateTask.execute();
        }
        else {
            if (mTranslateTask.getStatus() == AsyncTask.Status.RUNNING) {
                mTranslateTask.cancel(true);
            }
            else {
                mTranslateTask = new TranslateTask(s.toString(), lang, callback);
                mTranslateTask.execute();
            }
        }
    }

    private void startTimer() {
        if (timer != null) {
            timer.cancel();
        }

        timer = new TimerHistory();
        timer.start();
    }

    private String getLang(Spinner from, Spinner to) {
        final StringBuilder lang = new StringBuilder();

        lang.append(getResources().getStringArray(R.array.languages_values)[from.getSelectedItemPosition()]);
        lang.append("-");
        lang.append(getResources().getStringArray(R.array.languages_values)[to.getSelectedItemPosition()]);

        return lang.toString();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.translate_main, container, false);

        final TextView translateLabel = ((TextView) rootView.findViewById(R.id.translate_text_to));
        final TextView translateLabelFrom = ((TextView) rootView.findViewById(R.id.translate_text_from));
        final Spinner translateFromList = ((Spinner) rootView.findViewById(R.id.translate_from));
        final Spinner translateToList = ((Spinner) rootView.findViewById(R.id.translate_to));
        final FloatingActionButton fab = ((FloatingActionButton) rootView.findViewById(R.id.button_favorite));

        final TranslateCallback callback = new TranslateCallback() {
            @Override
            public void sendTranslate(TrResult tr) {
                if (tr.getCode() == 200) {
                    tr.setOriginalText(translateLabelFrom.getText().toString());
                    translateLabel.setText(tr.getText());

                    String lang = getLang(translateFromList, translateToList);

                    if (((String) translateFromList.getSelectedItem()).compareTo(((String) translateToList.getSelectedItem())) != 0) {
                        if (fieldChanged) {

                            startTranslateTask(translateLabelFrom.toString(), lang.toString(), this);
                            fieldChanged = false;
                        }
                    }
                }

                currentResult = tr;
            }
        };

        ((EditText) rootView.findViewById(R.id.translate_field)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Do nothing
            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                String lang = getLang(translateFromList, translateToList);

                startTimer();
                translateLabelFrom.setText(s);
                startTranslateTask(s.toString(), lang.toString(), callback);
                fieldChanged = true;
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Do nothing
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveResult(TrResult.FAVORITE_TYPE);
            }
        });


        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                startTranslateTask(translateLabelFrom.getText().toString(), getLang(translateFromList, translateToList), callback);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        translateFromList.setOnItemSelectedListener(listener);
        translateToList.setOnItemSelectedListener(listener);

        return rootView;
    }

    public boolean saveResult(String type) {
        if (type == null)
            return false;

        if ((type != TrResult.FAVORITE_TYPE) && (type != TrResult.HISTORY_TYPE))
            return false;

        if ((currentResult != null) && (currentResult.getCode() == 200) && (currentResult.getOriginalText() != null)) {
            List<TrResult> list = TrResult.find(TrResult.class, "original_text = ? and lang = ? and text = ? and type_rec = ?", new String[]{currentResult.getOriginalText(), currentResult.getLang(), currentResult.getText(), type});

            if (list.size() == 0) {
                currentResult.setTypeRec(type);
                currentResult.save();

                adapter.notifyDataSetChanged();
                return true;
            }
        }

        return false;
    }
}
