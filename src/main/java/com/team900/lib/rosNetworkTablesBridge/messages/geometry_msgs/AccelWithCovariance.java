// Auto generated!! Do not modify.
package com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

public class AccelWithCovariance
        extends com.team900.lib.rosNetworkTablesBridge.messages.RosMessage {

    private com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Accel accel =
            new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Accel();
    private java.lang.Double[] covariance =
            new java.lang.Double[] {
                0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                0.0, 0.0
            };

    @Expose(serialize = false, deserialize = false)
    public final java.lang.String _type = "geometry_msgs/AccelWithCovariance";

    public AccelWithCovariance() {}

    public AccelWithCovariance(
            com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Accel accel,
            java.lang.Double[] covariance) {
        this.accel = accel;
        for (int index = 0; index < 36; index++) {
            this.covariance[index] = covariance[index];
        }
    }

    public AccelWithCovariance(JsonObject jsonObj) {
        this.accel =
                new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Accel(
                        jsonObj.get("accel").getAsJsonObject());
        int covariance_element_index = 0;
        for (JsonElement covariance_element : jsonObj.getAsJsonArray("covariance")) {
            this.covariance[covariance_element_index++] = covariance_element.getAsDouble();
        }
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Accel getAccel() {
        return this.accel;
    }

    public java.lang.Double[] getCovariance() {
        return this.covariance;
    }

    public void setAccel(
            com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Accel accel) {
        this.accel = accel;
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
