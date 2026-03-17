// Auto generated!! Do not modify.
package com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

public class TwistStamped extends com.team900.lib.rosNetworkTablesBridge.messages.RosMessage {

    private com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header =
            new com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader();
    private com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Twist twist =
            new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Twist();

    @Expose(serialize = false, deserialize = false)
    public final java.lang.String _type = "geometry_msgs/TwistStamped";

    public TwistStamped() {}

    public TwistStamped(
            com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header,
            com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Twist twist) {
        this.header = header;
        this.twist = twist;
    }

    public TwistStamped(JsonObject jsonObj) {
        this.header =
                new com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader(
                        jsonObj.get("header").getAsJsonObject());
        this.twist =
                new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Twist(
                        jsonObj.get("twist").getAsJsonObject());
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader getHeader() {
        return this.header;
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Twist getTwist() {
        return this.twist;
    }

    public void setHeader(
            com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header) {
        this.header = header;
    }

    public void setTwist(
            com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Twist twist) {
        this.twist = twist;
    }

    public JsonObject toJSON() {
        return ginst.toJsonTree(this).getAsJsonObject();
    }

    public java.lang.String toString() {
        return ginst.toJson(this);
    }
}
