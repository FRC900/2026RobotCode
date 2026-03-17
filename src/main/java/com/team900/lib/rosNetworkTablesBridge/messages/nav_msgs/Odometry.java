// Auto generated!! Do not modify.
package com.team900.lib.rosNetworkTablesBridge.messages.nav_msgs;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

public class Odometry extends com.team900.lib.rosNetworkTablesBridge.messages.RosMessage {

    private com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header =
            new com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader();
    private java.lang.String child_frame_id = "";
    private com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.PoseWithCovariance pose =
            new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.PoseWithCovariance();
    private com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.TwistWithCovariance
            twist =
                    new com.team900
                            .lib
                            .rosNetworkTablesBridge
                            .messages
                            .geometry_msgs
                            .TwistWithCovariance();

    @Expose(serialize = false, deserialize = false)
    public final java.lang.String _type = "nav_msgs/Odometry";

    public Odometry() {}

    public Odometry(
            com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header,
            java.lang.String child_frame_id,
            com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.PoseWithCovariance pose,
            com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.TwistWithCovariance
                    twist) {
        this.header = header;
        this.child_frame_id = child_frame_id;
        this.pose = pose;
        this.twist = twist;
    }

    public Odometry(JsonObject jsonObj) {
        this.header =
                new com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader(
                        jsonObj.get("header").getAsJsonObject());
        this.child_frame_id = jsonObj.get("child_frame_id").getAsString();
        this.pose =
                new com.team900
                        .lib
                        .rosNetworkTablesBridge
                        .messages
                        .geometry_msgs
                        .PoseWithCovariance(jsonObj.get("pose").getAsJsonObject());
        this.twist =
                new com.team900
                        .lib
                        .rosNetworkTablesBridge
                        .messages
                        .geometry_msgs
                        .TwistWithCovariance(jsonObj.get("twist").getAsJsonObject());
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader getHeader() {
        return this.header;
    }

    public java.lang.String getChildFrameId() {
        return this.child_frame_id;
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.PoseWithCovariance
            getPose() {
        return this.pose;
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.TwistWithCovariance
            getTwist() {
        return this.twist;
    }

    public void setHeader(
            com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header) {
        this.header = header;
    }

    public void setChildFrameId(java.lang.String child_frame_id) {
        this.child_frame_id = child_frame_id;
    }

    public void setPose(
            com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.PoseWithCovariance pose) {
        this.pose = pose;
    }

    public void setTwist(
            com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.TwistWithCovariance
                    twist) {
        this.twist = twist;
    }

    public JsonObject toJSON() {
        return ginst.toJsonTree(this).getAsJsonObject();
    }

    public java.lang.String toString() {
        return ginst.toJson(this);
    }
}
