package com.prome.simplenote;

import org.litepal.crud.DataSupport;

/**
 * Created by kingme on 2017/11/24.
 */

public class NotePublic extends DataSupport {
    private String name;
    private String content;
    private String date;

    public NotePublic(){}

    public NotePublic(String name,String content,String date){
        this.name=name;
        this.content=content;
        this.date=date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
