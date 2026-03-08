// Auto generated!! Do not modify.
package com.team900.lib.rosNetworkTablesBridge.messages.visualization_msgs;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

public class InteractiveMarkerPose
        extends com.team900.lib.rosNetworkTablesBridge.messages.RosMessage {

    private com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header =
            new com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader();
    private com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Pose pose =
            new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Pose();
    private java.lang.String name = "";

    @Expose(serialize = false, deserialize = false)
    public final java.lang.String _type = "visualization_msgs/InteractiveMarkerPose";

    public InteractiveMarkerPose() {}

    public InteractiveMarkerPose(
            com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header,
            com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Pose pose,
            java.lang.String name) {
        this.header = header;
        this.pose = pose;
        this.name = name;
    }

    public InteractiveMarkerPose(JsonObject jsonObj) {
        this.header =
                new com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader(
                        jsonObj.get("header").getAsJsonObject());
        this.pose =
                new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Pose(
                        jsonObj.get("pose").getAsJsonObject());
        this.name = jsonObj.get("name").getAsString();
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader getHeader() {
        return this.header;
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Pose getPose() {
        return this.pose;
    }

    public java.lang.String getName() {
        return this.name;
    }

    public void setHeader(
            com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header) {
        this.header = header;
    }

    public void setPose(com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Pose pose) {
        this.pose = pose;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public JsonObject toJSON() {
        return ginst.toJsonTree(this).getAsJsonObject();
    }

    public java.lang.String toString() {
        return ginst.toJson(this);
    }
}
