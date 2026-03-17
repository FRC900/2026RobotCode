// Auto generated!! Do not modify.
package com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

public class PolygonStamped extends com.team900.lib.rosNetworkTablesBridge.messages.RosMessage {

    private com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header =
            new com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader();
    private com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Polygon polygon =
            new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Polygon();

    @Expose(serialize = false, deserialize = false)
    public final java.lang.String _type = "geometry_msgs/PolygonStamped";

    public PolygonStamped() {}

    public PolygonStamped(
            com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header,
            com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Polygon polygon) {
        this.header = header;
        this.polygon = polygon;
    }

    public PolygonStamped(JsonObject jsonObj) {
        this.header =
                new com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader(
                        jsonObj.get("header").getAsJsonObject());
        this.polygon =
                new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Polygon(
                        jsonObj.get("polygon").getAsJsonObject());
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader getHeader() {
        return this.header;
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Polygon getPolygon() {
        return this.polygon;
    }

    public void setHeader(
            com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header) {
        this.header = header;
    }

    public void setPolygon(
            com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Polygon polygon) {
        this.polygon = polygon;
    }

    public JsonObject toJSON() {
        return ginst.toJsonTree(this).getAsJsonObject();
    }

    public java.lang.String toString() {
        return ginst.toJson(this);
    }
}
