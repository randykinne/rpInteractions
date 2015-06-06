package RandomPvP.Core.Player.Toggles;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public class PlayerToggles {

    private boolean playerMessages;

    private boolean incognito;
    private boolean invisForIncognito = false;

    private boolean chatAcknowledgements;

    public PlayerToggles(boolean playerMessages, boolean incognito, boolean chatAcknowledgements) {
        this.playerMessages = playerMessages;
        this.incognito = incognito;
        this.chatAcknowledgements = chatAcknowledgements;
    }

    public boolean isPlayerMessages() {
        return playerMessages;
    }

    public void setPlayerMessages(boolean playerMessages) {
        this.playerMessages = playerMessages;
    }

    public boolean isIncognito() {
        return incognito;
    }

    public void setIncognito(boolean incognito) {
        this.incognito = incognito;
    }

    public boolean isInvisForIncognito() {
        return invisForIncognito;
    }

    public void setInvisForIncognito(boolean invisForInvognito) {
        this.invisForIncognito = invisForInvognito;
    }

    public boolean isChatAcknowledgements() {
        return chatAcknowledgements;
    }

    public void setChatAcknowledgements(boolean chatAcknowledgements) {
        this.chatAcknowledgements = chatAcknowledgements;
    }
}
