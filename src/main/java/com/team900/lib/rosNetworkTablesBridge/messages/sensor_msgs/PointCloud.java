// Auto generated!! Do not modify.
package com.team900.lib.rosNetworkTablesBridge.messages.sensor_msgs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.Arrays;

public class PointCloud extends com.team900.lib.rosNetworkTablesBridge.messages.RosMessage {

    private com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header =
            new com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader();
    private ArrayList<com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Point32>
            points = new ArrayList<>();
    private ArrayList<com.team900.lib.rosNetworkTablesBridge.messages.sensor_msgs.ChannelFloat32>
            channels = new ArrayList<>();

    @Expose(serialize = false, deserialize = false)
    public final java.lang.String _type = "sensor_msgs/PointCloud";

    public PointCloud() {}

    public PointCloud(
            com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header,
            com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Point32[] points,
            com.team900.lib.rosNetworkTablesBridge.messages.sensor_msgs.ChannelFloat32[] channels) {
        this.header = header;
        this.points = new ArrayList<>(Arrays.asList(points));
        this.channels = new ArrayList<>(Arrays.asList(channels));
    }

    public PointCloud(JsonObject jsonObj) {
        this.header =
                new com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader(
                        jsonObj.get("header").getAsJsonObject());
        for (JsonElement points_element : jsonObj.getAsJsonArray("points")) {
            this.points.add(
                    new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Point32(
                            points_element.getAsJsonObject()));
        }
        for (JsonElement channels_element : jsonObj.getAsJsonArray("channels")) {
            this.channels.add(
                    new com.team900.lib.rosNetworkTablesBridge.messages.sensor_msgs.ChannelFloat32(
                            channels_element.getAsJsonObject()));
        }
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader getHeader() {
        return this.header;
    }

    public ArrayList<com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Point32>
            getPoints() {
        return this.points;
    }

    public ArrayList<com.team900.lib.rosNetworkTablesBridge.messages.sensor_msgs.ChannelFloat32>
            getChannels() {
        return this.channels;
    }

    public void setHeader(
            com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header) {
        this.header = header;
    }

    public void setPoints(
            ArrayList<com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Point32>
                    points) {
        this.points = points;
    }

    public void setChannels(
            ArrayList<com.team900.lib.rosNetworkTablesBridge.messages.sensor_msgs.ChannelFloat32>
                    channels) {
        this.channels = channels;
    }

    public JsonObject toJSON() {
        return ginst.toJsonTree(this).getAsJsonObject();
    }

    public java.lang.String toString() {
        return ginst.toJson(this);
    }
}
