package nesh.myapp.objects;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nesh on 4/24/17.
 */

public class TrResult extends SugarRecord {
    @Ignore
    public static final String FAVORITE_TYPE = "favorite";
    @Ignore
    public static final String HISTORY_TYPE = "history";

    private int code;
    private String lang;
    private String text;
    private String originalText;
    private String typeRec;

    public TrResult() {
        this.typeRec = HISTORY_TYPE;
    }

    public TrResult(int code, String originalText, String text, String lang) {
        this.typeRec = HISTORY_TYPE;

        this.code = code;
        this.originalText = originalText;
        this.text = text;
        this.lang = lang;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getText() {
        return text;
    }

    public void setText(List<String> text) {
        this.text = text.get(0);
    }

    public String getOriginalText() {
        return originalText;
    }

    public void setOriginalText(String originalText) {
        this.originalText = originalText;
    }

    public String getTypeRec() {
        return typeRec;
    }

    public void setTypeRec(String typeRec) {
        this.typeRec = typeRec;
    }
}
