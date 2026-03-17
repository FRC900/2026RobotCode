// Auto generated!! Do not modify.
package com.team900.lib.rosNetworkTablesBridge.messages.std_msgs;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

public class RosUInt64 extends com.team900.lib.rosNetworkTablesBridge.messages.RosMessage {

    private long data = 0;

    @Expose(serialize = false, deserialize = false)
    public final java.lang.String _type = "std_msgs/UInt64";

    public RosUInt64() {}

    public RosUInt64(long data) {
        this.data = data;
    }

    public RosUInt64(JsonObject jsonObj) {
        this.data = jsonObj.get("data").getAsLong();
    }

    public long getData() {
        return this.data;
    }

    public void setData(long data) {
        this.data = data;
    }

    public JsonObject toJSON() {
        return ginst.toJsonTree(this).getAsJsonObject();
    }

    public java.lang.String toString() {
        return ginst.toJson(this);
    }
}
