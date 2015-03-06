package RandomPvP.Core.Util.Vote.net;

import RandomPvP.Core.Util.Vote.Votifier;
import RandomPvP.Core.Util.Vote.crypto.RSA;
import RandomPvP.Core.Util.Vote.model.Vote;
import RandomPvP.Core.Util.Vote.model.VoteEvent;
import org.bukkit.Bukkit;

import javax.crypto.BadPaddingException;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VoteReceiver extends Thread
{
    private static final Logger LOG = Logger.getLogger("Votifier");
    private final Votifier plugin;
    private final String host;
    private final int port;
    private ServerSocket server;
    private boolean running = true;

    public VoteReceiver(Votifier plugin, String host, int port)
            throws Exception
    {
        this.plugin = plugin;
        this.host = host;
        this.port = port;

        initialize();
    }

    private void initialize() throws Exception {
        try {
            this.server = new ServerSocket();
            this.server.bind(new InetSocketAddress(this.host, this.port));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error initializing vote receiver. Please verify that the configured");

            LOG.log(Level.SEVERE, "IP address and port are not already in use. This is a common problem");

            LOG.log(Level.SEVERE, "with hosting services and, if so, you should check with your hosting provider.", ex);

            throw new Exception(ex);
        }
    }

    public void shutdown()
    {
        this.running = false;
        if (this.server == null)
            return;
        try {
            this.server.close();
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "Unable to shut down vote receiver cleanly.");
        }
    }

    public void run()
    {
        while (this.running)
            try {
                Socket socket = this.server.accept();
                socket.setSoTimeout(5000);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                InputStream in = socket.getInputStream();

                writer.write(new StringBuilder().append("VOTIFIER ").append(Votifier.getInstance().getVersion()).toString());
                writer.newLine();
                writer.flush();

                byte[] block = new byte[256];
                in.read(block, 0, block.length);

                block = RSA.decrypt(block, Votifier.getInstance().getKeyPair().getPrivate());

                int position = 0;

                String opcode = readString(block, position);
                position += opcode.length() + 1;
                if (!opcode.equals("VOTE"))
                {
                    throw new Exception("Unable to decode RSA");
                }

                String serviceName = readString(block, position);
                position += serviceName.length() + 1;
                String username = readString(block, position);
                position += username.length() + 1;
                String address = readString(block, position);
                position += address.length() + 1;
                String timeStamp = readString(block, position);
                position += timeStamp.length() + 1;

                final Vote vote = new Vote();
                vote.setServiceName(serviceName);
                vote.setUsername(username);
                vote.setAddress(address);
                vote.setTimeStamp(timeStamp);

                if (this.plugin.isDebug()) {
                    LOG.info(new StringBuilder().append("Received vote record -> ").append(vote).toString());
                }

                System.out.println("SENDING VOTE EVENT");
                Bukkit.getServer().getPluginManager().callEvent(new VoteEvent(vote));
                writer.close();
                in.close();
                socket.close();
            } catch (SocketException ex) {
                LOG.log(Level.WARNING, new StringBuilder().append("Protocol error. Ignoring packet - ").append(ex.getLocalizedMessage()).toString());
            }
            catch (BadPaddingException ex) {
                LOG.log(Level.WARNING, "Unable to decrypt vote record. Make sure that that your public key");

                LOG.log(Level.WARNING, "matches the one you gave the server list.", ex);
            }
            catch (Exception ex) {
                LOG.log(Level.WARNING, "Exception caught while receiving a vote notification", ex);
            }
    }

    private String readString(byte[] data, int offset)
    {
        StringBuilder builder = new StringBuilder();
        for (int i = offset; (i < data.length) &&
                (data[i] != 10); i++)
        {
            builder.append((char)data[i]);
        }
        return builder.toString();
    }
}
