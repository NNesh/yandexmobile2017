package nesh.myapp.tasks;

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import nesh.myapp.objects.TrResult;
import nesh.myapp.tasks.callbacks.TranslateCallback;

/**
 * Created by nesh on 4/23/17.
 */

public class TranslateTask extends AsyncTask<Object, TrResult, TrResult> {
    private static final String APIKEY = "trnsl.1.1.20170423T202201Z.7dd6cc340790a37c.38d314d3150b1860753fcac9818bc21b7444b235";
    private static final String URL = "https://translate.yandex.net/api/v1.5/tr.json/translate?key={key}&lang={lang}&text={text}";

    private Map<String, String> mMap = null;
    private RestTemplate restTemplate = new RestTemplate();

    private TranslateCallback callback;

    public TranslateTask(String text, String lang, TranslateCallback tc) {
        callback = tc;

        mMap = new HashMap<>();
        mMap.put("key", APIKEY);
        mMap.put("lang", lang);
        mMap.put("text", text);

        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
    }

    @Override
    protected TrResult doInBackground(Object... params) {
        TrResult result = null;

        try {
            result = restTemplate.postForObject(URL, null, TrResult.class, mMap);
            Log.d("RESULT", result.getText());
        }
        catch (Exception ex) {
            //Do nothing
        }

        return result;
    }

    @Override
    protected void onPostExecute(TrResult s) {
        super.onPostExecute(s);
        if (s != null) {
            callback.sendTranslate(s);
        }
        else {
            callback.sendTranslate(new TrResult(0, "", "", ""));
        }
    }
}
