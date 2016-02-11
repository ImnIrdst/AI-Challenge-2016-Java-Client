package client.utils;


/**
 * Created by al on 2/11/16.
 */
public class Logger {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";
    private static boolean mIsLogEnabled = true;

    public static void setLogEnabled() {
        mIsLogEnabled = true;
    }

    public static void setLogDisabled(){
        mIsLogEnabled = false;
    }

    public static void log(String title, Object msg, boolean isNeeded) {
        if (isNeeded && mIsLogEnabled)
            System.out.println(ANSI_CYAN + title + ANSI_CYAN + ": " + ANSI_WHITE + msg + ANSI_WHITE);
    }

    public static void warn(Object msg){
        if(mIsLogEnabled)
            System.out.println(ANSI_YELLOW + "warning: " + ANSI_YELLOW + ANSI_BLACK + msg + ANSI_BLACK);
    }
}
