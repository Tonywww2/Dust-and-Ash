package com.tonywww.dustandash.block.entity.FissionReactor;

public enum ReactorStructureIssue {
    NONE(0),
    RADIUS_ANCHOR_MISSING(1),
    HEIGHT_ANCHOR_MISSING(2),
    INVALID_CASING(3),
    INVALID_WALL(4);

    private final int id;

    ReactorStructureIssue(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static ReactorStructureIssue byId(int id) {
        for (ReactorStructureIssue issue : values()) {
            if (issue.id == id) {
                return issue;
            }
        }
        return NONE;
    }
}