// Auto generated!! Do not modify.
package com.team900.lib.rosNetworkTablesBridge.messages.std_msgs;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

public class RosString extends com.team900.lib.rosNetworkTablesBridge.messages.RosMessage {

    private java.lang.String data = "";

    @Expose(serialize = false, deserialize = false)
    public final java.lang.String _type = "std_msgs/String";

    public RosString() {}

    public RosString(java.lang.String data) {
        this.data = data;
    }

    public RosString(JsonObject jsonObj) {
        this.data = jsonObj.get("data").getAsString();
    }

    public java.lang.String getData() {
        return this.data;
    }

    public void setData(java.lang.String data) {
        this.data = data;
    }

    public JsonObject toJSON() {
        return ginst.toJsonTree(this).getAsJsonObject();
    }

    public java.lang.String toString() {
        return ginst.toJson(this);
    }
}
