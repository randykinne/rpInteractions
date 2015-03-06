package RandomPvP.Core.Punish;

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
public enum PunishmentType {
    IP_BAN("IP Ban", "Banned"),
    PERMANENT_BAN("Permanent Ban", "Banned"),
    TEMPORARY_BAN("Temporary Ban", "Banned"),
    IP_MUTE("IP Mute", "Muted"),
    PERMANENT_MUTE("Permanent Mute", "Muted"),
    TEMPORARY_MUTE("Temporary Mute", "Muted"),
    KICK("Kick", "Kicked"),
    WARN("Warning", "Warned");

    private String name;
    private String verb;

    PunishmentType(String type, String verb) {
        this.name = type;
        this.verb = verb;
    }

    /**
     * Get the name of the punishment type
     *
     * @return Name of punishment type
     */
    public String getName() {
        return name;
    }

    public String getVerb() { return verb; }
    /**
     * Get a punishment type from a string
     *
     * @param name Name of punishment type
     * @return Punish type from string
     */
    public static PunishmentType fromString(String name) {
        for (PunishmentType type : values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        return null;
    }
}
