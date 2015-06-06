package RandomPvP.Core.Util.MotdData;

import RandomPvP.Core.Util.NetworkUtil;

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

    private int maxPlayers = 0;
    private int onlinePlayers = 0;
    private String motd = "";

    public MotdFetcher(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void fetch() throws Exception {
        //credit to skore87(bukkit forums)
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
            }
        }

        String[] data = str.toString().split("§");
        motd = data[0];
        onlinePlayers = Integer.parseInt(data[1]);
        maxPlayers = Integer.parseInt(data[2]);
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
