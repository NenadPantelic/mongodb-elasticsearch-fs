package edu.learning.db.mongodbfs.model;

public enum NodeType {
    FILE,
    DIR;

    public static NodeType fromValue(String value) {
        return value == null ? null : NodeType.valueOf(value);
    }

}
