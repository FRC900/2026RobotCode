package com.team900.lib.rosNetworkTablesBridge.messages;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public abstract class RosMessage {
    protected static final Gson ginst = new Gson();

    public RosMessage() {}

    public RosMessage(JsonObject jsonObj) {}

    public abstract JsonObject toJSON();
}
