// Auto generated!! Do not modify.
package com.team900.lib.rosNetworkTablesBridge.messages.nav_msgs;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

public class GetMapActionFeedback
        extends com.team900.lib.rosNetworkTablesBridge.messages.RosMessage {

    private com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header =
            new com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader();
    private com.team900.lib.rosNetworkTablesBridge.messages.actionlib_msgs.GoalStatus status =
            new com.team900.lib.rosNetworkTablesBridge.messages.actionlib_msgs.GoalStatus();
    private com.team900.lib.rosNetworkTablesBridge.messages.nav_msgs.GetMapFeedback feedback =
            new com.team900.lib.rosNetworkTablesBridge.messages.nav_msgs.GetMapFeedback();

    @Expose(serialize = false, deserialize = false)
    public final java.lang.String _type = "nav_msgs/GetMapActionFeedback";

    public GetMapActionFeedback() {}

    public GetMapActionFeedback(
            com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header,
            com.team900.lib.rosNetworkTablesBridge.messages.actionlib_msgs.GoalStatus status,
            com.team900.lib.rosNetworkTablesBridge.messages.nav_msgs.GetMapFeedback feedback) {
        this.header = header;
        this.status = status;
        this.feedback = feedback;
    }

    public GetMapActionFeedback(JsonObject jsonObj) {
        this.header =
                new com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader(
                        jsonObj.get("header").getAsJsonObject());
        this.status =
                new com.team900.lib.rosNetworkTablesBridge.messages.actionlib_msgs.GoalStatus(
                        jsonObj.get("status").getAsJsonObject());
        this.feedback =
                new com.team900.lib.rosNetworkTablesBridge.messages.nav_msgs.GetMapFeedback(
                        jsonObj.get("feedback").getAsJsonObject());
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader getHeader() {
        return this.header;
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.actionlib_msgs.GoalStatus getStatus() {
        return this.status;
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.nav_msgs.GetMapFeedback getFeedback() {
        return this.feedback;
    }

    public void setHeader(
            com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header) {
        this.header = header;
    }

    public void setStatus(
            com.team900.lib.rosNetworkTablesBridge.messages.actionlib_msgs.GoalStatus status) {
        this.status = status;
    }

    public void setFeedback(
            com.team900.lib.rosNetworkTablesBridge.messages.nav_msgs.GetMapFeedback feedback) {
        this.feedback = feedback;
    }

    public JsonObject toJSON() {
        return ginst.toJsonTree(this).getAsJsonObject();
    }

    public java.lang.String toString() {
        return ginst.toJson(this);
    }
}
