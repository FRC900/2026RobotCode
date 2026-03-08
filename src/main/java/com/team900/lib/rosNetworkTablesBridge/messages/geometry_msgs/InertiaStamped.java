// Auto generated!! Do not modify.
package com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

public class InertiaStamped extends com.team900.lib.rosNetworkTablesBridge.messages.RosMessage {

    private com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header = new com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader();
    private com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Inertia inertia = new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Inertia();

    @Expose(serialize = false, deserialize = false)
    public final java.lang.String _type = "geometry_msgs/InertiaStamped";

    public InertiaStamped() {

    }

    public InertiaStamped(com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header, com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Inertia inertia) {
        this.header = header;
        this.inertia = inertia;
    }

    public InertiaStamped(JsonObject jsonObj) {
        this.header = new com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader(jsonObj.get("header").getAsJsonObject());
        this.inertia = new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Inertia(jsonObj.get("inertia").getAsJsonObject());
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader getHeader() {
        return this.header;
    }
    public com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Inertia getInertia() {
        return this.inertia;
    }

    public void setHeader(com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header) {
        this.header = header;
    }
    public void setInertia(com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Inertia inertia) {
        this.inertia = inertia;
    }

    public JsonObject toJSON() {
        return ginst.toJsonTree(this).getAsJsonObject();
    }

    public java.lang.String toString() {
        return ginst.toJson(this);
    }
}
