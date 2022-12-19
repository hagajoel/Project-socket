package server;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import log.LogUtil;

public class HttpUtils extends Thread{
    Socket socket;
    String clientRequest;

    public HttpUtils(String req, Socket s){
        socket = s;
        clientRequest = req;
    }


    public void run(){
        try{
            // Clear list each time for handling new request
            LogUtil.clear();

            PrintStream printer = new PrintStream(socket.getOutputStream());

            LogUtil.write("");
            LogUtil.write("Http Worker is working...");
            LogUtil.write(clientRequest);

            if (!clientRequest.startsWith("GET") || clientRequest.length() < 14 ||
                    !(clientRequest.endsWith("HTTP/1.0") || clientRequest.endsWith("HTTP/1.1"))) {
                // bad request
                LogUtil.write("400(Bad Request): " + clientRequest);
                String errorPage = buildErrorPage("400", "Bad Request", "Your browser sent a request that this server could not understand.");
                printer.println(errorPage);
            }
            else {
                String req = clientRequest.substring(4, clientRequest.length()-9).trim();
                if (req.indexOf("..") > -1 || req.indexOf("/.ht") > -1 || req.endsWith("~")) {
                    // hack attack
                    LogUtil.write("403(Forbidden): " + req);
                    String errorPage = buildErrorPage("403", "Forbidden", "You don't have permission to access the requested URL " + req);
                    printer.println(errorPage);
                }
                else {
                    req = URLDecoder.decode(req, "UTF-8");
                    // Remove the last slash if exists
                    if (req.endsWith("/")) {
                        req = req.substring(0, req.length() - 1);
                    }
                    // Handle requests
                    if (req.indexOf(".")>-1) { // Request for single file
                        if (req.indexOf(".fake-cgi")>-1) { // CGI request
                            LogUtil.write("> This is a [CGI] request..");
                            handleCGIRequest(req, printer);
                        }
                        else { // Single file request
                            if (!req.startsWith("/img/")&&!req.startsWith("/favicon.ico")) {
                                LogUtil.write("> This is a [SINGLE FILE] request..");
                            }
                            handleFileRequest(req, printer);
                        }
                    }
                    else { // Request for directory
                        LogUtil.write("> This is a [DIRECTORY EXPLORE] request..");
                        handleExploreRequest(req, printer);
                    }
                }
            }
            // Save logs to file
            LogUtil.save(true);
            socket.close();
        }
        catch(IOException ex){
            // Handle the exception
            System.out.println(ex);
        }
    }

    private void handleCGIRequest(String req, PrintStream printer) throws UnsupportedEncodingException {
        // Parse the url to key-value pair
        Map<String, String> params = parseUrlParams(req);

        // Try to convert num1 and num2 to integer
        Integer number1 = tryParse(params.get("num1"));
        Integer number2 = tryParse(params.get("num2"));

        // Validate the input params
        if (number1 == null || number2 == null) {
            String errormsg = "Invalid parameter: " + params.get("num1") + " or " + params.get("num2") + ", both must be integer!";
            LogUtil.write(">> " + errormsg);
            String errorPage = buildErrorPage("500", "Internal Server Error", errormsg);
            printer.println(errorPage);
        }
        else {
            LogUtil.write(">> " + number1 + " + " + number2 + " = " + (number1+number2));
            StringBuilder sbContent = new StringBuilder();
            sbContent.append(params.get("person") + ", the sum of ");
            sbContent.append(params.get("num1") + " and " + params.get("num2") + " is ");
            sbContent.append(number1+number2);
            sbContent.append(".");
            String htmlPage = buildHtmlPage(sbContent.toString(), "Fake-CGI: Add Two Numbers");
            String htmlHeader = buildHttpHeader("aa.html", htmlPage.length());
            printer.println(htmlHeader);
            printer.println(htmlPage);
        }
    }

    private void handleFileRequest(String req, PrintStream printer) throws FileNotFoundException, IOException {
        // Get the root folder of the webserver
        String rootDir = getRootFolder();
        // Get the real file path
        String path = Paths.get(rootDir, req).toString();
        // Try to open the file
        File file = new File(path);
        if (!file.exists() || !file.isFile()) { // If not exists or not a file
            printer.println("No such resource:" + req);
            LogUtil.write(">> No such resource:" + req);
        }
        else { // It's a file
            if (!req.startsWith("/src/img/")&&!req.startsWith("/favicon.ico")) {
                LogUtil.write(">> Seek the content of file: " + file.getName());
            }
            // Print header
            String htmlHeader = buildHttpHeader(path, file.length());
            printer.println(htmlHeader);

            // Open file to input stream
            InputStream fs = new FileInputStream(file);
            byte[] buffer = new byte[1000];
            while (fs.available()>0) {
                printer.write(buffer, 0, fs.read(buffer));
            }
            fs.close();
        }
    }


    private void handleExploreRequest(String req, PrintStream printer) {
        // Get the root folder of the webserver
        String rootDir = getRootFolder();
        // Get the real file path
        String path = Paths.get(rootDir, req).toString();
        // Try to open the directory
        File file = new File (path) ;
        if (!file.exists()) { // If the directory does not exist
            printer.println("No such resource:" + req);
            LogUtil.write(">> No such resource:" + req);
        }
        else { // If exists
            LogUtil.write(">> Explore the content under folder: " + file.getName());
            // Get all the files and directory under current directory
            File[] files = file.listFiles();
            Arrays.sort(files);
            
            // Build file/directory structure in html format
            StringBuilder sbDirHtml = new StringBuilder();
            // Title line
            sbDirHtml.append("<table>");
            sbDirHtml.append("<tr>");
            sbDirHtml.append("  <th>Name</th>");
            sbDirHtml.append("  <th>Last Modified</th>");
            sbDirHtml.append("  <th>Size(Bytes)</th>");
            sbDirHtml.append("</tr>");

            // Parent folder, show it if current directory is not root
            if (!path.equals(rootDir)) {
                String parent = path.substring(0, path.lastIndexOf(File.separator));
                if (parent.equals(rootDir)) { // The first level
                    parent = "../";
                }
                else { // The second or deeper levels
                    parent = parent.replace(rootDir, "");
                }
                // Replace backslash to slash
                parent = parent.replace("\\", "/");
                // Parent line
                sbDirHtml.append("<tr>");
                sbDirHtml.append("  <td><img src=\""+buildImageLink(req,"src/img/folder.png")+"\"></img><a href=\"" + parent +"\">../</a></td>");
                sbDirHtml.append("  <td></td>");
                sbDirHtml.append("  <td></td>");
                sbDirHtml.append("</tr>");
            }

            // Build lines for directories
            List<File> folders = getFileByType(files, true);
            for (File folder: folders) {
                LogUtil.write(">>> Directory: " + folder.getName());
                sbDirHtml.append("<tr>");
                sbDirHtml.append("  <td><img src=\""+buildImageLink(req,"src/img/folder.png")+"\"></img><a href=\""+buildRelativeLink(req, folder.getName())+"\">"+folder.getName()+"</a></td>");
                sbDirHtml.append("  <td>" + getFormattedDate(folder.lastModified()) + "</td>");
                sbDirHtml.append("  <td></td>");
                sbDirHtml.append("</tr>");
            }
            // Build lines for files
            List<File> fileList = getFileByType(files, false);
            for (File f: fileList) {
                LogUtil.write(">>> File: " + f.getName());
                sbDirHtml.append("<tr>");
                sbDirHtml.append("  <td><img src=\""+buildImageLink(req, getFileImage(f.getName()))+"\" width=\"16\"></img><a href=\""+buildRelativeLink(req, f.getName())+"\">"+f.getName()+"</a></td>");
                sbDirHtml.append("  <td>" + getFormattedDate(f.lastModified()) + "</td>");
                sbDirHtml.append("  <td>" + f.length() + "</td>");
                sbDirHtml.append("</tr>");
            }

            sbDirHtml.append("</table>");
            String htmlPage = buildHtmlPage(sbDirHtml.toString(), "");
            String htmlHeader = buildHttpHeader(path, htmlPage.length());
            printer.println(htmlHeader);
            printer.println(htmlPage);
        }
    }

    private String buildHttpHeader(String path, long length) {
        StringBuilder sbHtml = new StringBuilder();
        sbHtml.append("HTTP/1.1 200 OK");
        sbHtml.append("\r\n");
        sbHtml.append("Content-Length: " + length);
        sbHtml.append("\r\n");
        sbHtml.append("Content-Type: "+ getContentType(path));
        sbHtml.append("\r\n");
        return sbHtml.toString();
    }


    private String buildHtmlPage(String content, String header) {
        StringBuilder sbHtml = new StringBuilder();
        sbHtml.append("<!DOCTYPE html>");
        sbHtml.append("<html>");
        sbHtml.append("<head>");
        sbHtml.append("<link href='/style/style.css'></link>");
        sbHtml.append("<title>My Web Server</title>");
        sbHtml.append("</head>");
        sbHtml.append("<body>");
        if (header != null && !header.isEmpty()) {
            sbHtml.append("<h1>" + header + "</h1>");
        }
        else {
            sbHtml.append("<h1>File Explorer in Web Server </h1>");
        }
        sbHtml.append(content);
        sbHtml.append("<hr>");
        sbHtml.append("<p>*This page is returned by Web Server.</p>");
        sbHtml.append("</body>");
        sbHtml.append("</html>");
        return sbHtml.toString();
    }

    private String buildErrorPage(String code, String title, String msg) {
        StringBuilder sbHtml = new StringBuilder();
        sbHtml.append("HTTP/1.1 " + code + " " + title + "\r\n\r\n");
        sbHtml.append("<!DOCTYPE html>");
        sbHtml.append("<html>");
        sbHtml.append("<link href='/style/style.css'></link>");
        sbHtml.append("<head>");
        sbHtml.append("<title>" + code + " " + title + "</title>");
        sbHtml.append("</head>");
        sbHtml.append("<body>");
        sbHtml.append("<h1>" + code + " " + title + "</h1>");
        sbHtml.append("<p>" + msg + "</p>");
        sbHtml.append("<hr>");
        sbHtml.append("</body>");
        sbHtml.append("</html>");
        return sbHtml.toString();
    }

    private List<File> getFileByType(File[] filelist, boolean isfolder) {
        List<File> files = new ArrayList<File>();
        if (filelist == null || filelist.length == 0) {
            return files;
        }

        for (int i = 0; i < filelist.length; i++) {
            if (filelist[i].isDirectory() && isfolder) {
                files.add(filelist[i]);
            }
            else if (filelist[i].isFile() && !isfolder) {
                files.add(filelist[i]);
            }
        }
        return files;
    }

    private Map<String, String> parseUrlParams(String url) throws UnsupportedEncodingException {
        HashMap<String, String> mapParams = new HashMap<String, String>();
        if (url.indexOf("?") < 0) {
            return mapParams;
        }

        url = url.substring(url.indexOf("?") + 1);
        String[] pairs = url.split("&");
        for (String pair : pairs) {
            int index = pair.indexOf("=");
            mapParams.put(URLDecoder.decode(pair.substring(0, index), "UTF-8"), URLDecoder.decode(pair.substring(index + 1), "UTF-8"));
        }
        return mapParams;
    }

    private String getRootFolder() {
        String root = "";
        try{
            File f = new File(".");
            root = f.getCanonicalPath();
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
        return root;
    }

    private String getFormattedDate(long lastmodified) {
        if (lastmodified < 0) {
            return "";
        }

        Date lm = new Date(lastmodified);
        String lasmod = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lm);
        return lasmod;
    }

    private String buildRelativeLink(String req, String filename) {
        if (req == null || req.equals("") || req.equals("/")) {
            return filename;
        }
        else {
            return req + "/" +filename;
        }
    }


    private String buildImageLink(String req, String filename) {
        if (req == null || req.equals("") || req.equals("/")) {
            return filename;
        }
        else {
            String imageLink = filename;
            for(int i = 0; i < req.length(); i++) {
                if (req.charAt(i) == '/') {
                    imageLink = "../" + imageLink;
                }
            }
            return imageLink;
        }
    }

    private static String getFileImage(String path) {
        if (path == null || path.equals("") || path.lastIndexOf(".") < 0) {
            return "src/img/page.png";
        }

        String extension = path.substring(path.lastIndexOf("."));
        switch(extension) {
            case ".class":
                return "src/img/page_white_c.png";
            case ".html":
                return "src/img/page_white_code.png";
            case ".java":
                return "src/img/page_white_cup.png";
            case ".txt":
                return "src/img/page_white_text.png";
            case ".xml":
                return "src/img/page_white_code_red.png";
            case ".bat":
                return "src/img/application_xp_terminal.png";
            case ".php":
                return "src/img/page_white_php.png";
            case ".png":
                return "src/img/image.png";
            case ".jpg":
                return "src/img/image.png";
            case ".jpeg":
                return "src/img/image.png";
            default:
                return "src/img/page.png";
        }
    }

    private static String getContentType(String path) {
        if (path == null || path.equals("") || path.lastIndexOf(".") < 0) {
            return "text/html";
        }

        String extension = path.substring(path.lastIndexOf("."));
        switch(extension) {
            case ".html":
            case ".htm":
                return "text/html";
            case ".txt":
                return "text/plain";
            case ".ico":
                return "src/img/favicon.ico";
            case ".wml":
                return "text/html";
            default:
                return "text/plain";
        }
    }

    private Integer tryParse(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
