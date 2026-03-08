// Auto generated!! Do not modify.
package com.team900.lib.rosNetworkTablesBridge.messages.std_msgs;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

public class RosChar extends com.team900.lib.rosNetworkTablesBridge.messages.RosMessage {

    private char data = 0;

    @Expose(serialize = false, deserialize = false)
    public final java.lang.String _type = "std_msgs/Char";

    public RosChar() {}

    public RosChar(char data) {
        this.data = data;
    }

    public RosChar(JsonObject jsonObj) {
        this.data = (char) jsonObj.get("data").getAsByte();
    }

    public char getData() {
        return this.data;
    }

    public void setData(char data) {
        this.data = data;
    }

    public JsonObject toJSON() {
        return ginst.toJsonTree(this).getAsJsonObject();
    }

    public java.lang.String toString() {
        return ginst.toJson(this);
    }
}
