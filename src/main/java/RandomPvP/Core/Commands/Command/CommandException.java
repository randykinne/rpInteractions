package RandomPvP.Core.Commands.Command;

/**
 * ***************************************************************************************
 * Copyright (c) Randomizer27 2014. All rights reserved.
 * All code contained within this document and any APIs assocated are
 * the sole property of Randomizer27. Please do not distribute/reproduce without
 * expressed explicit permission from Randomizer27. Not doing so will break the terms of
 * the license, and void any agreements with you, the third party.
 * Thanks.
 * ***************************************************************************************
 */
public class CommandException extends Exception {

    private String message;

    public CommandException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
