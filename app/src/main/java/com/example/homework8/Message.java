package com.example.homework8;

import com.shaded.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by revati on 15-04-2016.
 */
public class Message {
    String Time_Stamp,message_text,receiver,sender,id;
    Boolean message_read;

    public Message()
    {
        message_read = false;
    }

    public Message(String time_Stamp, String message_text, String receiver, String sender) {
        Time_Stamp = time_Stamp;
        this.message_text = message_text;
        this.receiver = receiver;
        this.sender = sender;
        this.message_read = false;

    }
    @JsonProperty("Time_Stamp")
    public String getTime_Stamp() {
        return Time_Stamp;
    }
    public void setTime_Stamp(String time_Stamp) {
        Time_Stamp = time_Stamp;
    }
    @JsonProperty("message_text")
    public String getMessage_text() {
        return message_text;
    }
    public void setMessage_text(String message_text) {
        this.message_text = message_text;
    }
    @JsonProperty("receiver")
    public String getReceiver() {
        return receiver;
    }
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
    @JsonProperty("sender")
    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    @JsonProperty("message_read")
     public Boolean getMessage_read() {
        return message_read;
    }
     public void setMessage_read(Boolean message_read) {
        this.message_read = message_read;
    }

}
