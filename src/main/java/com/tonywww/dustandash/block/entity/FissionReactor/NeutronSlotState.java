package com.tonywww.dustandash.block.entity.FissionReactor;

public enum NeutronSlotState {
    EMPTY(0),
    INVALID_ITEM(1),
    WAITING_FOR_NEUTRONS(2),
    ABSORBING(3),
    FULL(4);

    private final int id;

    NeutronSlotState(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static NeutronSlotState byId(int id) {
        for (NeutronSlotState state : values()) {
            if (state.id == id) {
                return state;
            }
        }
        return EMPTY;
    }
}