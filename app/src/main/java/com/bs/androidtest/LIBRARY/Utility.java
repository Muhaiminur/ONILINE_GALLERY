package com.bs.androidtest.LIBRARY;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.TransitionDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.StatFs;
import android.os.StrictMode;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import com.bs.androidtest.R;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.ACTIVITY_SERVICE;

public class Utility {

    Context context;
    ProgressDialog mProgressDialog;
    ProgressBarView progressDialog;

    public Utility(Context context) {
        this.context = context;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        freeMemory();
    }

    public Utility() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        freeMemory();
    }

    /* *//*
   ================ Show Progress Dialog ===============
   *//*
    public void showProgress(boolean isCancelable, String message) {
        mProgressDialog = new ProgressDialog(context, R.style.AppCompatAlertDialogStyle);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(isCancelable);
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    *//*
    ================ Hide Progress Dialog ===============
    *//*
    public void hideProgress() {
        try {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }*/

    public void showProgress(boolean isCancelable, String message) {
        try {
            if (progressDialog == null) {
                progressDialog = new ProgressBarView();
            }
            progressDialog.setCancelable(isCancelable);
            progressDialog.show(((FragmentActivity) context).getSupportFragmentManager(), message);
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    /*
    ================ Hide Progress Dialog ===============
    */
    public void hideProgress() {
        try {
            if (progressDialog != null || progressDialog.isVisible()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }


    public boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }


    /*
    ================ Show Toast Message ===============
    */
    public void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    /*
    =============== Set Window FullScreen ===============
    */
    public void setFullScreen() {
        Activity activity = ((Activity) context);
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /*
        ================ Get Screen Width ===============
        */
    public HashMap<String, Integer> getScreenRes() {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        map.put(KeyWord.SCREEN_WIDTH, width);
        map.put(KeyWord.SCREEN_HEIGHT, height);
        map.put(KeyWord.SCREEN_DENSITY, (int) metrics.density);
        return map;
    }


    /*
    ================ Log function ===============
     */
    public void logger(String message) {
        if (context != null) {
            Log.d(context.getString(R.string.app_name), message);
        } else {
            Log.d("Android Test", message);
        }
        long currentTime = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
        String date = sdf.format(new Date());
        //writeToFile(date+" -> "+message);
    }


    /*
============== Base64 Decode =========
*/
    public String decodeBase64(String message) {
        String text = "Conversion Error";
        try {
            byte[] bytes = null;
            bytes = Base64.decode(message, Base64.DEFAULT);
            text = new String(bytes, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            logger(ex.toString());
        }
        return text;
    }

    /*
    ============== Base64 Encode =========
     */
    public String encodeBase64(String message) {
        String text = "Conversion Error";
        try {
            byte[] data = message.getBytes(StandardCharsets.UTF_8);
            text = Base64.encodeToString(data, Base64.DEFAULT);
        } catch (Exception ex) {
            logger(ex.toString());
        }
        return text;
    }

    /*
    ================ Clear Text for EditText, Button, TextView ===============
    */
    public void clearText(View[] view) {
        for (View v : view) {
            if (v instanceof EditText) {
                ((EditText) v).setText("");
            } else if (v instanceof Button) {
                ((Button) v).setText("");
            } else if (v instanceof TextView) {
                ((TextView) v).setText("");
            }
        }
    }

    /*
    ================ Hide Keyboard from Screen ===============
    */
    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    /*
    ================ Show Keyboard to Screen ===============
    */
    public void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    /*
    ================ Hide & Show Views ===============
    */
    public void hideAndShowView(View[] views, View view) {
        for (int i = 0; i < views.length; i++) {
            views[i].setVisibility(View.GONE);
        }
        view.setVisibility(View.VISIBLE);
    }

    public void hideViews(View[] views) {
        for (int i = 0; i < views.length; i++) {
            views[i].setVisibility(View.GONE);
        }
    }


    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }


    public boolean isExternalMemoryAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = stat.getBlockSizeLong();
        }
        long availableBlocks = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            availableBlocks = stat.getAvailableBlocksLong();
        }
        return (availableBlocks * blockSize);
    }

    public long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = stat.getBlockSizeLong();
        } else {
            blockSize = stat.getBlockSize();
        }
        long totalBlocks = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            totalBlocks = stat.getBlockCountLong();
        } else {
            totalBlocks = stat.getBlockCount();
        }
        return (totalBlocks * blockSize);
    }

    public String getAvailableExternalMemorySize() {
        if (isExternalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                blockSize = stat.getBlockSizeLong();
            } else {
                blockSize = stat.getBlockSize();
            }
            long availableBlocks = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                availableBlocks = stat.getAvailableBlocksLong();
            } else {
                availableBlocks = stat.getAvailableBlocks();
            }
            return formatSize(availableBlocks * blockSize);
        } else {
            return "";
        }
    }

    public String getTotalExternalMemorySize() {
        if (isExternalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                blockSize = stat.getBlockSizeLong();
            } else {
                blockSize = stat.getBlockSize();
            }
            long totalBlocks = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                totalBlocks = stat.getBlockCountLong();
            } else {
                totalBlocks = stat.getBlockCount();
            }
            return formatSize(totalBlocks * blockSize);
        } else {
            return "";
        }
    }

    public static String formatSize(long size) {
        String suffix = null;
        if (size >= 1024) {
            suffix = "KB";
            size /= 1024;
            if (size >= 1024) {
                suffix = "MB";
                size /= 1024;
            }
        }
        StringBuilder resultBuffer = new StringBuilder(Long.toString(size));
        int commaOffset = resultBuffer.length() - 3;
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, ',');
            commaOffset -= 3;
        }
        if (suffix != null) resultBuffer.append(suffix);
        return resultBuffer.toString();
    }

    /*
   ================ Check File Size ===============
   */
    public long checkFileSize(String fileUrl) {
        long file_size = 0;
        try {
            URL url = new URL(fileUrl);
            URLConnection urlConnection = url.openConnection();
            urlConnection.connect();
            file_size = urlConnection.getContentLength();
        } catch (Exception ex) {
            logger(ex.toString());
        }
        return file_size;
    }

    public Uri ussdToCallableUri(String ussd) {

        String uriString = "";

        if (!ussd.startsWith("tel:"))
            uriString += "tel:";

        for (char c : ussd.toCharArray()) {

            if (c == '#')
                uriString += Uri.encode("#");
            else
                uriString += c;
        }

        return Uri.parse(uriString);
    }

    public String convertSecondsToHour(long seconds) {
        String second = String.valueOf(seconds % 60);
        String minute = String.valueOf((seconds / 60) % 60);
        String hour = String.valueOf((seconds / 60 / 60) % 60);
        if (second.length() < 2) {
            second = "0" + second;
        }
        if (minute.length() < 2) {
            minute = "0" + minute;
        }
        if (hour.length() < 2) {
            hour = "0" + hour;
        }
        return hour + ":" + minute + ":" + second;
    }


    public HashMap<String, Long> getNetworkInfo() {
        HashMap<String, Long> map = new HashMap<>();
        ActivityManager manager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = manager.getRunningAppProcesses();
        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(
                PackageManager.GET_META_DATA);
        for (ApplicationInfo packageInfo : packages) {
            //get the UID for the selected app
            if (packageInfo.packageName.equals(context.getPackageName())) {
                int uid = packageInfo.uid;
                long received = TrafficStats.getUidRxBytes(uid);
                long send = TrafficStats.getUidTxBytes(uid);
                map.put("send", send /*+ getDataUsage("totalSend")*/);
                map.put("received", received /*+ getDataUsage("totalReceived")*/);
//                writeDataUsage(send, received);
                Log.v("" + uid, "Send :" + send + ", Received :" + received);
                return map;
            }
        }
        map.put("Send", Long.parseLong("0"));
        map.put("Received", Long.parseLong("0"));
        return map;
    }

    /*
  =============== Check Version ===============
  */
    public boolean checkVersion(int versionCode) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            int currentCode = packageInfo.versionCode;
            return currentCode < versionCode;
        } catch (Exception ex) {
            return false;
        }
    }


    public void freeMemory() {
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }


    public HashMap<String, String> getDeviceInfo() {
        HashMap<String, String> map = new HashMap<>();
        map.put("Serial", Build.SERIAL);
        map.put("Model", Build.MODEL);
        //map.put("Id", Build.ID);
        map.put("Id", Build.SERIAL);
        map.put("Manufacture", Build.MANUFACTURER);
        map.put("Type", Build.TYPE);
        map.put("User", Build.USER);
        map.put("Base", String.valueOf(Build.VERSION_CODES.BASE));
        map.put("Incremental", Build.VERSION.INCREMENTAL);
        map.put("Board", Build.BOARD);
        map.put("Brand", Build.BRAND);
        map.put("Host", Build.HOST);
        map.put("Version Code", Build.VERSION.RELEASE);
        return map;
    }


    public void setNotificationdata(String token) {
        SharedPreferences sharedPref = context.getSharedPreferences("NOTIFICATION", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("NOTIFICATION_DATA", token);
        editor.commit();
    }

    public String getNotificationdata() {
        SharedPreferences sharedPref = context.getSharedPreferences("NOTIFICATION", Context.MODE_PRIVATE);
        return sharedPref.getString("NOTIFICATION_DATA", "");
    }

    public boolean isEmailValid(String email) {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        return matcher.matches();
    }

    public boolean isAdult(int year, int month, int day) {
        Calendar userAge = new GregorianCalendar(year, month, day);
        Calendar minAdultAge = new GregorianCalendar();
        minAdultAge.add(Calendar.YEAR, -18);
        return !minAdultAge.before(userAge);
    }

    public HashMap<String, Integer> getDateFromBday(String date) {
        HashMap<String, Integer> map = new HashMap<>();
        String month = date.substring(0, date.indexOf('/'));
        String day = date.substring(date.indexOf('/') + 1, date.lastIndexOf('/'));
        String year = date.substring(date.lastIndexOf('/') + 1);
        int d = day.charAt(0) == '0' ? Integer.parseInt(day.substring(1)) : Integer.parseInt(day);
        int m = month.charAt(0) == '0' ? Integer.parseInt(month.substring(1)) : Integer.parseInt(month);
        int y = Integer.parseInt(year);
        map.put("day", d);
        map.put("month", m - 1);
        map.put("year", y);
        return map;
    }

    public void preventScreenShot() {
        ((Activity) context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
    }

    public boolean isDeviceRooted() {
        return checkRootMethod1() || checkRootMethod2() || checkRootMethod3();
    }

    private boolean checkRootMethod1() {
        String buildTags = Build.TAGS;
        return buildTags != null && buildTags.contains("test-keys");
    }

    private boolean checkRootMethod2() {
        String[] paths = {"/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su",
                "/system/bin/failsafe/su", "/data/local/su", "/su/bin/su"};
        for (String path : paths) {
            if (new File(path).exists()) return true;
        }
        return false;
    }

    private static boolean checkRootMethod3() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[]{"/system/xbin/which", "su"});
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            return in.readLine() != null;
        } catch (Throwable t) {
            return false;
        } finally {
            if (process != null) process.destroy();
        }
    }

    public String loadJSONFromAsset(String filename) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("json/" + filename + ".json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public String makeFirstLetterUpperCase(String value) {
        if (value.length() == 0) {
            return "";
        }
        return value.substring(0, 1).toUpperCase() + value.substring(1);
    }

    public void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    /*
  ================ Check File Exists ===============
  */
    /*public boolean checkIfSongExists(String fileId, String fileType) {
        long file_size = 0;
        try {
            File directory = new File(context.getFilesDir(), context.getString(R.string.download_folder));
            if (!directory.exists()) {
                if (directory.mkdir()) {
                    logger("Directory Created");
                } else {
                    logger("Directory Not Created");
                }
            }
            File file = new File(directory.getAbsolutePath() + "/" + fileId + fileType);
            if (file.exists()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            logger(ex.toString());
            return false;
        }
    }*/


    /*public boolean deleteFile(String fileId, String fileType) {
        try {
            File directory = new File(context.getFilesDir(), context.getString(R.string.download_folder));
            File file = new File(directory.getAbsolutePath() + "/" + fileId + fileType);
            if (file.exists()) {
                if (file.delete()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        } catch (Exception ex) {
            return false;
        }
    }*/

    public String getFormattedDate(int year, int month, int day) {
        month = month + 1;
        String y = String.valueOf(year);
        String m = String.valueOf(month);
        String d = String.valueOf(day);
        if (m.length() == 1) m = "0" + m;
        if (d.length() == 1) d = "0" + d;
        return y + "-" + m + "-" + d;
    }

    public void showDialog(String message) {
        HashMap<String, Integer> screen = getScreenRes();
        int width = screen.get(KeyWord.SCREEN_WIDTH);
        int height = screen.get(KeyWord.SCREEN_HEIGHT);
        int mywidth = (width / 10) * 7;
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_toast);
        TextView tvMessage = dialog.findViewById(R.id.tv_message);
        Button btnOk = dialog.findViewById(R.id.btn_ok);
        tvMessage.setText(message);
        LinearLayout ll = dialog.findViewById(R.id.dialog_layout_size);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ll.getLayoutParams();
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        params.width = mywidth;
        ll.setLayoutParams(params);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    public boolean isFirstTimeLaunch() {
        SharedPreferences sharedPref = context.getSharedPreferences("FIRST_TIME", Context.MODE_PRIVATE);
        return sharedPref.getBoolean("firstTime", true);
    }

    public void disableFirstTimeLaunch() {
        SharedPreferences sharedPref = context.getSharedPreferences("FIRST_TIME", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("firstTime", false);
        editor.commit();
    }

    public Bitmap scaleBitmap(int resourceId) {
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), resourceId);
        int nh = (int) (largeIcon.getHeight() * (512.0 / largeIcon.getWidth()));
        return Bitmap.createScaledBitmap(largeIcon, 512, nh, true);
    }

    public double round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    public String millisToDate(long millis) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);

        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        String mHour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        String mMin = String.valueOf(calendar.get(Calendar.MINUTE));
        String mSec = String.valueOf(calendar.get(Calendar.SECOND));
        if (mHour.length() == 1) mHour = "0" + mHour;
        if (mMin.length() == 1) mMin = "0" + mMin;
        if (mSec.length() == 1) mSec = "0" + mSec;
        String date = getFormattedDate(mYear, mMonth, mDay) + " " + mHour + ":" + mMin + ":" + mSec;
        return date;
    }

    public String humanReadableDateTime(long times) {
        long minutes = times / 60000;
        String hours = String.valueOf(minutes / 60);
        if (hours.length() == 1) {
            hours = "0" + hours;
        }
        String mins = String.valueOf(minutes % 60);
        if (mins.length() == 1) {
            mins = "0" + mins;
        }
        String secs = String.valueOf((times / 1000) % 60);
        if (secs.length() == 1) {
            secs = "0" + secs;
        }
        String value = hours + ":" + mins + ":" + secs;
        return value;
    }

    public Spanned htmlDisplay(String message) {
        Spanned value = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            value = Html.fromHtml(message, Html.FROM_HTML_MODE_LEGACY);
        } else {
            value = Html.fromHtml(message);
        }
        return value;
    }

    public void focusView(View view) {
        final TransitionDrawable transition = (TransitionDrawable) view.getBackground();
        transition.startTransition(500);
        new CountDownTimer(500, 500) {

            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                transition.reverseTransition(500);
            }
        }.start();
    }

    public RequestOptions Glide_Cache_On() {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(R.drawable.ic_no_photos);
        return requestOptions;
    }

    public RequestOptions Glide_Cache_Off() {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.ic_loading);
        requestOptions.error(R.drawable.ic_no_photos);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.skipMemoryCache(true);
        return requestOptions;
    }

    /*public RequestOptions Glide_Cache_On_profile() {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.ic_loading);
        requestOptions.error(R.drawable.ic_user2);
        return requestOptions;
    }*/

    public void setSize(ViewGroup.LayoutParams params, int myWidth, int myHeight) {
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        if (myWidth != 0) {
            int width = size.x;
            params.width = (width / 10) * myWidth;
        }
        if (myHeight != 0) {
            int height = size.y;
            params.height = (height / 10) * myHeight;
        }
    }

    public int getAppVersionCode() {
        int versionCode = 1;
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionCode = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }
}
