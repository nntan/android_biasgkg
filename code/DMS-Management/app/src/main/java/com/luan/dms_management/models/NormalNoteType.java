package com.luan.dms_management.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class NormalNoteType {

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
