// Auto generated!! Do not modify.
package com.team900.lib.rosNetworkTablesBridge.messages.tf2_msgs;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

public class LookupTransformGoal extends com.team900.lib.rosNetworkTablesBridge.messages.RosMessage {

    private java.lang.String target_frame = "";
    private java.lang.String source_frame = "";
    private com.team900.lib.rosNetworkTablesBridge.messages.TimePrimitive source_time = new com.team900.lib.rosNetworkTablesBridge.messages.TimePrimitive();
    private com.team900.lib.rosNetworkTablesBridge.messages.DurationPrimitive timeout = new com.team900.lib.rosNetworkTablesBridge.messages.DurationPrimitive();
    private com.team900.lib.rosNetworkTablesBridge.messages.TimePrimitive target_time = new com.team900.lib.rosNetworkTablesBridge.messages.TimePrimitive();
    private java.lang.String fixed_frame = "";
    private boolean advanced = false;

    @Expose(serialize = false, deserialize = false)
    public final java.lang.String _type = "tf2_msgs/LookupTransformGoal";

    public LookupTransformGoal() {

    }

    public LookupTransformGoal(java.lang.String target_frame, java.lang.String source_frame, com.team900.lib.rosNetworkTablesBridge.messages.TimePrimitive source_time, com.team900.lib.rosNetworkTablesBridge.messages.DurationPrimitive timeout, com.team900.lib.rosNetworkTablesBridge.messages.TimePrimitive target_time, java.lang.String fixed_frame, boolean advanced) {
        this.target_frame = target_frame;
        this.source_frame = source_frame;
        this.source_time = source_time;
        this.timeout = timeout;
        this.target_time = target_time;
        this.fixed_frame = fixed_frame;
        this.advanced = advanced;
    }

    public LookupTransformGoal(JsonObject jsonObj) {
        this.target_frame = jsonObj.get("target_frame").getAsString();
        this.source_frame = jsonObj.get("source_frame").getAsString();
        this.source_time = new com.team900.lib.rosNetworkTablesBridge.messages.TimePrimitive(jsonObj.get("source_time").getAsJsonObject());
        this.timeout = new com.team900.lib.rosNetworkTablesBridge.messages.DurationPrimitive(jsonObj.get("timeout").getAsJsonObject());
        this.target_time = new com.team900.lib.rosNetworkTablesBridge.messages.TimePrimitive(jsonObj.get("target_time").getAsJsonObject());
        this.fixed_frame = jsonObj.get("fixed_frame").getAsString();
        this.advanced = jsonObj.get("advanced").getAsBoolean();
    }

    public java.lang.String getTargetFrame() {
        return this.target_frame;
    }
    public java.lang.String getSourceFrame() {
        return this.source_frame;
    }
    public com.team900.lib.rosNetworkTablesBridge.messages.TimePrimitive getSourceTime() {
        return this.source_time;
    }
    public com.team900.lib.rosNetworkTablesBridge.messages.DurationPrimitive getTimeout() {
        return this.timeout;
    }
    public com.team900.lib.rosNetworkTablesBridge.messages.TimePrimitive getTargetTime() {
        return this.target_time;
    }
    public java.lang.String getFixedFrame() {
        return this.fixed_frame;
    }
    public boolean getAdvanced() {
        return this.advanced;
    }

    public void setTargetFrame(java.lang.String target_frame) {
        this.target_frame = target_frame;
    }
    public void setSourceFrame(java.lang.String source_frame) {
        this.source_frame = source_frame;
    }
    public void setSourceTime(com.team900.lib.rosNetworkTablesBridge.messages.TimePrimitive source_time) {
        this.source_time = source_time;
    }
    public void setTimeout(com.team900.lib.rosNetworkTablesBridge.messages.DurationPrimitive timeout) {
        this.timeout = timeout;
    }
    public void setTargetTime(com.team900.lib.rosNetworkTablesBridge.messages.TimePrimitive target_time) {
        this.target_time = target_time;
    }
    public void setFixedFrame(java.lang.String fixed_frame) {
        this.fixed_frame = fixed_frame;
    }
    public void setAdvanced(boolean advanced) {
        this.advanced = advanced;
    }

    public JsonObject toJSON() {
        return ginst.toJsonTree(this).getAsJsonObject();
    }

    public java.lang.String toString() {
        return ginst.toJson(this);
    }
}
