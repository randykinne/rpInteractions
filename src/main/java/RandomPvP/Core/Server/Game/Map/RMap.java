package RandomPvP.Core.Server.Game.Map;

import org.bukkit.World;

import java.util.List;

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
public class RMap {

    //File info;
    String name;
    List<String> builders;
    String link;
    World world;

    public RMap(String name, String link, List<String> builders, World world) {
        //info = new File(world.getWorldFolder(), "MapInfo");

        /*
        if (!info.exists()) {
            try {
                info.createNewFile();

                FileWriter fileWriter = new FileWriter(info, true);

                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                bufferedWriter.write(name + "\n");
                bufferedWriter.write(builders.toString());
                if (link != null) bufferedWriter.write("\n" + link);
                bufferedWriter.close();

            } catch (Exception ignored) {}
        } else {
            try {

                Scanner scan = new Scanner(info);
                while (scan.hasNext()) {

                }
            } catch (Exception e) {}
        }
        */

        this.name = name;
        this.builders = builders;
        this.world = world;
        this.link = link;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getBuilders() {
        return builders;
    }
    public void setBuilders(List<String> builders) {
        this.builders = builders;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean hasLink() {
        return link != null;
    }

    public World getMap() {
        return world;
    }

    public void setMap(World map) {
        this.world = map;
    }

}
