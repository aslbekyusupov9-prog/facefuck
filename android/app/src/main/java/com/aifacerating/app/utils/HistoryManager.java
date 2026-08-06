package com.aifacerating.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HistoryManager {
    private static final String PREF_NAME = "face_rating_history";
    private static final String KEY_HISTORY = "history_list";

    public static void saveHistoryItem(Context context, HistoryItem item) {
        List<HistoryItem> list = getHistory(context);
        list.add(0, item); // Add to the top of the list
        
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        Gson gson = new Gson();
        editor.putString(KEY_HISTORY, gson.toJson(list));
        editor.apply();
    }

    public static List<HistoryItem> getHistory(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = pref.getString(KEY_HISTORY, null);
        if (json == null) return new ArrayList<>();

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<HistoryItem>>() {}.getType();
        List<HistoryItem> list = gson.fromJson(json, type);
        return list != null ? list : new ArrayList<>();
    }
}
