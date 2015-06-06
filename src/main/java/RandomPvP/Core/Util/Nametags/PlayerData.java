package RandomPvP.Core.Util.Nametags;

public class PlayerData {

    private String name;
    private String uuid;
    private String prefix;
    private String suffix;

    public PlayerData(String name, String uuid, String prefix, String suffix) {
        this.name = name;
        this.uuid = uuid;
        this.prefix = prefix;
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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