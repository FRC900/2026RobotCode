// Auto generated!! Do not modify.
package com.team900.lib.rosNetworkTablesBridge.messages.nav_msgs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.Arrays;

public class GridCells extends com.team900.lib.rosNetworkTablesBridge.messages.RosMessage {

    private com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header =
            new com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader();
    private float cell_width = 0.0f;
    private float cell_height = 0.0f;
    private ArrayList<com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Point> cells =
            new ArrayList<>();

    @Expose(serialize = false, deserialize = false)
    public final java.lang.String _type = "nav_msgs/GridCells";

    public GridCells() {}

    public GridCells(
            com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header,
            float cell_width,
            float cell_height,
            com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Point[] cells) {
        this.header = header;
        this.cell_width = cell_width;
        this.cell_height = cell_height;
        this.cells = new ArrayList<>(Arrays.asList(cells));
    }

    public GridCells(JsonObject jsonObj) {
        this.header =
                new com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader(
                        jsonObj.get("header").getAsJsonObject());
        this.cell_width = jsonObj.get("cell_width").getAsFloat();
        this.cell_height = jsonObj.get("cell_height").getAsFloat();
        for (JsonElement cells_element : jsonObj.getAsJsonArray("cells")) {
            this.cells.add(
                    new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Point(
                            cells_element.getAsJsonObject()));
        }
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader getHeader() {
        return this.header;
    }

    public float getCellWidth() {
        return this.cell_width;
    }

    public float getCellHeight() {
        return this.cell_height;
    }

    public ArrayList<com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Point>
            getCells() {
        return this.cells;
    }

    public void setHeader(
            com.team900.lib.rosNetworkTablesBridge.messages.std_msgs.RosHeader header) {
        this.header = header;
    }

    public void setCellWidth(float cell_width) {
        this.cell_width = cell_width;
    }

    public void setCellHeight(float cell_height) {
        this.cell_height = cell_height;
    }

    public void setCells(
            ArrayList<com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Point> cells) {
        this.cells = cells;
    }

    public JsonObject toJSON() {
        return ginst.toJsonTree(this).getAsJsonObject();
    }

    public java.lang.String toString() {
        return ginst.toJson(this);
    }
}
