package RandomPvP.Core.Util.Nametags;

import RandomPvP.Core.RPICore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

/**
 * This API class is used to set prefixes and suffixes at a high level.
 * This code is directly for NametagEdit and has been edited.
 */
public class NametagAPI {

    private NametagAPI() {
        // To restrict developers from accessing this class non-statically
    }

    /**
     * Clears the players existing prefix and suffix data</br></br>This method
     * is useful when the tag is frequently updated, so as to avoid "Cannot remove
     * from team" error
     *
     * @param player the player to clear
     */
    public static void clear(String player) {
        NametagManager.clear(player);
    }

    /**
     * Sets the custom prefix for the given player </br></br> This method
     * schedules a task with the request to change the player's name to prevent
     * it from clashing with the PlayerJoinEvent in NametagEdit.
     *
     * @param player the player to set the prefix for
     * @param prefix the prefix to use
     */
    public static void setPrefix(final String player, final String prefix) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(RPICore.getInstance(), new Runnable() {
            @Override
            public void run() {
                NametagManager.update(player, format(prefix), "");
            }
        });
    }

    /**
     * Sets the custom suffix for the given player </br></br> This method
     * schedules a task with the request to change the player's name to prevent
     * it from clashing with the PlayerJoinEvent in NametagEdit.
     *
     * @param player the player to set the suffix for
     * @param suffix the suffix to use
     */
    public static void setSuffix(final String player, final String suffix) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(RPICore.getInstance(), new Runnable() {
            @Override
            public void run() {
                NametagManager.update(player, format(suffix), "");
            }
        });
    }

    /**
     * Sets the custom given prefix and suffix to the player, overwriting any
     * existing prefix or suffix. If a given prefix or suffix is null/empty, it
     * will be removed from the player. </br></br> This method schedules a task
     * with the request to change the player's name to prevent it from clashing
     * with the PlayerJoinEvent in NametagEdit.
     *
     * @param player the player to set the prefix and suffix for
     * @param prefix the prefix to use
     * @param suffix the suffix to use
     */
    public static void setNametagHard(final String player, final String prefix, final String suffix) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(RPICore.getInstance(), new Runnable() {
            @Override
            public void run() {
                NametagManager.overlap(player, format(prefix), format(suffix));
            }
        });
    }

    /**
     * Sets the custom given prefix and suffix to the player. If a given prefix
     * or suffix is empty/null, it will be ignored. </br></br> This method
     * schedules a task with the request to change the player's name to prevent
     * it from clashing with the PlayerJoinEvent in NametagEdit.
     *
     * @param player the player to set the prefix and suffix for
     * @param prefix the prefix to use
     * @param suffix the suffix to use
     */
    public static void setNametagSoft(final String player, final String prefix, final String suffix) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(RPICore.getInstance(), new Runnable() {
            @Override
            public void run() {
                NametagManager.update(player, format(prefix), format(suffix));
            }
        });
    }

    /**
     * Sets the custom given prefix and suffix to the player, overwriting any
     * existing prefix or suffix. If a given prefix or suffix is null/empty, it
     * will be removed from the player.<br>
     * <br>
     *
     * This method does not save the modified nametag, it only updates it about
     * their head. use setNametagSoft and setNametagHard if you don't know what
     * you're doing. </br></br> This method schedules a task with the request to
     * change the player's name to prevent it from clashing with the
     * PlayerJoinEvent in NametagEdit.
     *
     * @param player the player to set the prefix and suffix for
     * @param prefix the prefix to use
     * @param suffix the suffix to use
     */
    public static void updateNametagHard(final String player, final String prefix, final String suffix) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(RPICore.getInstance(), new Runnable() {
            @Override
            public void run() {
                NametagManager.overlap(player, format(prefix), format(suffix));
            }
        });
    }

    /**
     * Sets the custom given prefix and suffix to the player. If a given prefix
     * or suffix is empty/null, it will be ignored.<br>
     * <br>
     *
     * This method does not save the modified nametag, it only updates it about
     * their head. use setNametagSoft and setNametagHard if you don't know what
     * you're doing. </br></br> This method schedules a task with the request to
     * change the player's name to prevent it from clashing with the
     * PlayerJoinEvent in NametagEdit.
     *
     * @param player the player to set the prefix and suffix for
     * @param prefix the prefix to use
     * @param suffix the suffix to use
     */
    public static void updateNametagSoft(final String player, final String prefix, final String suffix) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(RPICore.getInstance(), new Runnable() {
            @Override
            public void run() {
                NametagManager.update(player, format(prefix), format(suffix));
            }
        });
    }

    /**
     * Returns the prefix for the given player name
     *
     * @param player the player to check
     * @return the player's prefix, or null if there is none.
     */
    public static String getPrefix(String player) {
        return NametagManager.getPrefix(player);
    }

    /**
     * Returns the suffix for the given player name
     *
     * @param player the player to check
     * @return the player's suffix, or null if there is none.
     */
    public static String getSuffix(String player) {
        return NametagManager.getSuffix(player);
    }

    /**
     * Returns the entire nametag for the given player
     *
     * @param player the player to check
     * @return the player's prefix, actual name, and suffix in one string
     */
    public static String getNametag(String player) {
        return NametagManager.getFormattedName(player);
    }

    /**
     * Function colorizes and formats a string input, and will
     * concatenate if it the string exceeds 16 characters
     *
     * @param input the string to format
     * @return the formatted string
     */
    public static String format(String input) {
        input = ChatColor.translateAlternateColorCodes('&', input);
        return input.length() > 16 ? input.substring(0, 16) : input;
    }
}
