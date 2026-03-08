// Auto generated!! Do not modify.
package com.team900.lib.rosNetworkTablesBridge.messages.visualization_msgs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.Arrays;

public class MarkerArray extends com.team900.lib.rosNetworkTablesBridge.messages.RosMessage {

    private ArrayList<com.team900.lib.rosNetworkTablesBridge.messages.visualization_msgs.Marker> markers = new ArrayList<>();

    @Expose(serialize = false, deserialize = false)
    public final java.lang.String _type = "visualization_msgs/MarkerArray";

    public MarkerArray() {

    }

    public MarkerArray(com.team900.lib.rosNetworkTablesBridge.messages.visualization_msgs.Marker[] markers) {
        this.markers = new ArrayList<>(Arrays.asList(markers));
    }

    public MarkerArray(JsonObject jsonObj) {
        for (JsonElement markers_element : jsonObj.getAsJsonArray("markers")) {
            this.markers.add(new com.team900.lib.rosNetworkTablesBridge.messages.visualization_msgs.Marker(markers_element.getAsJsonObject()));
        }
    }

    public ArrayList<com.team900.lib.rosNetworkTablesBridge.messages.visualization_msgs.Marker> getMarkers() {
        return this.markers;
    }

    public void setMarkers(ArrayList<com.team900.lib.rosNetworkTablesBridge.messages.visualization_msgs.Marker> markers) {
        this.markers = markers;
    }

    public JsonObject toJSON() {
        return ginst.toJsonTree(this).getAsJsonObject();
    }

    public java.lang.String toString() {
        return ginst.toJson(this);
    }
}
