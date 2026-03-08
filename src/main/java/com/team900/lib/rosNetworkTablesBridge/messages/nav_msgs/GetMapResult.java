// Auto generated!! Do not modify.
package com.team900.lib.rosNetworkTablesBridge.messages.nav_msgs;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

public class GetMapResult extends com.team900.lib.rosNetworkTablesBridge.messages.RosMessage {

    private com.team900.lib.rosNetworkTablesBridge.messages.nav_msgs.OccupancyGrid map =
            new com.team900.lib.rosNetworkTablesBridge.messages.nav_msgs.OccupancyGrid();

    @Expose(serialize = false, deserialize = false)
    public final java.lang.String _type = "nav_msgs/GetMapResult";

    public GetMapResult() {}

    public GetMapResult(
            com.team900.lib.rosNetworkTablesBridge.messages.nav_msgs.OccupancyGrid map) {
        this.map = map;
    }

    public GetMapResult(JsonObject jsonObj) {
        this.map =
                new com.team900.lib.rosNetworkTablesBridge.messages.nav_msgs.OccupancyGrid(
                        jsonObj.get("map").getAsJsonObject());
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.nav_msgs.OccupancyGrid getMap() {
        return this.map;
    }

    public void setMap(com.team900.lib.rosNetworkTablesBridge.messages.nav_msgs.OccupancyGrid map) {
        this.map = map;
    }

    public JsonObject toJSON() {
        return ginst.toJsonTree(this).getAsJsonObject();
    }

    public java.lang.String toString() {
        return ginst.toJson(this);
    }
}
