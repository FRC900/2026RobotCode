// Auto generated!! Do not modify.
package com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

public class PoseWithCovariance extends com.team900.lib.rosNetworkTablesBridge.messages.RosMessage {

    private com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Pose pose = new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Pose();
    private java.lang.Double[] covariance = new java.lang.Double[] {
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0
    };

    @Expose(serialize = false, deserialize = false)
    public final java.lang.String _type = "geometry_msgs/PoseWithCovariance";

    public PoseWithCovariance() {

    }

    public PoseWithCovariance(com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Pose pose, java.lang.Double[] covariance) {
        this.pose = pose;
        for (int index = 0; index < 36; index++) {
            this.covariance[index] = covariance[index];
        }
    }

    public PoseWithCovariance(JsonObject jsonObj) {
        this.pose = new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Pose(jsonObj.get("pose").getAsJsonObject());
        int covariance_element_index = 0;
        for (JsonElement covariance_element : jsonObj.getAsJsonArray("covariance")) {
            this.covariance[covariance_element_index++] = covariance_element.getAsDouble();
        }
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Pose getPose() {
        return this.pose;
    }
    public java.lang.Double[] getCovariance() {
        return this.covariance;
    }

    public void setPose(com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Pose pose) {
        this.pose = pose;
    }
    public void setCovariance(java.lang.Double[] covariance) {
        this.covariance = covariance;
    }

    public JsonObject toJSON() {
        return ginst.toJsonTree(this).getAsJsonObject();
    }

    public java.lang.String toString() {
        return ginst.toJson(this);
    }
}
