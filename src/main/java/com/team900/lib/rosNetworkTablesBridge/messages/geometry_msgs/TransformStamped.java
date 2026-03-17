// Auto generated!! Do not modify.
package com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

public class TransformStamped extends com.team900.lib.rosNetworkTablesBridge.messages.RosMessage {

    private com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header =
            new com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader();
    private java.lang.String child_frame_id = "";
    private com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Transform transform =
            new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Transform();

    @Expose(serialize = false, deserialize = false)
    public final java.lang.String _type = "geometry_msgs/TransformStamped";

    public TransformStamped() {}

    public TransformStamped(
            com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header,
            java.lang.String child_frame_id,
            com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Transform transform) {
        this.header = header;
        this.child_frame_id = child_frame_id;
        this.transform = transform;
    }

    public TransformStamped(JsonObject jsonObj) {
        this.header =
                new com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader(
                        jsonObj.get("header").getAsJsonObject());
        this.child_frame_id = jsonObj.get("child_frame_id").getAsString();
        this.transform =
                new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Transform(
                        jsonObj.get("transform").getAsJsonObject());
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader getHeader() {
        return this.header;
    }

    public java.lang.String getChildFrameId() {
        return this.child_frame_id;
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Transform getTransform() {
        return this.transform;
    }

    public void setHeader(
            com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header) {
        this.header = header;
    }

    public void setChildFrameId(java.lang.String child_frame_id) {
        this.child_frame_id = child_frame_id;
    }

    public void setTransform(
            com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Transform transform) {
        this.transform = transform;
    }

    public JsonObject toJSON() {
        return ginst.toJsonTree(this).getAsJsonObject();
    }

    public java.lang.String toString() {
        return ginst.toJson(this);
    }
}
