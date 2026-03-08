// Auto generated!! Do not modify.
package com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

public class Vector3Stamped extends com.team900.lib.rosNetworkTablesBridge.messages.RosMessage {

    private com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header =
            new com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader();
    private com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Vector3 vector =
            new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Vector3();

    @Expose(serialize = false, deserialize = false)
    public final java.lang.String _type = "geometry_msgs/Vector3Stamped";

    public Vector3Stamped() {}

    public Vector3Stamped(
            com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header,
            com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Vector3 vector) {
        this.header = header;
        this.vector = vector;
    }

    public Vector3Stamped(JsonObject jsonObj) {
        this.header =
                new com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader(
                        jsonObj.get("header").getAsJsonObject());
        this.vector =
                new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Vector3(
                        jsonObj.get("vector").getAsJsonObject());
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader getHeader() {
        return this.header;
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Vector3 getVector() {
        return this.vector;
    }

    public void setHeader(
            com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header) {
        this.header = header;
    }

    public void setVector(
            com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Vector3 vector) {
        this.vector = vector;
    }

    public JsonObject toJSON() {
        return ginst.toJsonTree(this).getAsJsonObject();
    }

    public java.lang.String toString() {
        return ginst.toJson(this);
    }
}
