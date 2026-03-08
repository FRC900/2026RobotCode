// Auto generated!! Do not modify.
package com.team900.frc2026.subsystems.coprocessor.messages.apriltag_msgs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.Arrays;

public class RawFiducialArrayStamped
        extends com.team900.lib.rosNetworkTablesBridge.messages.RosMessage {

    private com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header =
            new com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader();
    private ArrayList<com.team900.frc2026.subsystems.coprocessor.messages.apriltag_msgs.RawFiducial>
            rawFiducials = new ArrayList<>();

    @Expose(serialize = false, deserialize = false)
    public final java.lang.String _type = "apriltag_msgs/RawFiducialArrayStamped";

    public RawFiducialArrayStamped() {}

    public RawFiducialArrayStamped(
            com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header,
            com.team900.frc2026.subsystems.coprocessor.messages.apriltag_msgs.RawFiducial[]
                    rawFiducials) {
        this.header = header;
        this.rawFiducials = new ArrayList<>(Arrays.asList(rawFiducials));
    }

    public RawFiducialArrayStamped(JsonObject jsonObj) {
        this.header =
                new com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader(
                        jsonObj.get("header").getAsJsonObject());
        for (JsonElement rawFiducials_element : jsonObj.getAsJsonArray("rawFiducials")) {
            this.rawFiducials.add(
                    new com.team900.frc2026.subsystems.coprocessor.messages.apriltag_msgs
                            .RawFiducial(rawFiducials_element.getAsJsonObject()));
        }
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader getHeader() {
        return this.header;
    }

    public
ArrayList<com.team900.frc2026.subsystems.coprocessor.messages.apriltag_msgs.RawFiducial>
            getRawFiducials() {
        return this.rawFiducials;
    }

    public void setHeader(
            com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header) {
        this.header = header;
    }

    public void setRawFiducials(

ArrayList<com.team900.frc2026.subsystems.coprocessor.messages.apriltag_msgs.RawFiducial>
                    rawFiducials) {
        this.rawFiducials = rawFiducials;
    }

    public JsonObject toJSON() {
        return ginst.toJsonTree(this).getAsJsonObject();
    }

    public java.lang.String toString() {
        return ginst.toJson(this);
    }
}
