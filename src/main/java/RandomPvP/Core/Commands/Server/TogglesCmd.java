package RandomPvP.Core.Commands.Server;

import RandomPvP.Core.Commands.Command.RCommand;
import RandomPvP.Core.Player.Inventory.Button;
import RandomPvP.Core.Player.Inventory.InventoryType;
import RandomPvP.Core.Player.Inventory.RInventory;
import RandomPvP.Core.Player.MsgType;
import RandomPvP.Core.Player.PlayerManager;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.Rank.Rank;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public class TogglesCmd extends RCommand {

    public TogglesCmd() {
        super("toggles");
        setPlayerOnly(true);
    }

    @Override
    public void onCommand(final RPlayer pl, String string, String[] args) {
        final RInventory inv = new RInventory(27, InventoryType.MENU, "Toggles");
        {
            inv.addButton(new Button() {
                @Override
                public String getName() {
                    return getColorForBoolean(pl.getToggles().isIncognito(), pl.has(Rank.VIP)).toString() + ChatColor.BOLD + "Incognito Mode";
                }

                @Override
                public Material getMaterial() {
                    return getMaterialForBoolean(pl.getToggles().isIncognito(), pl.has(Rank.VIP));
                }

                @Override
                public int getAmount() {
                    return 1;
                }

                @Override
                public List<String> getDescription() {
                    return getDescForBoolean(pl.getToggles().isIncognito(), pl.has(Rank.VIP), Rank.VIP, Arrays.asList(ChatColor.GRAY + "We'll keep you hidden", ChatColor.GRAY + "from non-vips in hubs",
                            ChatColor.GRAY + "until you chat."));
                }

                @Override
                public int getLocation() {
                    return 11;
                }

                @Override
                public boolean closeOnClick() {
                    return false;
                }

                @Override
                public void click(RPlayer pl) {
                    boolean was = pl.getToggles().isIncognito();
                    pl.getToggles().setIncognito(!was);
                    pl.getToggles().setInvisForIncognito(false);

                    setName(getColorForBoolean(pl.getToggles().isIncognito(), pl.has(Rank.VIP)).toString() + ChatColor.BOLD + "Incognito Mode");
                    setMaterial(getMaterialForBoolean(pl.getToggles().isIncognito(), pl.has(Rank.VIP)));
                    setDescription(getDescForBoolean(pl.getToggles().isIncognito(), pl.has(Rank.VIP), Rank.VIP, Arrays.asList(ChatColor.GRAY + "We'll keep you hidden",
                            ChatColor.GRAY + "from non-vips in hubs", ChatColor.GRAY + "until you chat.")));

                    if(was) {
                        for (RPlayer cur : PlayerManager.getInstance().getOnlinePlayers()) {
                            if(!cur.getPlayer().canSee(pl.getPlayer())) {
                                cur.getPlayer().showPlayer(pl.getPlayer());
                            }
                        }

                        pl.message(MsgType.NETWORK, "Left " + ChatColor.BLUE.toString() + ChatColor.BOLD + "INCOGNITO MODE" + ChatColor.GRAY + ".");
                        pl.updateNametag();
                    }

                    pl.playSound(pl.getLocation(), Sound.CLICK, 1F, 1F);
                }
            });
            inv.addButton(new Button() {
                @Override
                public String getName() {
                    return getColorForBoolean(pl.getToggles().isPlayerMessages(), true).toString() + ChatColor.BOLD + "Player Messages";
                }

                @Override
                public Material getMaterial() {
                    return getMaterialForBoolean(pl.getToggles().isPlayerMessages(), true);
                }

                @Override
                public int getAmount() {
                    return 1;
                }

                @Override
                public List<String> getDescription() {
                    return getDescForBoolean(pl.getToggles().isPlayerMessages(), true, Rank.PLAYER, Arrays.asList(ChatColor.GRAY + "We'll prevent players", ChatColor.GRAY + "from messaging you",
                            ChatColor.GRAY + "with /msg."));
                }

                @Override
                public int getLocation() {
                    return 13;
                }

                @Override
                public boolean closeOnClick() {
                    return false;
                }

                @Override
                public void click(RPlayer pl) {
                    pl.getToggles().setPlayerMessages(!pl.getToggles().isPlayerMessages());

                    setName(getColorForBoolean(pl.getToggles().isPlayerMessages(), true).toString() + ChatColor.BOLD + "Player Messages");
                    setMaterial(getMaterialForBoolean(pl.getToggles().isIncognito(), true));
                    setDescription(getDescForBoolean(pl.getToggles().isPlayerMessages(), true, Rank.PLAYER, Arrays.asList(ChatColor.GRAY + "We'll prevent players",
                            ChatColor.GRAY + "from messaging you", ChatColor.GRAY + "with /msg.")));

                    pl.playSound(pl.getLocation(), Sound.CLICK, 1F, 1F);
                }
            });
            inv.addButton(new Button() {
                @Override
                public String getName() {
                    return getColorForBoolean(pl.getToggles().isChatAcknowledgements(), true).toString() + ChatColor.BOLD + "Chat Acknowledgements";
                }

                @Override
                public Material getMaterial() {
                    return getMaterialForBoolean(pl.getToggles().isChatAcknowledgements(), true);
                }

                @Override
                public int getAmount() {
                    return 1;
                }

                @Override
                public List<String> getDescription() {
                    return getDescForBoolean(pl.getToggles().isChatAcknowledgements(), true, Rank.PLAYER, Arrays.asList(ChatColor.GRAY + "When players tag",
                            ChatColor.GRAY + "you in chat (@" + pl.getName() + ")", ChatColor.GRAY + "we'll remind you with a little meow."));
                }

                @Override
                public int getLocation() {
                    return 15;
                }

                @Override
                public boolean closeOnClick() {
                    return false;
                }

                @Override
                public void click(RPlayer pl) {
                    pl.getToggles().setChatAcknowledgements(!pl.getToggles().isChatAcknowledgements());

                    setName(getColorForBoolean(pl.getToggles().isChatAcknowledgements(), true).toString() + ChatColor.BOLD + "Chat Acknowledgements");
                    setMaterial(getMaterialForBoolean(pl.getToggles().isIncognito(), true));
                    setDescription(getDescForBoolean(pl.getToggles().isChatAcknowledgements(), true, Rank.PLAYER, Arrays.asList(ChatColor.GRAY + "When players tag",
                            ChatColor.GRAY + "you in chat (@" + pl.getName() + ")", ChatColor.GRAY + "we'll remind you with a little ding.")));

                    pl.playSound(pl.getLocation(), Sound.CLICK, 1F, 1F);
                }
            });
        }
        pl.openInventory(inv);
    }

    private ChatColor getColorForBoolean(boolean b, boolean canToggle) {
        return (canToggle ? (b ? ChatColor.GREEN : ChatColor.RED) : ChatColor.GRAY);
    }

    private Material getMaterialForBoolean(boolean b, boolean canToggle) {
        return (canToggle ? (b ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK) : Material.IRON_BLOCK);
    }

    public List<String> getDescForBoolean(boolean b, boolean canToggle, Rank needed, List<String> d) {
        List<String> desc = new ArrayList<>();
        {
            desc.addAll(d);

            desc.add("");

            if(canToggle) {
                if (b) {
                    desc.add(ChatColor.GRAY + "Click to " + ChatColor.RED.toString() + ChatColor.BOLD + "DISABLE" + ChatColor.GRAY + ".");
                } else {
                    desc.add(ChatColor.GRAY + "Click to " + ChatColor.GREEN.toString() + ChatColor.BOLD + "ENABLE" + ChatColor.GRAY + ".");
                }
            } else {
                desc.add(ChatColor.GRAY + "Can't toggle.");
                desc.add(ChatColor.RED + "Needed Rank: " + needed.getFormattedName());
            }
        }
        return desc;
    }

}
