package com.cogent.cogentappointment.admin.utils.commons;

import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.cogent.cogentappointment.admin.constants.PatternConstants.NetworkConstants.*;

/**
 * @author smriti on 25/04/20
 */
public class NetworkUtils {

    private static Function<String, String> getClientMACAddress = (ip) -> {
        Pattern macpt = null;

        /* Find OS and set command according to OS*/
        String OS = System.getProperty("os.name").toLowerCase();

        /*ARP stands for Address Resolution Protocol. When you try to ping an IP address on your local network,
        say 192.168.1.1, your system has to turn the IP address 192.168.1.1 into a MAC address. Because it is
        a broadcast packet, it is sent to a special MAC address that causes all machines on the network to receive it.*/
        String[] cmd;
        if (OS.contains("win")) {
           /*For Windows*/
            macpt = Pattern.compile(PATTERN_REGEX_FOR_WINDOWS);
            String[] a = {ARP, ARP_A, ip};
            cmd = a;
        } else {
            /*For Mac OS X, Linux*/
            macpt = Pattern.compile(PATTERN_REGEX_FOR_MAC_OR_LINUX);
            String[] a = {ARP, ip};
            cmd = a;
        }

        BufferedReader reader = null;

        try {
            /*Run command*/
            Process p = Runtime.getRuntime().exec(cmd);
            p.waitFor();

            /*Read output with BufferedReader*/
            System.out.println(p.getInputStream());
            reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = reader.readLine();

            /*Line is of format:*/
            /*Address                  HWtype  HWaddress           Flags Mask            Iface */
            /*192.168.100.8            ether   d8:5d:e2:a4:f5:85   C                     wlp5s0*/
            /*Loop through lines*/
            while (line != null) {
                Matcher m = macpt.matcher(line);

                /*when Matcher finds a Line then return it as result*/
                if (m.find())
                    return m.group();

                line = reader.readLine();
            }

        } catch (IOException | InterruptedException e1) {
            e1.printStackTrace();
        } finally {
            try {
                assert reader != null;
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Return empty string if no MAC is found
        return "";
    };

    private static String getLocalHostAddress() {

        StringBuilder sb = new StringBuilder();

        String macAddress = "";

        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();

                if (iface.isLoopback() || !iface.isUp() || iface.isVirtual() || iface.isPointToPoint())
                    continue;

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {

                    InetAddress addr = addresses.nextElement();

                    /*GET MAC ADDRESS*/

                    if (ObjectUtils.isEmpty(macAddress)) {

                        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(addr);

                        byte[] mac = networkInterface.getHardwareAddress();

                    /*CONVERT mac(IN DECIMAL FORMAT) TO HEXADECIMAL FORMAT */
                        for (int i = 0; i < mac.length; i++) {
                            sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                            macAddress = sb.toString();
                        }
                    }

                }
            }
            return macAddress;
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    public static Function<HttpServletRequest, String> getDeviceAddress = (request) -> {

        String remoteAddr = "";

        String mac = "";

        if (!Objects.isNull(request)) {
            remoteAddr = request.getHeader(REQUEST_HEADER);
            if (remoteAddr == null || "".equals(remoteAddr))
                remoteAddr = request.getRemoteAddr();
        }

        if (remoteAddr.equals(LOCALHOST_IPV6_ADDRESS)) {
            mac = getLocalHostAddress();
        } else {
            mac = getClientMACAddress.apply(remoteAddr);
        }

        return mac;
    };
}
