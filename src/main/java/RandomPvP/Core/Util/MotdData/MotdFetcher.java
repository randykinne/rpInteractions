package RandomPvP.Core.Util.MotdData;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public class MotdFetcher {

    private String ip;
    private int port;

    private int maxPlayers;
    private int onlinePlayers;
    private String motd;

    public MotdFetcher(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void fetch() {
        //credit to skore87(bukkit forums)
        try {
            Socket sock = new Socket(ip, port);

            DataOutputStream out = new DataOutputStream(sock.getOutputStream());
            DataInputStream in = new DataInputStream(sock.getInputStream());

            out.write(0xFE);

            int b;
            StringBuffer str = new StringBuffer();
            while ((b = in.read()) != -1) {
                if (b != 0 && b > 16 && b != 255 && b != 23 && b != 24) {
                    // Not sure what use the two characters are so I omit them
                    str.append((char) b);
                    //System.out.println(b + ":" + ((char) b));
                }
            }

            String[] data = str.toString().split("§");
            motd = data[0];
            onlinePlayers = Integer.parseInt(data[1]);
            maxPlayers = Integer.parseInt(data[2]);

            /*System.out.println(String.format(
                    ">>>>>>>>>>>>>>>>>>>>>> MOTD: \"%s\"\nOnline Players: %d/%d <<<<<<<<<<<<<<<<<<", motd,
                    onlinePlayers, maxPlayers));
                    */
        } catch (UnknownHostException e) {
            System.out.println("[INFO] [MOTDFETCHER] You tried to catch the MOTD of server " + ip + ":" + port + " but got an UnknownHostException!! (" + e.getMessage() + ")");
            maxPlayers = 0;
            maxPlayers = 0;
            motd = null;
        } catch (IOException e) {
            System.out.println("[ERROR] [MOTDFETCHER] IOException occurred while trying to fetch the data of server " + ip + ":" + port + "!! (" + e.getMessage() + ")");
            maxPlayers = 0;
            maxPlayers = 0;
            motd = null;
        }
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getOnlinePlayers() {
        return onlinePlayers;
    }

    public String getMotd() {
        return motd;
    }

}
