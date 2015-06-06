package RandomPvP.Core.Commands.Server;

import RandomPvP.Core.Commands.Command.RCommand;
import RandomPvP.Core.Server.Game.GameManager;
import RandomPvP.Core.Server.Game.GameState.GameState;
import RandomPvP.Core.Player.MsgType;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Util.GUI.ShopGUI;
import RandomPvP.Core.Util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public class ShopCmd extends RCommand {

    private ShopGUI gui = new ShopGUI("Global Shop", "global", ItemBuilder.build(Material.BEACON, ChatColor.GREEN.toString() + ChatColor.BOLD + "GLOBAL SHOP", 1));

    public ShopCmd() {
        super("shop");
        setPlayerOnly(true);
    }

    @Override
    public void onCommand(RPlayer pl, String string, String[] args) {
        if(GameManager.getState() == GameState.LOBBY) {
            gui.open(pl);
        } else {
            pl.message(MsgType.ERROR, "The game must be in lobby to use this command.");
        }
    }

}
