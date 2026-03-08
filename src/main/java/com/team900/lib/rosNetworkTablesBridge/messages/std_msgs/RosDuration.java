// Auto generated!! Do not modify.
package com.team900.lib.rosNetworkTablesBridge.messages.std_msgs;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

public class RosDuration extends com.team900.lib.rosNetworkTablesBridge.messages.RosMessage {

    private com.team900.lib.rosNetworkTablesBridge.messages.DurationPrimitive data = new com.team900.lib.rosNetworkTablesBridge.messages.DurationPrimitive();

    @Expose(serialize = false, deserialize = false)
    public final java.lang.String _type = "std_msgs/Duration";

    public RosDuration() {

    }

    public RosDuration(com.team900.lib.rosNetworkTablesBridge.messages.DurationPrimitive data) {
        this.data = data;
    }

    public RosDuration(JsonObject jsonObj) {
        this.data = new com.team900.lib.rosNetworkTablesBridge.messages.DurationPrimitive(jsonObj.get("data").getAsJsonObject());
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.DurationPrimitive getData() {
        return this.data;
    }

    public void setData(com.team900.lib.rosNetworkTablesBridge.messages.DurationPrimitive data) {
        this.data = data;
    }

    public JsonObject toJSON() {
        return ginst.toJsonTree(this).getAsJsonObject();
    }

    public java.lang.String toString() {
        return ginst.toJson(this);
    }
}
