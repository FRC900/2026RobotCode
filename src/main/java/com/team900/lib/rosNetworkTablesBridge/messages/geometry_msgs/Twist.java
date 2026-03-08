// Auto generated!! Do not modify.
package com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

public class Twist extends com.team900.lib.rosNetworkTablesBridge.messages.RosMessage {

    private com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Vector3 linear = new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Vector3();
    private com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Vector3 angular = new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Vector3();

    @Expose(serialize = false, deserialize = false)
    public final java.lang.String _type = "geometry_msgs/Twist";

    public Twist() {

    }

    public Twist(com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Vector3 linear, com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Vector3 angular) {
        this.linear = linear;
        this.angular = angular;
    }

    public Twist(JsonObject jsonObj) {
        this.linear = new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Vector3(jsonObj.get("linear").getAsJsonObject());
        this.angular = new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Vector3(jsonObj.get("angular").getAsJsonObject());
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Vector3 getLinear() {
        return this.linear;
    }
    public com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Vector3 getAngular() {
        return this.angular;
    }

    public void setLinear(com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Vector3 linear) {
        this.linear = linear;
    }
    public void setAngular(com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Vector3 angular) {
        this.angular = angular;
    }

    public JsonObject toJSON() {
        return ginst.toJsonTree(this).getAsJsonObject();
    }

    public java.lang.String toString() {
        return ginst.toJson(this);
    }
}
