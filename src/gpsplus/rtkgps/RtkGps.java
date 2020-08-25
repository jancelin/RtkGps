package gpsplus.rtkgps;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.PoGoPin;

import gpsplus.ntripcaster.NTRIPCaster;

import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.gdal.gdal.gdal;
import org.gdal.ogr.ogr;
import org.proj4.PJ;

import java.io.OutputStream;
import java.io.PrintStream;

@ReportsCrashes(formKey = "",
    mailTo = "bug@sudagri-jatropha.com",
    mode = ReportingInteractionMode.TOAST,
    resToastText = R.string.crash_toast_text)
public class RtkGps extends Application {

    private static final boolean DBG = BuildConfig.DEBUG & true;
    private static String VERSION = "";

    private static class MyErrDump extends PrintStream {
        public MyErrDump( OutputStream destination) {
            super(destination);
        }

        public synchronized void println(String str) {
            super.println(str);
            if (str.equals("System.err: StrictMode VmPolicy violation with POLICY_DEATH; shutting down.")) {
                super.println("CGDEBUG - Trapped StrictMode shutdown notice: heap data");
                try {
                    android.os.Debug.dumpHprofData("/sdcard/RtkGps/strictmode-death-penalty.hprof");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onCreate() {


        super.onCreate();


        //ACRA.init(this);
        System.loadLibrary("proj");
        Log.v("Proj4","Proj4 version: "+PJ.getVersion());

        System.loadLibrary("ntripcaster");
        Log.v("ntripcaster","NTRIP Caster "+NTRIPCaster.getVersion());

        System.loadLibrary("rtkgps");


        //System.loadLibrary("gdalalljni"); //Automaticaly done
        ogr.RegisterAll();
        gdal.AllRegister();
        Log.v("GDAL",gdal.VersionInfo("--version"));

        if (DBG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }
        super.onCreate();
        //ACRA.init(this);
        System.loadLibrary("proj");
        Log.v("Proj4","Proj4 version: "+PJ.getVersion());

        System.loadLibrary("ntripcaster");
        Log.v("ntripcaster","NTRIP Caster "+NTRIPCaster.getVersion());

        System.loadLibrary("rtkgps");

        //System.loadLibrary("gdalalljni"); //Automaticaly done
        ogr.RegisterAll();
        gdal.AllRegister();
        Log.v("GDAL",gdal.VersionInfo("--version"));
        //set version
        PackageInfo pi;
        try {
            pi = getPackageManager().getPackageInfo(getPackageName(), 0);
            RtkGps.VERSION = pi.versionName;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String getVersion() {
        return RtkGps.VERSION;
    }
}
