// Auto generated!! Do not modify.
package com.team900.lib.rosNetworkTablesBridge.messages.std_msgs;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

public class RosTime extends com.team900.lib.rosNetworkTablesBridge.messages.RosMessage {

    private com.team900.lib.rosNetworkTablesBridge.messages.TimePrimitive data = new com.team900.lib.rosNetworkTablesBridge.messages.TimePrimitive();

    @Expose(serialize = false, deserialize = false)
    public final java.lang.String _type = "std_msgs/Time";

    public RosTime() {

    }

    public RosTime(com.team900.lib.rosNetworkTablesBridge.messages.TimePrimitive data) {
        this.data = data;
    }

    public RosTime(JsonObject jsonObj) {
        this.data = new com.team900.lib.rosNetworkTablesBridge.messages.TimePrimitive(jsonObj.get("data").getAsJsonObject());
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.TimePrimitive getData() {
        return this.data;
    }

    public void setData(com.team900.lib.rosNetworkTablesBridge.messages.TimePrimitive data) {
        this.data = data;
    }

    public JsonObject toJSON() {
        return ginst.toJsonTree(this).getAsJsonObject();
    }

    public java.lang.String toString() {
        return ginst.toJson(this);
    }
}
