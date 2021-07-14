package com.example.retrofit_ex;

import java.util.ArrayList;

public class CommentInfo {
    private int __v;
    private String name, title, comment, _id;
    public CommentInfo(String name, String title, String comment){
        this.name=name;
        this.title=title;
        this.comment=comment;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void set__v(int __v) {
        this.__v = __v;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public int get__v() {
        return __v;
    }

    public String get_id() {
        return _id;
    }


    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

}