package com.t_robop.yuusuke.a01_spica_android.model;

import io.realm.RealmList;
import io.realm.RealmObject;

public class ScriptBook extends RealmObject {
    public String title = "";
    public RealmList<ScriptRealmObject> pages;

    public String getTitle() {
        return title;
    }

    public void setTtitle(String title) {
        this.title = title;
    }

    public RealmList<ScriptRealmObject> getPages() {
        return pages;
    }

    public void setPage(RealmList<ScriptRealmObject> page) {
        this.pages = page;
    }
}
