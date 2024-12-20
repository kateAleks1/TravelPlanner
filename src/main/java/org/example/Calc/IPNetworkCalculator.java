package org.example.Calc;

import java.util.Scanner;

import java.util.Scanner;

public class IPNetworkCalculator {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter IP address (e.g., 192.168.106.9): ");
        String ipAddress = scanner.nextLine();
        System.out.print("Enter prefix (e.g., 22): ");
        int prefix = scanner.nextInt();

        int subnetMask = calculateSubnetMask(prefix);
        String subnetMaskStr = convertToDottedDecimal(subnetMask);

        int ipAsInt = convertIpToInteger(ipAddress);
        int networkAddress = ipAsInt & subnetMask;
        String networkAddressStr = convertToDottedDecimal(networkAddress);

        int hostBits = 32 - prefix;
        int totalHosts = (int) Math.pow(2, hostBits) - 2;


        int nodeNumber = ipAsInt - networkAddress;

        System.out.println("\nResults:");
        System.out.println("Subnet Mask: " + subnetMaskStr);
        System.out.println("Network Address: " + networkAddressStr);
        System.out.println("Total Hosts: " + totalHosts);
        System.out.println("Node Number: " + nodeNumber);
    }


    private static int calculateSubnetMask(int prefix) {
        return (int) ((0xFFFFFFFFL << (32 - prefix)) & 0xFFFFFFFFL);
    }

    private static String convertToDottedDecimal(int ip) {
        return String.format("%d.%d.%d.%d", (ip >> 24) & 0xFF, (ip >> 16) & 0xFF, (ip >> 8) & 0xFF, ip & 0xFF);
    }

    private static int convertIpToInteger(String ipAddress) {
        String[] parts = ipAddress.split("\\.");
        int ip = 0;
        for (String part : parts) {
            ip = (ip << 8) | Integer.parseInt(part);
        }
        return ip;
    }
}
