// Auto generated!! Do not modify.
package com.team900.lib.rosNetworkTablesBridge.messages.sensor_msgs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.Arrays;

public class Joy extends com.team900.lib.rosNetworkTablesBridge.messages.RosMessage {

    private com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header =
            new com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader();
    private ArrayList<java.lang.Float> axes = new ArrayList<>();
    private ArrayList<java.lang.Integer> buttons = new ArrayList<>();

    @Expose(serialize = false, deserialize = false)
    public final java.lang.String _type = "sensor_msgs/Joy";

    public Joy() {}

    public Joy(
            com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header,
            java.lang.Float[] axes,
            java.lang.Integer[] buttons) {
        this.header = header;
        this.axes = new ArrayList<>(Arrays.asList(axes));
        this.buttons = new ArrayList<>(Arrays.asList(buttons));
    }

    public Joy(JsonObject jsonObj) {
        this.header =
                new com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader(
                        jsonObj.get("header").getAsJsonObject());
        for (JsonElement axes_element : jsonObj.getAsJsonArray("axes")) {
            this.axes.add(axes_element.getAsFloat());
        }
        for (JsonElement buttons_element : jsonObj.getAsJsonArray("buttons")) {
            this.buttons.add(buttons_element.getAsInt());
        }
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader getHeader() {
        return this.header;
    }

    public ArrayList<java.lang.Float> getAxes() {
        return this.axes;
    }

    public ArrayList<java.lang.Integer> getButtons() {
        return this.buttons;
    }

    public void setHeader(
            com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header) {
        this.header = header;
    }

    public void setAxes(ArrayList<java.lang.Float> axes) {
        this.axes = axes;
    }

    public void setButtons(ArrayList<java.lang.Integer> buttons) {
        this.buttons = buttons;
    }

    public JsonObject toJSON() {
        return ginst.toJsonTree(this).getAsJsonObject();
    }

    public java.lang.String toString() {
        return ginst.toJson(this);
    }
}
