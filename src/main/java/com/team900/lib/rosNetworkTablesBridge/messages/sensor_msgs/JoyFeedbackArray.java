// Auto generated!! Do not modify.
package com.team900.lib.rosNetworkTablesBridge.messages.sensor_msgs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.Arrays;

public class JoyFeedbackArray extends com.team900.lib.rosNetworkTablesBridge.messages.RosMessage {

    private ArrayList<com.team900.lib.rosNetworkTablesBridge.messages.sensor_msgs.JoyFeedback> array = new ArrayList<>();

    @Expose(serialize = false, deserialize = false)
    public final java.lang.String _type = "sensor_msgs/JoyFeedbackArray";

    public JoyFeedbackArray() {

    }

    public JoyFeedbackArray(com.team900.lib.rosNetworkTablesBridge.messages.sensor_msgs.JoyFeedback[] array) {
        this.array = new ArrayList<>(Arrays.asList(array));
    }

    public JoyFeedbackArray(JsonObject jsonObj) {
        for (JsonElement array_element : jsonObj.getAsJsonArray("array")) {
            this.array.add(new com.team900.lib.rosNetworkTablesBridge.messages.sensor_msgs.JoyFeedback(array_element.getAsJsonObject()));
        }
    }

    public ArrayList<com.team900.lib.rosNetworkTablesBridge.messages.sensor_msgs.JoyFeedback> getArray() {
        return this.array;
    }

    public void setArray(ArrayList<com.team900.lib.rosNetworkTablesBridge.messages.sensor_msgs.JoyFeedback> array) {
        this.array = array;
    }

    public JsonObject toJSON() {
        return ginst.toJsonTree(this).getAsJsonObject();
    }

    public java.lang.String toString() {
        return ginst.toJson(this);
    }
}
