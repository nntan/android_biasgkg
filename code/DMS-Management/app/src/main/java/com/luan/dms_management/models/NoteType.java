package com.luan.dms_management.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by luan.nt on 9/6/2017.
 */

public class NoteType extends RealmObject {

    @PrimaryKey
    private String NotesGroup;
    private String NotesGroupName;

    public String getNotesGroup() {
        return NotesGroup;
    }

    public void setNotesGroup(String value) {
        NotesGroup = value;
    }

    public String getNotesGroupName() {
        return NotesGroupName;
    }

    public void setNotesGroupName(String value) {
        NotesGroupName = value;
    }
}
