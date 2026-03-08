// Auto generated!! Do not modify.
package com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

public class WrenchStamped extends com.team900.lib.rosNetworkTablesBridge.messages.RosMessage {

    private com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header = new com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader();
    private com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Wrench wrench = new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Wrench();

    @Expose(serialize = false, deserialize = false)
    public final java.lang.String _type = "geometry_msgs/WrenchStamped";

    public WrenchStamped() {

    }

    public WrenchStamped(com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header, com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Wrench wrench) {
        this.header = header;
        this.wrench = wrench;
    }

    public WrenchStamped(JsonObject jsonObj) {
        this.header = new com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader(jsonObj.get("header").getAsJsonObject());
        this.wrench = new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Wrench(jsonObj.get("wrench").getAsJsonObject());
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader getHeader() {
        return this.header;
    }
    public com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Wrench getWrench() {
        return this.wrench;
    }

    public void setHeader(com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header) {
        this.header = header;
    }
    public void setWrench(com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Wrench wrench) {
        this.wrench = wrench;
    }

    public JsonObject toJSON() {
        return ginst.toJsonTree(this).getAsJsonObject();
    }

    public java.lang.String toString() {
        return ginst.toJson(this);
    }
}
