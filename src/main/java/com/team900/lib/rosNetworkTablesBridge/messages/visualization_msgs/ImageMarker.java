// Auto generated!! Do not modify.
package com.team900.lib.rosNetworkTablesBridge.messages.visualization_msgs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.Arrays;

public class ImageMarker extends com.team900.lib.rosNetworkTablesBridge.messages.RosMessage {
    public static int CIRCLE = 0;
    public static int LINE_STRIP = 1;
    public static int LINE_LIST = 2;
    public static int POLYGON = 3;
    public static int POINTS = 4;
    public static int ADD = 0;
    public static int REMOVE = 1;

    private com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header =
            new com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader();
    private java.lang.String ns = "";
    private int id = 0;
    private int type = 0;
    private int action = 0;
    private com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Point position =
            new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Point();
    private float scale = 0.0f;
    private com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosColorRGBA outline_color =
            new com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosColorRGBA();
    private byte filled = 0;
    private com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosColorRGBA fill_color =
            new com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosColorRGBA();
    private com.team900.lib.rosNetworkTablesBridge.messages.DurationPrimitive lifetime =
            new com.team900.lib.rosNetworkTablesBridge.messages.DurationPrimitive();
    private ArrayList<com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Point> points =
            new ArrayList<>();
    private ArrayList<com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosColorRGBA>
            outline_colors = new ArrayList<>();

    @Expose(serialize = false, deserialize = false)
    public final java.lang.String _type = "visualization_msgs/ImageMarker";

    public ImageMarker() {}

    public ImageMarker(
            com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header,
            java.lang.String ns,
            int id,
            int type,
            int action,
            com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Point position,
            float scale,
            com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosColorRGBA outline_color,
            byte filled,
            com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosColorRGBA fill_color,
            com.team900.lib.rosNetworkTablesBridge.messages.DurationPrimitive lifetime,
            com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Point[] points,
            com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosColorRGBA[]
                    outline_colors) {
        this.header = header;
        this.ns = ns;
        this.id = id;
        this.type = type;
        this.action = action;
        this.position = position;
        this.scale = scale;
        this.outline_color = outline_color;
        this.filled = filled;
        this.fill_color = fill_color;
        this.lifetime = lifetime;
        this.points = new ArrayList<>(Arrays.asList(points));
        this.outline_colors = new ArrayList<>(Arrays.asList(outline_colors));
    }

    public ImageMarker(JsonObject jsonObj) {
        this.header =
                new com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader(
                        jsonObj.get("header").getAsJsonObject());
        this.ns = jsonObj.get("ns").getAsString();
        this.id = jsonObj.get("id").getAsInt();
        this.type = jsonObj.get("type").getAsInt();
        this.action = jsonObj.get("action").getAsInt();
        this.position =
                new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Point(
                        jsonObj.get("position").getAsJsonObject());
        this.scale = jsonObj.get("scale").getAsFloat();
        this.outline_color =
                new com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosColorRGBA(
                        jsonObj.get("outline_color").getAsJsonObject());
        this.filled = jsonObj.get("filled").getAsByte();
        this.fill_color =
                new com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosColorRGBA(
                        jsonObj.get("fill_color").getAsJsonObject());
        this.lifetime =
                new com.team900.lib.rosNetworkTablesBridge.messages.DurationPrimitive(
                        jsonObj.get("lifetime").getAsJsonObject());
        for (JsonElement points_element : jsonObj.getAsJsonArray("points")) {
            this.points.add(
                    new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Point(
                            points_element.getAsJsonObject()));
        }
        for (JsonElement outline_colors_element : jsonObj.getAsJsonArray("outline_colors")) {
            this.outline_colors.add(
                    new com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosColorRGBA(
                            outline_colors_element.getAsJsonObject()));
        }
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader getHeader() {
        return this.header;
    }

    public java.lang.String getNs() {
        return this.ns;
    }

    public int getId() {
        return this.id;
    }

    public int getType() {
        return this.type;
    }

    public int getAction() {
        return this.action;
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Point getPosition() {
        return this.position;
    }

    public float getScale() {
        return this.scale;
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosColorRGBA getOutlineColor() {
        return this.outline_color;
    }

    public byte getFilled() {
        return this.filled;
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosColorRGBA getFillColor() {
        return this.fill_color;
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.DurationPrimitive getLifetime() {
        return this.lifetime;
    }

    public ArrayList<com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Point>
            getPoints() {
        return this.points;
    }

    public ArrayList<com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosColorRGBA>
            getOutlineColors() {
        return this.outline_colors;
    }

    public void setHeader(
            com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header) {
        this.header = header;
    }

    public void setNs(java.lang.String ns) {
        this.ns = ns;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public void setPosition(
            com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Point position) {
        this.position = position;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void setOutlineColor(
            com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosColorRGBA outline_color) {
        this.outline_color = outline_color;
    }

    public void setFilled(byte filled) {
        this.filled = filled;
    }

    public void setFillColor(
            com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosColorRGBA fill_color) {
        this.fill_color = fill_color;
    }

    public void setLifetime(
            com.team900.lib.rosNetworkTablesBridge.messages.DurationPrimitive lifetime) {
        this.lifetime = lifetime;
    }

    public void setPoints(
            ArrayList<com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Point> points) {
        this.points = points;
    }

    public void setOutlineColors(
            ArrayList<com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosColorRGBA>
                    outline_colors) {
        this.outline_colors = outline_colors;
    }

    public JsonObject toJSON() {
        return ginst.toJsonTree(this).getAsJsonObject();
    }

    public java.lang.String toString() {
        return ginst.toJson(this);
    }
}
