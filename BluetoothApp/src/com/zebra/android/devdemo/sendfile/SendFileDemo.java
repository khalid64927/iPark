/********************************************** 
 * CONFIDENTIAL AND PROPRIETARY 
 *
 * The source code and other information contained herein is the confidential and the exclusive property of
 * ZIH Corp. and is subject to the terms and conditions in your end user license agreement.
 * This source code, and any other information contained herein, shall not be copied, reproduced, published, 
 * displayed or distributed, in whole or in part, in any medium, by any means, for any purpose except as
 * expressly permitted under such license agreement.
 * 
 * Copyright ZIH Corp. 2010
 *
 * ALL RIGHTS RESERVED 
 ***********************************************/
package com.zebra.android.devdemo.sendfile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;

import com.zebra.android.comm.BluetoothPrinterConnection;
import com.zebra.android.comm.TcpPrinterConnection;
import com.zebra.android.comm.ZebraPrinterConnection;
import com.zebra.android.comm.ZebraPrinterConnectionException;
import com.zebra.android.devdemo.ConnectionScreen;
import com.zebra.android.devdemo.util.SettingsHelper;
import com.zebra.android.devdemo.util.UIHelper;
import com.zebra.android.printer.PrinterLanguage;
import com.zebra.android.printer.ZebraPrinterFactory;
import com.zebra.android.printer.ZebraPrinterLanguageUnknownException;

public class SendFileDemo extends ConnectionScreen {

    private UIHelper helper = new UIHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        testButton.setText("Send Test File");
    }

    @Override
    public void performTest() {
        new Thread(new Runnable() {
            public void run() {
                Looper.prepare();
                sendFile();
                Looper.loop();
                Looper.myLooper().quit();
            }
        }).start();

    }

    private void sendFile() {
        ZebraPrinterConnection connection = null;
        if (isBluetoothSelected() == true) {
            connection = new BluetoothPrinterConnection(getMacAddressFieldText());
        } else {
            try {
                int port = Integer.parseInt(getTcpPortNumber());
                connection = new TcpPrinterConnection(getTcpAddress(), port);
            } catch (NumberFormatException e) {
                helper.showErrorDialogOnGuiThread("Port number is invalid");
                return;
            }
        }
        try {
            helper.showLoadingDialog("Sending file to printer ...");
            connection.open();
            printer = ZebraPrinterFactory.getInstance(connection);
            testSendFile();
            connection.close();
        } catch (ZebraPrinterConnectionException e) {
            System.out.println(">>>>>>  ZebraPrinterConnectionException  >>>>>>>>>>>>.");
            e.printStackTrace();
            helper.showErrorDialogOnGuiThread(e.getMessage());
        } catch (ZebraPrinterLanguageUnknownException e) {
            System.out.println(">>>>>>> ZebraPrinterLanguageUnknownException >>>>>>>>>>>.");
            e.printStackTrace();
            helper.showErrorDialogOnGuiThread(e.getMessage());
        } finally {
            helper.dismissLoadingDialog();
        }
    }

    private void testSendFile() {
        try {
            File filepath = getFileStreamPath("TEST.LBL");
            createDemoFile("TEST.LBL");
            printer.getFileUtil().sendFileContents(filepath.getAbsolutePath());
            SettingsHelper.saveBluetoothAddress(this, getMacAddressFieldText());
            SettingsHelper.saveIp(this, getTcpAddress());
            SettingsHelper.savePort(this, getTcpPortNumber());

        } catch (ZebraPrinterConnectionException e1) {
            helper.showErrorDialogOnGuiThread("Error sending file to printer");
        } catch (IOException e) {
            helper.showErrorDialogOnGuiThread("Error creating file");
        }
    }

    private void createDemoFile(String fileName) throws IOException {

        FileOutputStream os = this.openFileOutput(fileName, Context.MODE_PRIVATE);

        byte[] configLabel = null;

        PrinterLanguage pl = printer.getPrinterControlLanguage();
        if (pl == PrinterLanguage.ZPL) {
            configLabel = "^XA^FO17,16^GB379,371,8^FS^FT65,255^A0N,135,134^FDTEST^FS^XZ".getBytes();
        } else if (pl == PrinterLanguage.CPCL) {
            String cpclConfigLabel = "! 0 200 200 406 1\r\n" + "ON-FEED IGNORE\r\n" + "BOX 20 20 380 380 8\r\n" + "T 0 6 137 177 TEST\r\n" + "PRINT\r\n";
            configLabel = cpclConfigLabel.getBytes();
        }
        os.write(configLabel);
        os.flush();
        os.close();
    }

}
