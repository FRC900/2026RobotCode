// Auto generated!! Do not modify.
package com.team900.lib.rosNetworkTablesBridge.messages.std_msgs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.Arrays;

public class RosInt32MultiArray extends com.team900.lib.rosNetworkTablesBridge.messages.RosMessage {

    private com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosMultiArrayLayout layout = new com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosMultiArrayLayout();
    private ArrayList<java.lang.Integer> data = new ArrayList<>();

    @Expose(serialize = false, deserialize = false)
    public final java.lang.String _type = "std_msgs/Int32MultiArray";

    public RosInt32MultiArray() {

    }

    public RosInt32MultiArray(com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosMultiArrayLayout layout, java.lang.Integer[] data) {
        this.layout = layout;
        this.data = new ArrayList<>(Arrays.asList(data));
    }

    public RosInt32MultiArray(JsonObject jsonObj) {
        this.layout = new com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosMultiArrayLayout(jsonObj.get("layout").getAsJsonObject());
        for (JsonElement data_element : jsonObj.getAsJsonArray("data")) {
            this.data.add(data_element.getAsInt());
        }
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosMultiArrayLayout getLayout() {
        return this.layout;
    }
    public ArrayList<java.lang.Integer> getData() {
        return this.data;
    }

    public void setLayout(com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosMultiArrayLayout layout) {
        this.layout = layout;
    }
    public void setData(ArrayList<java.lang.Integer> data) {
        this.data = data;
    }

    public JsonObject toJSON() {
        return ginst.toJsonTree(this).getAsJsonObject();
    }

    public java.lang.String toString() {
        return ginst.toJson(this);
    }
}
