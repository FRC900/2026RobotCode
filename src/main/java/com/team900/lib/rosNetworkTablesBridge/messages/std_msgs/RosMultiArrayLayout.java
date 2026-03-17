// Auto generated!! Do not modify.
package com.team900.lib.rosNetworkTablesBridge.messages.std_msgs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.Arrays;

public class RosMultiArrayLayout
        extends com.team900.lib.rosNetworkTablesBridge.messages.RosMessage {

    private ArrayList<
                    com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosMultiArrayDimension>
            dim = new ArrayList<>();
    private int data_offset = 0;

    @Expose(serialize = false, deserialize = false)
    public final java.lang.String _type = "std_msgs/MultiArrayLayout";

    public RosMultiArrayLayout() {}

    public RosMultiArrayLayout(
            com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosMultiArrayDimension[] dim,
            int data_offset) {
        this.dim = new ArrayList<>(Arrays.asList(dim));
        this.data_offset = data_offset;
    }

    public RosMultiArrayLayout(JsonObject jsonObj) {
        for (JsonElement dim_element : jsonObj.getAsJsonArray("dim")) {
            this.dim.add(
                    new com.team900
                            .lib
                            .rosNetworkTablesBridge
                            .messages
                            .std_msgs
                            .RosMultiArrayDimension(dim_element.getAsJsonObject()));
        }
        this.data_offset = jsonObj.get("data_offset").getAsInt();
    }

    public ArrayList<
                    com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosMultiArrayDimension>
            getDim() {
        return this.dim;
    }

    public int getDataOffset() {
        return this.data_offset;
    }

    public void setDim(
            ArrayList<
                            com.team900
                                    .lib
                                    .rosNetworkTablesBridge
                                    .messages
                                    .std_msgs
                                    .RosMultiArrayDimension>
                    dim) {
        this.dim = dim;
    }

    public void setDataOffset(int data_offset) {
        this.data_offset = data_offset;
    }

    public JsonObject toJSON() {
        return ginst.toJsonTree(this).getAsJsonObject();
    }

    public java.lang.String toString() {
        return ginst.toJson(this);
    }
}
