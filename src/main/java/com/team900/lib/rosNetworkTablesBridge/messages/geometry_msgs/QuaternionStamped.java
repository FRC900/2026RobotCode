// Auto generated!! Do not modify.
package com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

public class QuaternionStamped extends com.team900.lib.rosNetworkTablesBridge.messages.RosMessage {

    private com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header =
            new com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader();
    private com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Quaternion quaternion =
            new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Quaternion();

    @Expose(serialize = false, deserialize = false)
    public final java.lang.String _type = "geometry_msgs/QuaternionStamped";

    public QuaternionStamped() {}

    public QuaternionStamped(
            com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header,
            com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Quaternion quaternion) {
        this.header = header;
        this.quaternion = quaternion;
    }

    public QuaternionStamped(JsonObject jsonObj) {
        this.header =
                new com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader(
                        jsonObj.get("header").getAsJsonObject());
        this.quaternion =
                new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Quaternion(
                        jsonObj.get("quaternion").getAsJsonObject());
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader getHeader() {
        return this.header;
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Quaternion
            getQuaternion() {
        return this.quaternion;
    }

    public void setHeader(
            com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header) {
        this.header = header;
    }

    public void setQuaternion(
            com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Quaternion quaternion) {
        this.quaternion = quaternion;
    }

    public JsonObject toJSON() {
        return ginst.toJsonTree(this).getAsJsonObject();
    }

    public java.lang.String toString() {
        return ginst.toJson(this);
    }
}
