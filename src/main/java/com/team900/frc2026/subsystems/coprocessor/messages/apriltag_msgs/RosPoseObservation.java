package com.team900.frc2026.subsystems.coprocessor.messages.apriltag_msgs;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.team900.lib.rosNetworkTablesBridge.messages.RosMessage;
import com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Pose;

public class RosPoseObservation extends RosMessage {

    private double timestamp = 0.0;
    private Pose pose = new Pose();
    private double ambiguity = 0.0;
    private int tagCount = 0;
    private double averageTagDistance = 0.0;

    @Expose(serialize = false, deserialize = false)
    public final java.lang.String _type = "apriltag_msgs/RosPoseObservation";

    public RosPoseObservation() {}

    public RosPoseObservation(
            double timestamp,
            Pose pose,
            double ambiguity,
            int tagCount,
            double averageTagDistance) {
        this.timestamp = timestamp;
        this.pose = pose;
        this.ambiguity = ambiguity;
        this.tagCount = tagCount;
        this.averageTagDistance = averageTagDistance;
    }

    public RosPoseObservation(JsonObject jsonObj) {
        this.timestamp = jsonObj.get("timestamp").getAsDouble();
        this.pose = new Pose(jsonObj.get("pose").getAsJsonObject());
        this.ambiguity = jsonObj.get("ambiguity").getAsDouble();
        this.tagCount = jsonObj.get("tagCount").getAsInt();
        this.averageTagDistance = jsonObj.get("averageTagDistance").getAsDouble();
    }

    public double getTimestamp() {
        return this.timestamp;
    }

    public Pose getPose() {
        return this.pose;
    }

    public double getAmbiguity() {
        return this.ambiguity;
    }

    public int getTagCount() {
        return this.tagCount;
    }

    public double getAverageTagDistance() {
        return this.averageTagDistance;
    }

    public void setTimestamp(double timestamp) {
        this.timestamp = timestamp;
    }

    public void setPose(Pose pose) {
        this.pose = pose;
    }

    public void setAmbiguity(double ambiguity) {
        this.ambiguity = ambiguity;
    }

    public void setTagCount(int tagCount) {
        this.tagCount = tagCount;
    }

    public void setAverageTagDistance(double averageTagDistance) {
        this.averageTagDistance = averageTagDistance;
    }

    public JsonObject toJSON() {
        return ginst.toJsonTree(this).getAsJsonObject();
    }

    public java.lang.String toString() {
        return ginst.toJson(this);
    }
}
