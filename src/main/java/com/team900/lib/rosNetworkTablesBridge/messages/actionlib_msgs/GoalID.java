// Auto generated!! Do not modify.
package com.team900.lib.rosNetworkTablesBridge.messages.actionlib_msgs;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

public class GoalID extends com.team900.lib.rosNetworkTablesBridge.messages.RosMessage {

    private com.team900.lib.rosNetworkTablesBridge.messages.TimePrimitive stamp =
            new com.team900.lib.rosNetworkTablesBridge.messages.TimePrimitive();
    private java.lang.String id = "";

    @Expose(serialize = false, deserialize = false)
    public final java.lang.String _type = "actionlib_msgs/GoalID";

    public GoalID() {}

    public GoalID(
            com.team900.lib.rosNetworkTablesBridge.messages.TimePrimitive stamp,
            java.lang.String id) {
        this.stamp = stamp;
        this.id = id;
    }

    public GoalID(JsonObject jsonObj) {
        this.stamp =
                new com.team900.lib.rosNetworkTablesBridge.messages.TimePrimitive(
                        jsonObj.get("stamp").getAsJsonObject());
        this.id = jsonObj.get("id").getAsString();
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.TimePrimitive getStamp() {
        return this.stamp;
    }

    public java.lang.String getId() {
        return this.id;
    }

    public void setStamp(com.team900.lib.rosNetworkTablesBridge.messages.TimePrimitive stamp) {
        this.stamp = stamp;
    }

    public void setId(java.lang.String id) {
        this.id = id;
    }

    public JsonObject toJSON() {
        return ginst.toJsonTree(this).getAsJsonObject();
    }

    public java.lang.String toString() {
        return ginst.toJson(this);
    }
}
