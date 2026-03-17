// Auto generated!! Do not modify.
package com.team900.lib.rosNetworkTablesBridge.messages.tf2_msgs;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

public class LookupTransformAction
        extends com.team900.lib.rosNetworkTablesBridge.messages.RosMessage {

    private com.team900.lib.rosNetworkTablesBridge.messages.tf2_msgs.LookupTransformActionGoal
            action_goal =
                    new com.team900
                            .lib
                            .rosNetworkTablesBridge
                            .messages
                            .tf2_msgs
                            .LookupTransformActionGoal();
    private com.team900.lib.rosNetworkTablesBridge.messages.tf2_msgs.LookupTransformActionResult
            action_result =
                    new com.team900
                            .lib
                            .rosNetworkTablesBridge
                            .messages
                            .tf2_msgs
                            .LookupTransformActionResult();
    private com.team900.lib.rosNetworkTablesBridge.messages.tf2_msgs.LookupTransformActionFeedback
            action_feedback =
                    new com.team900
                            .lib
                            .rosNetworkTablesBridge
                            .messages
                            .tf2_msgs
                            .LookupTransformActionFeedback();

    @Expose(serialize = false, deserialize = false)
    public final java.lang.String _type = "tf2_msgs/LookupTransformAction";

    public LookupTransformAction() {}

    public LookupTransformAction(
            com.team900.lib.rosNetworkTablesBridge.messages.tf2_msgs.LookupTransformActionGoal
                    action_goal,
            com.team900.lib.rosNetworkTablesBridge.messages.tf2_msgs.LookupTransformActionResult
                    action_result,
            com.team900.lib.rosNetworkTablesBridge.messages.tf2_msgs.LookupTransformActionFeedback
                    action_feedback) {
        this.action_goal = action_goal;
        this.action_result = action_result;
        this.action_feedback = action_feedback;
    }

    public LookupTransformAction(JsonObject jsonObj) {
        this.action_goal =
                new com.team900
                        .lib
                        .rosNetworkTablesBridge
                        .messages
                        .tf2_msgs
                        .LookupTransformActionGoal(jsonObj.get("action_goal").getAsJsonObject());
        this.action_result =
                new com.team900
                        .lib
                        .rosNetworkTablesBridge
                        .messages
                        .tf2_msgs
                        .LookupTransformActionResult(
                        jsonObj.get("action_result").getAsJsonObject());
        this.action_feedback =
                new com.team900
                        .lib
                        .rosNetworkTablesBridge
                        .messages
                        .tf2_msgs
                        .LookupTransformActionFeedback(
                        jsonObj.get("action_feedback").getAsJsonObject());
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.tf2_msgs.LookupTransformActionGoal
            getActionGoal() {
        return this.action_goal;
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.tf2_msgs.LookupTransformActionResult
            getActionResult() {
        return this.action_result;
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.tf2_msgs.LookupTransformActionFeedback
            getActionFeedback() {
        return this.action_feedback;
    }

    public void setActionGoal(
            com.team900.lib.rosNetworkTablesBridge.messages.tf2_msgs.LookupTransformActionGoal
                    action_goal) {
        this.action_goal = action_goal;
    }

    public void setActionResult(
            com.team900.lib.rosNetworkTablesBridge.messages.tf2_msgs.LookupTransformActionResult
                    action_result) {
        this.action_result = action_result;
    }

    public void setActionFeedback(
            com.team900.lib.rosNetworkTablesBridge.messages.tf2_msgs.LookupTransformActionFeedback
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
