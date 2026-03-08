// Auto generated!! Do not modify.
package com.team900.lib.rosNetworkTablesBridge.messages.trajectory_msgs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.Arrays;

public class MultiDOFJointTrajectoryPoint extends com.team900.lib.rosNetworkTablesBridge.messages.RosMessage {

    private ArrayList<com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Transform> transforms = new ArrayList<>();
    private ArrayList<com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Twist> velocities = new ArrayList<>();
    private ArrayList<com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Twist> accelerations = new ArrayList<>();
    private com.team900.lib.rosNetworkTablesBridge.messages.DurationPrimitive time_from_start = new com.team900.lib.rosNetworkTablesBridge.messages.DurationPrimitive();

    @Expose(serialize = false, deserialize = false)
    public final java.lang.String _type = "trajectory_msgs/MultiDOFJointTrajectoryPoint";

    public MultiDOFJointTrajectoryPoint() {

    }

    public MultiDOFJointTrajectoryPoint(com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Transform[] transforms, com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Twist[] velocities, com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Twist[] accelerations, com.team900.lib.rosNetworkTablesBridge.messages.DurationPrimitive time_from_start) {
        this.transforms = new ArrayList<>(Arrays.asList(transforms));
        this.velocities = new ArrayList<>(Arrays.asList(velocities));
        this.accelerations = new ArrayList<>(Arrays.asList(accelerations));
        this.time_from_start = time_from_start;
    }

    public MultiDOFJointTrajectoryPoint(JsonObject jsonObj) {
        for (JsonElement transforms_element : jsonObj.getAsJsonArray("transforms")) {
            this.transforms.add(new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Transform(transforms_element.getAsJsonObject()));
        }
        for (JsonElement velocities_element : jsonObj.getAsJsonArray("velocities")) {
            this.velocities.add(new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Twist(velocities_element.getAsJsonObject()));
        }
        for (JsonElement accelerations_element : jsonObj.getAsJsonArray("accelerations")) {
            this.accelerations.add(new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Twist(accelerations_element.getAsJsonObject()));
        }
        this.time_from_start = new com.team900.lib.rosNetworkTablesBridge.messages.DurationPrimitive(jsonObj.get("time_from_start").getAsJsonObject());
    }

    public ArrayList<com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Transform> getTransforms() {
        return this.transforms;
    }
    public ArrayList<com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Twist> getVelocities() {
        return this.velocities;
    }
    public ArrayList<com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Twist> getAccelerations() {
        return this.accelerations;
    }
    public com.team900.lib.rosNetworkTablesBridge.messages.DurationPrimitive getTimeFromStart() {
        return this.time_from_start;
    }

    public void setTransforms(ArrayList<com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Transform> transforms) {
        this.transforms = transforms;
    }
    public void setVelocities(ArrayList<com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Twist> velocities) {
        this.velocities = velocities;
    }
    public void setAccelerations(ArrayList<com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Twist> accelerations) {
        this.accelerations = accelerations;
    }
    public void setTimeFromStart(com.team900.lib.rosNetworkTablesBridge.messages.DurationPrimitive time_from_start) {
        this.time_from_start = time_from_start;
    }

    public JsonObject toJSON() {
        return ginst.toJsonTree(this).getAsJsonObject();
    }

    public java.lang.String toString() {
        return ginst.toJson(this);
    }
}
