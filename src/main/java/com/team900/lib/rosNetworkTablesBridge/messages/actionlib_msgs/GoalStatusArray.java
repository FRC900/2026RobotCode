// Auto generated!! Do not modify.
package com.team900.lib.rosNetworkTablesBridge.messages.actionlib_msgs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.Arrays;

public class GoalStatusArray extends com.team900.lib.rosNetworkTablesBridge.messages.RosMessage {

    private com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header =
            new com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader();
    private ArrayList<com.team900.lib.rosNetworkTablesBridge.messages.actionlib_msgs.GoalStatus>
            status_list = new ArrayList<>();

    @Expose(serialize = false, deserialize = false)
    public final java.lang.String _type = "actionlib_msgs/GoalStatusArray";

    public GoalStatusArray() {}

    public GoalStatusArray(
            com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header,
            com.team900.lib.rosNetworkTablesBridge.messages.actionlib_msgs.GoalStatus[]
                    status_list) {
        this.header = header;
        this.status_list = new ArrayList<>(Arrays.asList(status_list));
    }

    public GoalStatusArray(JsonObject jsonObj) {
        this.header =
                new com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader(
                        jsonObj.get("header").getAsJsonObject());
        for (JsonElement status_list_element : jsonObj.getAsJsonArray("status_list")) {
            this.status_list.add(
                    new com.team900.lib.rosNetworkTablesBridge.messages.actionlib_msgs.GoalStatus(
                            status_list_element.getAsJsonObject()));
        }
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader getHeader() {
        return this.header;
    }

    public ArrayList<com.team900.lib.rosNetworkTablesBridge.messages.actionlib_msgs.GoalStatus>
            getStatusList() {
        return this.status_list;
    }

    public void setHeader(
            com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header) {
        this.header = header;
    }

    public void setStatusList(
            ArrayList<com.team900.lib.rosNetworkTablesBridge.messages.actionlib_msgs.GoalStatus>
                    status_list) {
        this.status_list = status_list;
    }

    public JsonObject toJSON() {
        return ginst.toJsonTree(this).getAsJsonObject();
    }

    public java.lang.String toString() {
        return ginst.toJson(this);
    }
}
