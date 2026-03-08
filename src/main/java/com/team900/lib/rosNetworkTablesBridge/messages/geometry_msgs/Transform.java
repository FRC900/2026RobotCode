// Auto generated!! Do not modify.
package com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

public class Transform extends com.team900.lib.rosNetworkTablesBridge.messages.RosMessage {

    private com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Vector3 translation = new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Vector3();
    private com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Quaternion rotation = new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Quaternion();

    @Expose(serialize = false, deserialize = false)
    public final java.lang.String _type = "geometry_msgs/Transform";

    public Transform() {

    }

    public Transform(com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Vector3 translation, com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Quaternion rotation) {
        this.translation = translation;
        this.rotation = rotation;
    }

    public Transform(JsonObject jsonObj) {
        this.translation = new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Vector3(jsonObj.get("translation").getAsJsonObject());
        this.rotation = new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Quaternion(jsonObj.get("rotation").getAsJsonObject());
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Vector3 getTranslation() {
        return this.translation;
    }
    public com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Quaternion getRotation() {
        return this.rotation;
    }

    public void setTranslation(com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Vector3 translation) {
        this.translation = translation;
    }
    public void setRotation(com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Quaternion rotation) {
        this.rotation = rotation;
    }

    public JsonObject toJSON() {
        return ginst.toJsonTree(this).getAsJsonObject();
    }

    public java.lang.String toString() {
        return ginst.toJson(this);
    }
}
