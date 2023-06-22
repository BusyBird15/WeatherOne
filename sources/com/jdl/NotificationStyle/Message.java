package com.jdl.NotificationStyle;

public class Message {
    private CharSequence sender;
    private CharSequence text;
    private long timestamp;

    public Message(CharSequence charSequence, CharSequence charSequence2, long j) {
        this.text = charSequence;
        this.sender = charSequence2;
        this.timestamp = j;
    }

    public CharSequence getSender() {
        return this.sender;
    }

    public CharSequence getText() {
        return this.text;
    }

    public long getTimestamp() {
        return this.timestamp;
    }
}
