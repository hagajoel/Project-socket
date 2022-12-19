package log;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class LogUtil {
    private static final String FILE_LOG = "log/logs.txt";
    private static List<String> logs = new LinkedList<String>();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    

    public static void write(String log) {
        write(log, true);
    }

    public static void write(String log, boolean print) {
        String message = sdf.format(new Date()) + " " + log;
        logs.add(message);

        if(print) {
            System.out.println(message);
        }
    }
    

    public static void save(boolean append) {
        try {
            if (logs!=null && logs.size()>0) {

                FileWriter fileWriterLog = new FileWriter(FILE_LOG, append);

                BufferedWriter bufferedWriterLog = new BufferedWriter(fileWriterLog);

                for (String str : logs) {
                    bufferedWriterLog.write(str);
                    bufferedWriterLog.newLine();
                }

                bufferedWriterLog.close();
            }
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + FILE_LOG + "'");
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void clear() {
        logs.clear();
    }
}
