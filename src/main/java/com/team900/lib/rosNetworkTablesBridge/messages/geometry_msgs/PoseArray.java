// Auto generated!! Do not modify.
package com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.Arrays;

public class PoseArray extends com.team900.lib.rosNetworkTablesBridge.messages.RosMessage {

    private com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header =
            new com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader();
    private ArrayList<com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Pose> poses =
            new ArrayList<>();

    @Expose(serialize = false, deserialize = false)
    public final java.lang.String _type = "geometry_msgs/PoseArray";

    public PoseArray() {}

    public PoseArray(
            com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header,
            com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Pose[] poses) {
        this.header = header;
        this.poses = new ArrayList<>(Arrays.asList(poses));
    }

    public PoseArray(JsonObject jsonObj) {
        this.header =
                new com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader(
                        jsonObj.get("header").getAsJsonObject());
        for (JsonElement poses_element : jsonObj.getAsJsonArray("poses")) {
            this.poses.add(
                    new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Pose(
                            poses_element.getAsJsonObject()));
        }
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader getHeader() {
        return this.header;
    }

    public ArrayList<com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Pose>
            getPoses() {
        return this.poses;
    }

    public void setHeader(
            com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header) {
        this.header = header;
    }

    public void setPoses(
            ArrayList<com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Pose> poses) {
        this.poses = poses;
    }

    public JsonObject toJSON() {
        return ginst.toJsonTree(this).getAsJsonObject();
    }

    public java.lang.String toString() {
        return ginst.toJson(this);
    }
}
