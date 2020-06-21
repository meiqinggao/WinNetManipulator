## WinNetManipulator - A Java-Based Network Hacking Tool for Geek on Windows Platform

This tool empowers you to sniff the network info, manipulate the payload data as you want, and send to your desired destination. This project is built on Open Source Project: WinDivert, which enables you to intercept the network payload before sending out.

I wrap this C++ dll with SpringBoot, also I have been put some fake demo usage code in WinNetApplication.java. 

To run the demo application, you need to replace the SRC_IP and DEST_IP as the ones in your project. You also need to know how to define the filtering string for WinDivert (Goolge/Baidu WinDivert). And the CONTAINED_STRING is the string snippet which is contained the source TCP payload you want to replace, only unique piece is enough, no need to provide the whole string. REPLACEMENT is the payload data you want to inject to into the packat. **NOTE:**, the most important part about this project is the changed_size variable, because the source payload and the new injected payload usually do not have the same length. You need to adjust the sequence and acknoledgement number. Don't know how to change? Maybe you need to review TCP/IP knowledge.

I have been using this network hacking tool for a long time. This is my personal project previously. I use this tool for more compliated way. I removed all my git log and business related parts away from the project, and only provide the demo code, because the git log and business related parts contains personal secrect information.

Furture enhancement: 
 - Upgrade WinDivert to version 2.2.0
 - Several TO-DO in the code. 
 - Web interface configuration page for easy usage. 
