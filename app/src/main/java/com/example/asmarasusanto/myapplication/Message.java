package com.example.asmarasusanto.myapplication;

import java.util.Date;

/**
 * Created by asmarasusanto on 1/16/17.
 */

public class Message {
    private String mText;
    private String mSender;

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public String getSender() {
        return mSender;
    }

    public void setSender(String sender) {
        mSender = sender;
    }
}