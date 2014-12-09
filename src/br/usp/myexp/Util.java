package br.usp.myexp;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.Environment;

public class Util {

    public static File getDir() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            String path = Environment.getExternalStorageDirectory().getPath() + File.separator + Constants.MY_EXP_DIR;
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdir();
            }
            return dir;
        }
        return null;
    }
    
    /**
     * Get string dd/MM/yyyy HH:mm:ss date.
     * @return date
     */
    public static String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return String.valueOf(dateFormat.format(date));
    }
    
    /**
     * Get string dd-MM-yyyy date.
     * @return date
     */
    public static String getTodayDateForFileName() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date = new Date();
        return String.valueOf(dateFormat.format(date));
    }

    public static String getGoogleUserId(Context context) {
        AccountManager manager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        Account[] list = manager.getAccountsByType("com.google");
        Account account = list[0];
        String user = account.name;
        if (user.contains("@")) {
            user = user.substring(0, user.indexOf("@"));
        }
        return user;
    }

}
