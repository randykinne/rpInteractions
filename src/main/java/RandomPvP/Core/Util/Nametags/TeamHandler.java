package RandomPvP.Core.Util.Nametags;

/**
 * This code is directly for NametagEdit and has been edited.
 */
public class TeamHandler {

    private String name;
    private String prefix;
    private String suffix;

    public TeamHandler(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

}
