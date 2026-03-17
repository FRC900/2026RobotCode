// Auto generated!! Do not modify.
package com.team900.lib.rosNetworkTablesBridge.messages.sensor_msgs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.Arrays;

public class MultiDOFJointState extends com.team900.lib.rosNetworkTablesBridge.messages.RosMessage {

    private com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header =
            new com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader();
    private ArrayList<java.lang.String> joint_names = new ArrayList<>();
    private ArrayList<com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Transform>
            transforms = new ArrayList<>();
    private ArrayList<com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Twist> twist =
            new ArrayList<>();
    private ArrayList<com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Wrench> wrench =
            new ArrayList<>();

    @Expose(serialize = false, deserialize = false)
    public final java.lang.String _type = "sensor_msgs/MultiDOFJointState";

    public MultiDOFJointState() {}

    public MultiDOFJointState(
            com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header,
            java.lang.String[] joint_names,
            com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Transform[] transforms,
            com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Twist[] twist,
            com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Wrench[] wrench) {
        this.header = header;
        this.joint_names = new ArrayList<>(Arrays.asList(joint_names));
        this.transforms = new ArrayList<>(Arrays.asList(transforms));
        this.twist = new ArrayList<>(Arrays.asList(twist));
        this.wrench = new ArrayList<>(Arrays.asList(wrench));
    }

    public MultiDOFJointState(JsonObject jsonObj) {
        this.header =
                new com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader(
                        jsonObj.get("header").getAsJsonObject());
        for (JsonElement joint_names_element : jsonObj.getAsJsonArray("joint_names")) {
            this.joint_names.add(joint_names_element.getAsString());
        }
        for (JsonElement transforms_element : jsonObj.getAsJsonArray("transforms")) {
            this.transforms.add(
                    new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Transform(
                            transforms_element.getAsJsonObject()));
        }
        for (JsonElement twist_element : jsonObj.getAsJsonArray("twist")) {
            this.twist.add(
                    new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Twist(
                            twist_element.getAsJsonObject()));
        }
        for (JsonElement wrench_element : jsonObj.getAsJsonArray("wrench")) {
            this.wrench.add(
                    new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Wrench(
                            wrench_element.getAsJsonObject()));
        }
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader getHeader() {
        return this.header;
    }

    public ArrayList<java.lang.String> getJointNames() {
        return this.joint_names;
    }

    public ArrayList<com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Transform>
            getTransforms() {
        return this.transforms;
    }

    public ArrayList<com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Twist>
            getTwist() {
        return this.twist;
    }

    public ArrayList<com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Wrench>
            getWrench() {
        return this.wrench;
    }

    public void setHeader(
            com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header) {
        this.header = header;
    }

    public void setJointNames(ArrayList<java.lang.String> joint_names) {
        this.joint_names = joint_names;
    }

    public void setTransforms(
            ArrayList<com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Transform>
                    transforms) {
        this.transforms = transforms;
    }

    public void setTwist(
            ArrayList<com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Twist> twist) {
        this.twist = twist;
    }

    public void setWrench(
            ArrayList<com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Wrench>
                    wrench) {
        this.wrench = wrench;
    }

    public JsonObject toJSON() {
        return ginst.toJsonTree(this).getAsJsonObject();
    }

    public java.lang.String toString() {
        return ginst.toJson(this);
    }
}
