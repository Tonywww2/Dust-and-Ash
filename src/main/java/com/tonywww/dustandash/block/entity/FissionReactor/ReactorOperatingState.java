package com.tonywww.dustandash.block.entity.FissionReactor;

public enum ReactorOperatingState {
    SCANNING(0),
    MALFORMED(1),
    MISSING_INTERFACE(2),
    MISSING_FUEL_CELL(3),
    WAITING_FOR_FUEL(4),
    COOLING_DOWN(5),
    RUNNING(6);

    private final int id;

    ReactorOperatingState(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static ReactorOperatingState byId(int id) {
        for (ReactorOperatingState state : values()) {
            if (state.id == id) {
                return state;
            }
        }
        return SCANNING;
    }
}