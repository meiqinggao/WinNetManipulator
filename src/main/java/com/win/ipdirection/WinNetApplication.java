package com.win.ipdirection;

import com.win.ipdirection.headers.Tcp;
import com.win.ipdirection.windivert.WinDivertAddress;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class WinNetApplication implements ApplicationRunner {
    private static final String DEST_IP = "111.111.111.111";
    private static final String SRC_IP = "222.222.222.222";
    private static final String REPLACEMENT_1 = "toRepleceString1";
    private static final String REPLACEMENT_2 = "toRepleceString2";
    private static final String CONTAINED_STRING_1 = "containedString1";
    private static final String CONTAINED_STRING_2 = "containedString2";

    public static void main(String[] args) {
        SpringApplication.run(WinNetApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String filter = "(((ip.DstAddr == 111.111.111.111 and tcp.DstPort == 88) or false) and outbound) " +
                "or (((ip.SrcAddr == 222.222.222.222 and tcp.SrcPort == 88) or false) and inbound)";

        WinDivert w = new WinDivert(filter);

        w.open();

        boolean alreadyReplaced = false;
        int changedSize = 0;

        while (true) {
            Packet packet = w.recv();

            if (packet.getDstAddr().equals(DEST_IP)) {
                if (packet.getTcp().is(Tcp.Flag.SYN)) {
                    changedSize = 0;
                    alreadyReplaced = false;
                }
                String payLoad = Util.printHexBinary(packet.getPayload());

                if (!alreadyReplaced) {
                    if (Strings.isNotEmpty(payLoad) && payLoad.toLowerCase().contains(CONTAINED_STRING_1)) {
                        packet.setPayload(Util.parseHexBinary(REPLACEMENT_1));
                    } else if (Strings.isNotEmpty(payLoad) && payLoad.toLowerCase().contains(CONTAINED_STRING_2)) {
                        int newLen = REPLACEMENT_2.length();
                        String hexString = Integer.toHexString(newLen / 2 + 40);
                        String zeroPrependedHexString = StringUtils.prependZero(hexString);

                        String secondRaw = Util.printHexBinary(packet.getRaw());
                        String newRawString = secondRaw.substring(0, 4) + zeroPrependedHexString + secondRaw.substring(8, 80) + REPLACEMENT_2;
                        WinDivertAddress addr = packet.getWinDivertAddress();
                        packet = new Packet(Util.parseHexBinary(newRawString), new int[]{addr.IfIdx.intValue(), addr.SubIfIdx.intValue()}, Enums.Direction.INBOUND);
                        packet.recalculateChecksum();
                        alreadyReplaced = true;
                        int oldLen = payLoad.length();
                        changedSize = (newLen - oldLen)/2;
                    }
                } else {
                    int newSeq = packet.getTcp().getSeqNumber() + changedSize;
                    packet.getTcp().setSeqNumber(newSeq);
                }
            } else if (packet.getSrcAddr().equals(SRC_IP)) {
                int newAck = packet.getTcp().getAckNumber() - changedSize;
                packet.getTcp().setAckNumber(newAck);
            }

            try {
                w.send(packet);
            } catch (Exception e) {
                System.out.println("Exception: " + e.getMessage());
                System.out.println("Exception: " + e);
            }
        }
    }
}
