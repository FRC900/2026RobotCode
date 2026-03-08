// Auto generated!! Do not modify.
package com.team900.lib.rosNetworkTablesBridge.messages.diagnostic_msgs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.Arrays;

public class DiagnosticArray extends com.team900.lib.rosNetworkTablesBridge.messages.RosMessage {

    private com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header = new com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader();
    private ArrayList<com.team900.lib.rosNetworkTablesBridge.messages.diagnostic_msgs.DiagnosticStatus> status = new ArrayList<>();

    @Expose(serialize = false, deserialize = false)
    public final java.lang.String _type = "diagnostic_msgs/DiagnosticArray";

    public DiagnosticArray() {

    }

    public DiagnosticArray(com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header, com.team900.lib.rosNetworkTablesBridge.messages.diagnostic_msgs.DiagnosticStatus[] status) {
        this.header = header;
        this.status = new ArrayList<>(Arrays.asList(status));
    }

    public DiagnosticArray(JsonObject jsonObj) {
        this.header = new com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader(jsonObj.get("header").getAsJsonObject());
        for (JsonElement status_element : jsonObj.getAsJsonArray("status")) {
            this.status.add(new com.team900.lib.rosNetworkTablesBridge.messages.diagnostic_msgs.DiagnosticStatus(status_element.getAsJsonObject()));
        }
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader getHeader() {
        return this.header;
    }
    public ArrayList<com.team900.lib.rosNetworkTablesBridge.messages.diagnostic_msgs.DiagnosticStatus> getStatus() {
        return this.status;
    }

    public void setHeader(com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header) {
        this.header = header;
    }
    public void setStatus(ArrayList<com.team900.lib.rosNetworkTablesBridge.messages.diagnostic_msgs.DiagnosticStatus> status) {
        this.status = status;
    }

    public JsonObject toJSON() {
        return ginst.toJsonTree(this).getAsJsonObject();
    }

    public java.lang.String toString() {
        return ginst.toJson(this);
    }
}
