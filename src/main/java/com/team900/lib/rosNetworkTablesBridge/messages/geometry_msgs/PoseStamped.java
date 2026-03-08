// Auto generated!! Do not modify.
package com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

public class PoseStamped extends com.team900.lib.rosNetworkTablesBridge.messages.RosMessage {

    private com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header =
            new com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader();
    private com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Pose pose =
            new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Pose();

    @Expose(serialize = false, deserialize = false)
    public final java.lang.String _type = "geometry_msgs/PoseStamped";

    public PoseStamped() {}

    public PoseStamped(
            com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header,
            com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Pose pose) {
        this.header = header;
        this.pose = pose;
    }

    public PoseStamped(JsonObject jsonObj) {
        this.header =
                new com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader(
                        jsonObj.get("header").getAsJsonObject());
        this.pose =
                new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Pose(
                        jsonObj.get("pose").getAsJsonObject());
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader getHeader() {
        return this.header;
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Pose getPose() {
        return this.pose;
    }

    public void setHeader(
            com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header) {
        this.header = header;
    }

    public void setPose(com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Pose pose) {
        this.pose = pose;
    }

    public JsonObject toJSON() {
        return ginst.toJsonTree(this).getAsJsonObject();
    }

    public java.lang.String toString() {
        return ginst.toJson(this);
    }
}
