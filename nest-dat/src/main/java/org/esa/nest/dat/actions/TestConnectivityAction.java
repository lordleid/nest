package org.esa.nest.dat.actions;

import org.esa.beam.framework.ui.command.CommandEvent;
import org.esa.beam.framework.ui.command.ExecCommand;
import org.esa.beam.visat.VisatApp;
import org.esa.nest.util.Settings;
import org.esa.nest.util.ftpUtils;

/**
 * This action test if FTP is working
 *
 */
public class TestConnectivityAction extends ExecCommand {

    private static final String remoteFTPSRTM = Settings.instance().get("DEM/srtm3GeoTiffDEM_FTP");
    private static final String remotePathSRTM = ftpUtils.getPathFromSettings("DEM/srtm3GeoTiffDEM_remotePath");

    private static final String delftFTP = Settings.instance().get("OrbitFiles/delftFTP");
    private static final String delftFTPPath = Settings.instance().get("OrbitFiles/delftFTP_ERS2_precise_remotePath");

    @Override
    public void actionPerformed(CommandEvent event) {

        boolean failed = false;
        String msg1 = "Connection to FTP "+ remoteFTPSRTM + remotePathSRTM;
        if(ftpUtils.testFTP(remoteFTPSRTM, remotePathSRTM)) {
            msg1 += " PASSED";
        } else {
            msg1 += " FAILED";
            failed = true;
        }

        String msg2 = "Connection to FTP "+ delftFTP + delftFTPPath;
        if(ftpUtils.testFTP(delftFTP, delftFTPPath)) {
            msg2 += " PASSED";
        } else {
            msg2 += " FAILED";
            failed = true;
        }

        String msg = msg1 +"\n" +msg2;
        if(failed) {
            msg += "\n\nPlease verify that all paths are correct in your $NEST_HOME/config/settings.xml";
            msg += "\nAlso verify that FTP is not blocked by your firewall.";
        }
        VisatApp.getApp().showInfoDialog(msg, null);
    }
}