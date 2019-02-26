package com.t_robop.yuusuke.a01_spica_android.repository;

import android.content.Context;

import com.t_robop.yuusuke.a01_spica_android.manager.RealmManager;
import com.t_robop.yuusuke.a01_spica_android.model.ScriptModel;

import java.util.ArrayList;

public class ScriptRepository {
    private RealmManager realmManager;

    public ScriptRepository(Context context) {
        realmManager = new RealmManager(context);
    }

    public void writeScripts(
            String title,
            ArrayList<ScriptModel> scripts) {
        realmManager.writeBook(title, scripts);
    }

    public ArrayList<ScriptModel> getScripts(String title) {
        return realmManager.findBook(title);
    }

    public int getAllScriptSize(){
        return realmManager.sizeBookShelf();
    }

    public ArrayList<String> getAllScriptTitle(){
        return realmManager.getTitles();
    }
}
