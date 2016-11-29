package tk.saiprasadm.smartled;

import org.apache.commons.validator.routines.InetAddressValidator;

/**
 * Created by SAI on 25-06-2016.
 */
public class IP {
    private int port;
    private String ip;
    private String IPwithPort;

    IP(String IPwithPort) {
        this.IPwithPort = IPwithPort;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public boolean isValidIPAddress() throws NumberFormatException {
        String ipandport[] = new String[2];
        InetAddressValidator validator;
        if (IPwithPort.contains(":")) {

            ipandport = IPwithPort.split(":");
        } else {
            ipandport[0] = IPwithPort;
            ipandport[1] = "333";
        }
        validator = new InetAddressValidator();
        if (validator.isValidInet4Address(ipandport[0])) {
            this.ip = ipandport[0];
            this.port = Integer.parseInt(ipandport[1]);

            return true;
        } else {
            return false;
        }
    }
}
