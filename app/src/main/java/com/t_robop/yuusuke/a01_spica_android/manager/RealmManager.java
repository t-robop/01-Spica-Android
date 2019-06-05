package com.t_robop.yuusuke.a01_spica_android.manager;

import android.content.Context;

import com.t_robop.yuusuke.a01_spica_android.model.ScriptBook;
import com.t_robop.yuusuke.a01_spica_android.model.ScriptModel;
import com.t_robop.yuusuke.a01_spica_android.model.ScriptRealmObject;
import com.t_robop.yuusuke.a01_spica_android.model.ScriptShell;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class RealmManager {

    private Realm realm;

    public RealmManager(Context context) {
        Realm.init(context);
        realm = Realm.getDefaultInstance();
    }

    public void writeBook(String title, ArrayList<ScriptModel> scripts) {
        RealmResults<ScriptBook> r = realm.where(ScriptBook.class)
                .equalTo("title", title)
                .findAll();

        realm.beginTransaction();

        ScriptBook book;

        if (r.size() == 0) {
            book = realm.createObject(ScriptBook.class);
            book.setTtitle(title);
            addBookNum();
        } else {
            book = r.first();
            book.pages.clear();
        }

        for (ScriptModel script : scripts) {
            book.pages.add(writeScript(script));
        }

        realm.commitTransaction();
    }

    private ScriptRealmObject writeScript(ScriptModel script) {
        ScriptRealmObject object = realm.createObject(ScriptRealmObject.class);
        object.from(script);
        return object;
    }

    public ArrayList<ScriptModel> findBook(String title) {
        RealmResults<ScriptBook> r = realm.where(ScriptBook.class)
                .equalTo("title", title)
                .findAll();
        ArrayList<ScriptModel> scripts = new ArrayList<>();
        for (ScriptRealmObject page : r.first().pages) {
            scripts.add(toScript(page));
        }
        return scripts;
    }

    public int sizeBookShelf() {
        RealmQuery<ScriptBook> query = realm.where(ScriptBook.class);
        return query.findAll().size();
    }

    public ArrayList<String> getTitles() {
        RealmResults<ScriptBook> r = realm.where(ScriptBook.class).findAll().sort("title");
        ArrayList<String> titles = new ArrayList<>();
        for (ScriptBook book : r) {
            titles.add(book.title);
        }
        return titles;
    }

    public void deleteBook(String title) {
        RealmResults<ScriptBook> r = realm.where(ScriptBook.class)
                .equalTo("title", title)
                .findAll();

        realm.beginTransaction();

        r.deleteAllFromRealm();

        realm.commitTransaction();
    }

    private void addBookNum() {
        RealmResults<ScriptShell> r = realm.where(ScriptShell.class).findAll();

        if (r.isEmpty()) {
            ScriptShell shell = realm.createObject(ScriptShell.class);
        } else {
            r.first().setAllBookNum(r.first().getAllBookNum() + 1);
        }
    }

    public int getAllBookNum() {
        RealmResults<ScriptShell> r = realm.where(ScriptShell.class).findAll();
        if (r.isEmpty()) {
            return 0;
        } else {
            return r.first().getAllBookNum();
        }
    }

    private ScriptModel toScript(ScriptRealmObject page) {
        ScriptModel script = new ScriptModel();
        script.setId(page.getId());
        script.setBlock(ScriptModel.SpicaBlock.getScriptBlock(Integer.parseInt(page.getId())));
        script.setPos(page.getPos());
        script.setIfState(page.getIfState());
        script.setInLoop(page.isInLoop());
        script.setSpeed(page.getSpeed());
        script.setValue(page.getValue());
        return script;
    }
}
