// Auto generated!! Do not modify.
package com.team900.lib.rosNetworkTablesBridge.messages.nav_msgs;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

public class GetMapActionResult extends com.team900.lib.rosNetworkTablesBridge.messages.RosMessage {

    private com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header =
            new com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader();
    private com.team900.lib.rosNetworkTablesBridge.messages.actionlib_msgs.GoalStatus status =
            new com.team900.lib.rosNetworkTablesBridge.messages.actionlib_msgs.GoalStatus();
    private com.team900.lib.rosNetworkTablesBridge.messages.nav_msgs.GetMapResult result =
            new com.team900.lib.rosNetworkTablesBridge.messages.nav_msgs.GetMapResult();

    @Expose(serialize = false, deserialize = false)
    public final java.lang.String _type = "nav_msgs/GetMapActionResult";

    public GetMapActionResult() {}

    public GetMapActionResult(
            com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header,
            com.team900.lib.rosNetworkTablesBridge.messages.actionlib_msgs.GoalStatus status,
            com.team900.lib.rosNetworkTablesBridge.messages.nav_msgs.GetMapResult result) {
        this.header = header;
        this.status = status;
        this.result = result;
    }

    public GetMapActionResult(JsonObject jsonObj) {
        this.header =
                new com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader(
                        jsonObj.get("header").getAsJsonObject());
        this.status =
                new com.team900.lib.rosNetworkTablesBridge.messages.actionlib_msgs.GoalStatus(
                        jsonObj.get("status").getAsJsonObject());
        this.result =
                new com.team900.lib.rosNetworkTablesBridge.messages.nav_msgs.GetMapResult(
                        jsonObj.get("result").getAsJsonObject());
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader getHeader() {
        return this.header;
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.actionlib_msgs.GoalStatus getStatus() {
        return this.status;
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.nav_msgs.GetMapResult getResult() {
        return this.result;
    }

    public void setHeader(
            com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header) {
        this.header = header;
    }

    public void setStatus(
            com.team900.lib.rosNetworkTablesBridge.messages.actionlib_msgs.GoalStatus status) {
        this.status = status;
    }

    public void setResult(
            com.team900.lib.rosNetworkTablesBridge.messages.nav_msgs.GetMapResult result) {
        this.result = result;
    }

    public JsonObject toJSON() {
        return ginst.toJsonTree(this).getAsJsonObject();
    }

    public java.lang.String toString() {
        return ginst.toJson(this);
    }
}
