// Auto generated!! Do not modify.
package com.team900.lib.rosNetworkTablesBridge.messages.nav_msgs;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

public class GetMapActionGoal extends com.team900.lib.rosNetworkTablesBridge.messages.RosMessage {

    private com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header =
            new com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader();
    private com.team900.lib.rosNetworkTablesBridge.messages.actionlib_msgs.GoalID goal_id =
            new com.team900.lib.rosNetworkTablesBridge.messages.actionlib_msgs.GoalID();
    private com.team900.lib.rosNetworkTablesBridge.messages.nav_msgs.GetMapGoal goal =
            new com.team900.lib.rosNetworkTablesBridge.messages.nav_msgs.GetMapGoal();

    @Expose(serialize = false, deserialize = false)
    public final java.lang.String _type = "nav_msgs/GetMapActionGoal";

    public GetMapActionGoal() {}

    public GetMapActionGoal(
            com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header,
            com.team900.lib.rosNetworkTablesBridge.messages.actionlib_msgs.GoalID goal_id,
            com.team900.lib.rosNetworkTablesBridge.messages.nav_msgs.GetMapGoal goal) {
        this.header = header;
        this.goal_id = goal_id;
        this.goal = goal;
    }

    public GetMapActionGoal(JsonObject jsonObj) {
        this.header =
                new com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader(
                        jsonObj.get("header").getAsJsonObject());
        this.goal_id =
                new com.team900.lib.rosNetworkTablesBridge.messages.actionlib_msgs.GoalID(
                        jsonObj.get("goal_id").getAsJsonObject());
        this.goal =
                new com.team900.lib.rosNetworkTablesBridge.messages.nav_msgs.GetMapGoal(
                        jsonObj.get("goal").getAsJsonObject());
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader getHeader() {
        return this.header;
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.actionlib_msgs.GoalID getGoalId() {
        return this.goal_id;
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.nav_msgs.GetMapGoal getGoal() {
        return this.goal;
    }

    public void setHeader(
            com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header) {
        this.header = header;
    }

    public void setGoalId(
            com.team900.lib.rosNetworkTablesBridge.messages.actionlib_msgs.GoalID goal_id) {
        this.goal_id = goal_id;
    }

    public void setGoal(com.team900.lib.rosNetworkTablesBridge.messages.nav_msgs.GetMapGoal goal) {
        this.goal = goal;
    }

    public JsonObject toJSON() {
        return ginst.toJsonTree(this).getAsJsonObject();
    }

    public java.lang.String toString() {
        return ginst.toJson(this);
    }
}
