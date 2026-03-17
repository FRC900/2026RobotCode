// Auto generated!! Do not modify.
package com.team900.lib.rosNetworkTablesBridge.messages.tf2_msgs;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

public class LookupTransformResult
        extends com.team900.lib.rosNetworkTablesBridge.messages.RosMessage {

    private com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.TransformStamped
            transform =
                    new com.team900
                            .lib
                            .rosNetworkTablesBridge
                            .messages
                            .geometry_msgs
                            .TransformStamped();
    private com.team900.lib.rosNetworkTablesBridge.messages.tf2_msgs.TF2Error error =
            new com.team900.lib.rosNetworkTablesBridge.messages.tf2_msgs.TF2Error();

    @Expose(serialize = false, deserialize = false)
    public final java.lang.String _type = "tf2_msgs/LookupTransformResult";

    public LookupTransformResult() {}

    public LookupTransformResult(
            com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.TransformStamped
                    transform,
            com.team900.lib.rosNetworkTablesBridge.messages.tf2_msgs.TF2Error error) {
        this.transform = transform;
        this.error = error;
    }

    public LookupTransformResult(JsonObject jsonObj) {
        this.transform =
                new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.TransformStamped(
                        jsonObj.get("transform").getAsJsonObject());
        this.error =
                new com.team900.lib.rosNetworkTablesBridge.messages.tf2_msgs.TF2Error(
                        jsonObj.get("error").getAsJsonObject());
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.TransformStamped
            getTransform() {
        return this.transform;
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.tf2_msgs.TF2Error getError() {
        return this.error;
    }

    public void setTransform(
            com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.TransformStamped
                    transform) {
        this.transform = transform;
    }

    public void setError(com.team900.lib.rosNetworkTablesBridge.messages.tf2_msgs.TF2Error error) {
        this.error = error;
    }

    public JsonObject toJSON() {
        return ginst.toJsonTree(this).getAsJsonObject();
    }

    public java.lang.String toString() {
        return ginst.toJson(this);
    }
}
