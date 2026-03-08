// // Auto generated!! Do not modify.
// package com.team900.frc2026.subsystems.coprocessor.messages.apriltag_msgs;

// import com.google.gson.JsonElement;
// import com.google.gson.JsonObject;
// import com.google.gson.annotations.Expose;

public class Apriltag extends com.team900.lib.rosNetworkTablesBridge.messages.RosMessage {

    private int id = 0;
    private java.lang.String family = "";
    private int hamming = 0;
    private int border = 0;
    private int bits = 0;
    private com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Point center =
            new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Point();
    private com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Point[] corners =
            new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Point[] {
                new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Point(),
                new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Point(),
                new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Point(),
                new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Point()
            };

//     @Expose(serialize = false, deserialize = false)
//     public final java.lang.String _type = "apriltag_msgs/Apriltag";

//     public Apriltag() {}

    public Apriltag(
            int id,
            java.lang.String family,
            int hamming,
            int border,
            int bits,
            com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Point center,
            com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Point[] corners) {
        this.id = id;
        this.family = family;
        this.hamming = hamming;
        this.border = border;
        this.bits = bits;
        this.center = center;
        for (int index = 0; index < 4; index++) {
            this.corners[index] = corners[index];
        }
    }

    public Apriltag(JsonObject jsonObj) {
        this.id = jsonObj.get("id").getAsInt();
        this.family = jsonObj.get("family").getAsString();
        this.hamming = jsonObj.get("hamming").getAsInt();
        this.border = jsonObj.get("border").getAsInt();
        this.bits = jsonObj.get("bits").getAsInt();
        this.center =
                new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Point(
                        jsonObj.get("center").getAsJsonObject());
        int corners_element_index = 0;
        for (JsonElement corners_element : jsonObj.getAsJsonArray("corners")) {
            this.corners[corners_element_index++] =
                    new com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Point(
                            corners_element.getAsJsonObject());
        }
    }

//     public int getId() {
//         return this.id;
//     }

//     public java.lang.String getFamily() {
//         return this.family;
//     }

//     public int getHamming() {
//         return this.hamming;
//     }

//     public int getBorder() {
//         return this.border;
//     }

//     public int getBits() {
//         return this.bits;
//     }

    public com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Point getCenter() {
        return this.center;
    }

    public com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Point[] getCorners() {
        return this.corners;
    }

//     public void setId(int id) {
//         this.id = id;
//     }

//     public void setFamily(java.lang.String family) {
//         this.family = family;
//     }

//     public void setHamming(int hamming) {
//         this.hamming = hamming;
//     }

//     public void setBorder(int border) {
//         this.border = border;
//     }

//     public void setBits(int bits) {
//         this.bits = bits;
//     }

    public void setCenter(
            com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Point center) {
        this.center = center;
    }

    public void setCorners(
            com.team900.lib.rosNetworkTablesBridge.messages.geometry_msgs.Point[] corners) {
        this.corners = corners;
    }

//     public JsonObject toJSON() {
//         return ginst.toJsonTree(this).getAsJsonObject();
//     }

//     public java.lang.String toString() {
//         return ginst.toJson(this);
//     }
// }
