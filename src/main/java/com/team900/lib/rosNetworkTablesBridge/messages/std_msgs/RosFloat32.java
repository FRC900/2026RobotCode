// Auto generated!! Do not modify.
package com.team900.lib.rosNetworkTablesBridge.messages.std_msgs;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

public class RosFloat32 extends com.team900.lib.rosNetworkTablesBridge.messages.RosMessage {

    private float data = 0.0f;

    @Expose(serialize = false, deserialize = false)
    public final java.lang.String _type = "std_msgs/Float32";

    public RosFloat32() {}

    public RosFloat32(float data) {
        this.data = data;
    }

    public RosFloat32(JsonObject jsonObj) {
        this.data = jsonObj.get("data").getAsFloat();
    }

    public float getData() {
        return this.data;
    }

    public void setData(float data) {
        this.data = data;
    }

    public JsonObject toJSON() {
        return ginst.toJsonTree(this).getAsJsonObject();
    }

    public java.lang.String toString() {
        return ginst.toJson(this);
    }
}
