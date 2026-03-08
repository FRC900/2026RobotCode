// Auto generated!! Do not modify.
package com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

public class Pose extends com.team900.lib.rosNetworkTablesBridge.messages.RosMessage {

    private com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Point position = new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Point();
    private com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Quaternion orientation = new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Quaternion();

    @Expose(serialize = false, deserialize = false)
    public final java.lang.String _type = "geometry_msgs/Pose";

    public Pose() {

    }

    public Pose(com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Point position, com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Quaternion orientation) {
        this.position = position;
        this.orientation = orientation;
    }

    public Pose(JsonObject jsonObj) {
        this.position = new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Point(jsonObj.get("position").getAsJsonObject());
        this.orientation = new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Quaternion(jsonObj.get("orientation").getAsJsonObject());
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Point getPosition() {
        return this.position;
    }
    public com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Quaternion getOrientation() {
        return this.orientation;
    }

    public void setPosition(com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Point position) {
        this.position = position;
    }
    public void setOrientation(com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Quaternion orientation) {
        this.orientation = orientation;
    }

    public JsonObject toJSON() {
        return ginst.toJsonTree(this).getAsJsonObject();
    }

    public java.lang.String toString() {
        return ginst.toJson(this);
    }
}
