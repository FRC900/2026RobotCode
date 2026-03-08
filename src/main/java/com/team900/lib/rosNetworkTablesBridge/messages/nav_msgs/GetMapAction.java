// Auto generated!! Do not modify.
package com.team900.lib.rosNetworkTablesBridge.messages.nav_msgs;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

public class GetMapAction extends com.team900.lib.rosNetworkTablesBridge.messages.RosMessage {

    private com.team900.lib.rosNetworkTablesBridge.messages.nav_msgs.GetMapActionGoal action_goal =
            new com.team900.lib.rosNetworkTablesBridge.messages.nav_msgs.GetMapActionGoal();
    private com.team900.lib.rosNetworkTablesBridge.messages.nav_msgs.GetMapActionResult
            action_result =
                    new com.team900
                            .lib
                            .rosNetworkTablesBridge
                            .messages
                            .nav_msgs
                            .GetMapActionResult();
    private com.team900.lib.rosNetworkTablesBridge.messages.nav_msgs.GetMapActionFeedback
            action_feedback =
                    new com.team900
                            .lib
                            .rosNetworkTablesBridge
                            .messages
                            .nav_msgs
                            .GetMapActionFeedback();

    @Expose(serialize = false, deserialize = false)
    public final java.lang.String _type = "nav_msgs/GetMapAction";

    public GetMapAction() {}

    public GetMapAction(
            com.team900.lib.rosNetworkTablesBridge.messages.nav_msgs.GetMapActionGoal action_goal,
            com.team900.lib.rosNetworkTablesBridge.messages.nav_msgs.GetMapActionResult
                    action_result,
            com.team900.lib.rosNetworkTablesBridge.messages.nav_msgs.GetMapActionFeedback
                    action_feedback) {
        this.action_goal = action_goal;
        this.action_result = action_result;
        this.action_feedback = action_feedback;
    }

    public GetMapAction(JsonObject jsonObj) {
        this.action_goal =
                new com.team900.lib.rosNetworkTablesBridge.messages.nav_msgs.GetMapActionGoal(
                        jsonObj.get("action_goal").getAsJsonObject());
        this.action_result =
                new com.team900.lib.rosNetworkTablesBridge.messages.nav_msgs.GetMapActionResult(
                        jsonObj.get("action_result").getAsJsonObject());
        this.action_feedback =
                new com.team900.lib.rosNetworkTablesBridge.messages.nav_msgs.GetMapActionFeedback(
                        jsonObj.get("action_feedback").getAsJsonObject());
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.nav_msgs.GetMapActionGoal
            getActionGoal() {
        return this.action_goal;
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.nav_msgs.GetMapActionResult
            getActionResult() {
        return this.action_result;
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.nav_msgs.GetMapActionFeedback
            getActionFeedback() {
        return this.action_feedback;
    }

    public void setActionGoal(
            com.team900.lib.rosNetworkTablesBridge.messages.nav_msgs.GetMapActionGoal action_goal) {
        this.action_goal = action_goal;
    }

    public void setActionResult(
            com.team900.lib.rosNetworkTablesBridge.messages.nav_msgs.GetMapActionResult
                    action_result) {
        this.action_result = action_result;
    }

    public void setActionFeedback(
            com.team900.lib.rosNetworkTablesBridge.messages.nav_msgs.GetMapActionFeedback
                    action_feedback) {
        this.action_feedback = action_feedback;
    }

    public JsonObject toJSON() {
        return ginst.toJsonTree(this).getAsJsonObject();
    }

    public java.lang.String toString() {
        return ginst.toJson(this);
    }
}
