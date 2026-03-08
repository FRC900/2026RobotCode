package com.team900.frc2026.subsystems.coprocessor.messages.apriltag_msgs;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Quaternion;

public class RosTargetObservation extends com.team900.lib.rosNetworkTablesBridge.messages.RosMessage {

    private Quaternion rot = new Quaternion();

    @Expose(serialize = false, deserialize = false)
    public final java.lang.String _type = "RosTargetObservation_msgs/RosTargetObservation";

    public RosTargetObservation() {}

    public RosTargetObservation(
            Quaternion rot) {
        this.rot = rot;
    }

    public RosTargetObservation(JsonObject jsonObj) {
        this.rot = new Quaternion(jsonObj.get("rot").getAsJsonObject());
    }

    public Quaternion getrot() {
        return this.rot;
    }

    public void setrot(Quaternion rot) {
        this.rot = rot;
    }

    public JsonObject toJSON() {
        return ginst.toJsonTree(this).getAsJsonObject();
    }

    public java.lang.String toString() {
        return ginst.toJson(this);
    }
}
