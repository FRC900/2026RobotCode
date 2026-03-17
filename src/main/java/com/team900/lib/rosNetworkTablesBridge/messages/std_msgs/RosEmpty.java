// Auto generated!! Do not modify.
package com.team900.lib.rosNetworkTablesBridge.messages.std_msgs;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

public class RosEmpty extends com.team900.lib.rosNetworkTablesBridge.messages.RosMessage {

    @Expose(serialize = false, deserialize = false)
    public final java.lang.String _type = "std_msgs/Empty";

    public RosEmpty() {}

    public RosEmpty(JsonObject jsonObj) {}

    public JsonObject toJSON() {
        return ginst.toJsonTree(this).getAsJsonObject();
    }

    public java.lang.String toString() {
        return ginst.toJson(this);
    }
}
