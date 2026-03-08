// Auto generated!! Do not modify.
package com.team900.lib.rosNetworkTablesBridge.messages.std_msgs;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

public class RosHeader extends com.team900.lib.rosNetworkTablesBridge.messages.RosMessage {

    private int seq = 0;
    private com.team900.lib.rosNetworkTablesBridge.messages.TimePrimitive stamp = new com.team900.lib.rosNetworkTablesBridge.messages.TimePrimitive();
    private java.lang.String frame_id = "";

    @Expose(serialize = false, deserialize = false)
    public final java.lang.String _type = "std_msgs/Header";

    public RosHeader() {

    }

    public RosHeader(int seq, com.team900.lib.rosNetworkTablesBridge.messages.TimePrimitive stamp, java.lang.String frame_id) {
        this.seq = seq;
        this.stamp = stamp;
        this.frame_id = frame_id;
    }

    public RosHeader(JsonObject jsonObj) {
        this.seq = jsonObj.get("seq").getAsInt();
        this.stamp = new com.team900.lib.rosNetworkTablesBridge.messages.TimePrimitive(jsonObj.get("stamp").getAsJsonObject());
        this.frame_id = jsonObj.get("frame_id").getAsString();
    }

    public int getSeq() {
        return this.seq;
    }
    public com.team900.lib.rosNetworkTablesBridge.messages.TimePrimitive getStamp() {
        return this.stamp;
    }
    public java.lang.String getFrameId() {
        return this.frame_id;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }
    public void setStamp(com.team900.lib.rosNetworkTablesBridge.messages.TimePrimitive stamp) {
        this.stamp = stamp;
    }
    public void setFrameId(java.lang.String frame_id) {
        this.frame_id = frame_id;
    }

    public JsonObject toJSON() {
        return ginst.toJsonTree(this).getAsJsonObject();
    }

    public java.lang.String toString() {
        return ginst.toJson(this);
    }
}
