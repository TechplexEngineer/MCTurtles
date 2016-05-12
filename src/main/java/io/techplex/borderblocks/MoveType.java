/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.techplex.borderblocks;

/**
 * Types of movements.
 *
 * <p>Used with {@link Session#testMoveTo(Location, MoveType)}.</p>
 */
public enum MoveType {

    RESPAWN(false, true),
    EMBARK(true, false),
    MOVE(true, false),
    TELEPORT(true, true),
    RIDE(true, false),
    OTHER_NON_CANCELLABLE(false, false),
    OTHER_CANCELLABLE(true, false);

    private final boolean cancellable;
    private final boolean teleport;

    MoveType(boolean cancellable, boolean teleport) {
        this.cancellable = cancellable;
        this.teleport = teleport;
    }

    public boolean isCancellable() {
        return cancellable;
    }

    public boolean isTeleport() {
        return teleport;
    }
}
