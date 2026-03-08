// Auto generated!! Do not modify.
package com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

public class Wrench extends com.team900.lib.rosNetworkTablesBridge.messages.RosMessage {

    private com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Vector3 force = new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Vector3();
    private com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Vector3 torque = new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Vector3();

    @Expose(serialize = false, deserialize = false)
    public final java.lang.String _type = "geometry_msgs/Wrench";

    public Wrench() {

    }

    public Wrench(com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Vector3 force, com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Vector3 torque) {
        this.force = force;
        this.torque = torque;
    }

    public Wrench(JsonObject jsonObj) {
        this.force = new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Vector3(jsonObj.get("force").getAsJsonObject());
        this.torque = new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Vector3(jsonObj.get("torque").getAsJsonObject());
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Vector3 getForce() {
        return this.force;
    }
    public com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Vector3 getTorque() {
        return this.torque;
    }

    public void setForce(com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Vector3 force) {
        this.force = force;
    }
    public void setTorque(com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Vector3 torque) {
        this.torque = torque;
    }

    public JsonObject toJSON() {
        return ginst.toJsonTree(this).getAsJsonObject();
    }

    public java.lang.String toString() {
        return ginst.toJson(this);
    }
}
