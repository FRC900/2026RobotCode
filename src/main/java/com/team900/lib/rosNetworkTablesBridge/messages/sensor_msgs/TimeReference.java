// Auto generated!! Do not modify.
package com.team900.lib.rosNetworkTablesBridge.messages.sensor_msgs;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

public class TimeReference extends com.team900.lib.rosNetworkTablesBridge.messages.RosMessage {

    private com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header = new com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader();
    private com.team900.lib.rosNetworkTablesBridge.messages.TimePrimitive time_ref = new com.team900.lib.rosNetworkTablesBridge.messages.TimePrimitive();
    private java.lang.String source = "";

    @Expose(serialize = false, deserialize = false)
    public final java.lang.String _type = "sensor_msgs/TimeReference";

    public TimeReference() {

    }

    public TimeReference(com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header, com.team900.lib.rosNetworkTablesBridge.messages.TimePrimitive time_ref, java.lang.String source) {
        this.header = header;
        this.time_ref = time_ref;
        this.source = source;
    }

    public TimeReference(JsonObject jsonObj) {
        this.header = new com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader(jsonObj.get("header").getAsJsonObject());
        this.time_ref = new com.team900.lib.rosNetworkTablesBridge.messages.TimePrimitive(jsonObj.get("time_ref").getAsJsonObject());
        this.source = jsonObj.get("source").getAsString();
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader getHeader() {
        return this.header;
    }
    public com.team900.lib.rosNetworkTablesBridge.messages.TimePrimitive getTimeRef() {
        return this.time_ref;
    }
    public java.lang.String getSource() {
        return this.source;
    }

    public void setHeader(com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header) {
        this.header = header;
    }
    public void setTimeRef(com.team900.lib.rosNetworkTablesBridge.messages.TimePrimitive time_ref) {
        this.time_ref = time_ref;
    }
    public void setSource(java.lang.String source) {
        this.source = source;
    }

    public JsonObject toJSON() {
        return ginst.toJsonTree(this).getAsJsonObject();
    }

    public java.lang.String toString() {
        return ginst.toJson(this);
    }
}
