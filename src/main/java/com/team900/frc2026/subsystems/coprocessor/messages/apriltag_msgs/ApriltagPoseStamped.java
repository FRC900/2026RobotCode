// Auto generated!! Do not modify.
package com.team900.frc2026.subsystems.coprocessor.messages.apriltag_msgs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.Arrays;

public class ApriltagPoseStamped extends frc.team88.ros.messages.RosMessage {

    private frc.team88.ros.messages.std_msgs.RosHeader header =
            new frc.team88.ros.messages.std_msgs.RosHeader();
    private ArrayList<com.team900.frc2026.subsystems.coprocessor.messages.apriltag_msgs.Apriltag>
            apriltags = new ArrayList<>();
    private frc.team88.ros.messages.geometry_msgs.PoseArray posearray =
            new frc.team88.ros.messages.geometry_msgs.PoseArray();

    @Expose(serialize = false, deserialize = false)
    public final java.lang.String _type = "apriltag_msgs/ApriltagPoseStamped";

    public ApriltagPoseStamped() {}

    public ApriltagPoseStamped(
            frc.team88.ros.messages.std_msgs.RosHeader header,
            com.team900.frc2026.subsystems.coprocessor.messages.apriltag_msgs.Apriltag[] apriltags,
            frc.team88.ros.messages.geometry_msgs.PoseArray posearray) {
        this.header = header;
        this.apriltags = new ArrayList<>(Arrays.asList(apriltags));
        this.posearray = posearray;
    }

    public ApriltagPoseStamped(JsonObject jsonObj) {
        this.header =
                new frc.team88.ros.messages.std_msgs.RosHeader(
                        jsonObj.get("header").getAsJsonObject());
        for (JsonElement apriltags_element : jsonObj.getAsJsonArray("apriltags")) {
            this.apriltags.add(
                    new com.team900.frc2026.subsystems.coprocessor.messages.apriltag_msgs.Apriltag(
                            apriltags_element.getAsJsonObject()));
        }
        this.posearray =
                new frc.team88.ros.messages.geometry_msgs.PoseArray(
                        jsonObj.get("posearray").getAsJsonObject());
    }

    public frc.team88.ros.messages.std_msgs.RosHeader getHeader() {
        return this.header;
    }

    public ArrayList<com.team900.frc2026.subsystems.coprocessor.messages.apriltag_msgs.Apriltag>
            getApriltags() {
        return this.apriltags;
    }

    public frc.team88.ros.messages.geometry_msgs.PoseArray getPosearray() {
        return this.posearray;
    }

    public void setHeader(frc.team88.ros.messages.std_msgs.RosHeader header) {
        this.header = header;
    }

    public void setApriltags(
            ArrayList<com.team900.frc2026.subsystems.coprocessor.messages.apriltag_msgs.Apriltag>
                    apriltags) {
        this.apriltags = apriltags;
    }

    public void setPosearray(frc.team88.ros.messages.geometry_msgs.PoseArray posearray) {
        this.posearray = posearray;
    }

    public JsonObject toJSON() {
        return ginst.toJsonTree(this).getAsJsonObject();
    }

    public java.lang.String toString() {
        return ginst.toJson(this);
    }
}
