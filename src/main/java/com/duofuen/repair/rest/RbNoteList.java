package com.duofuen.repair.rest;

import java.util.Date;
import java.util.List;

public class RbNoteList extends BaseResultBody {

    private List<Note> noteList;

    public Note giveOneNote() {
        return new Note();
    }

    public List<Note> getNoteList() {
        return noteList;
    }

    public void setNoteList(List<Note> noteList) {
        this.noteList = noteList;
    }

    public class Note {
        private Integer noteId;
        private String content;
        private String createTime;

        public Integer getNoteId() {
            return noteId;
        }

        public void setNoteId(Integer noteId) {
            this.noteId = noteId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }
}
