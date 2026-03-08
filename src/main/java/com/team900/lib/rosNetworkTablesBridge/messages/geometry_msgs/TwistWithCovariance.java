// Auto generated!! Do not modify.
package com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

public class TwistWithCovariance extends com.team900.lib.rosNetworkTablesBridge.messages.RosMessage {

    private com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Twist twist = new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Twist();
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
    public final java.lang.String _type = "geometry_msgs/TwistWithCovariance";

    public TwistWithCovariance() {

    }

    public TwistWithCovariance(com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Twist twist, java.lang.Double[] covariance) {
        this.twist = twist;
        for (int index = 0; index < 36; index++) {
            this.covariance[index] = covariance[index];
        }
    }

    public TwistWithCovariance(JsonObject jsonObj) {
        this.twist = new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Twist(jsonObj.get("twist").getAsJsonObject());
        int covariance_element_index = 0;
        for (JsonElement covariance_element : jsonObj.getAsJsonArray("covariance")) {
            this.covariance[covariance_element_index++] = covariance_element.getAsDouble();
        }
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Twist getTwist() {
        return this.twist;
    }
    public java.lang.Double[] getCovariance() {
        return this.covariance;
    }

    public void setTwist(com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Twist twist) {
        this.twist = twist;
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
