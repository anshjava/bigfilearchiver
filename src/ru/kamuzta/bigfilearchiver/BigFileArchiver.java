package ru.kamuzta.bigfilearchiver;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class BigFileArchiver {
    private static SearchFileVisitor searchFileVisitor;
    private static Path dirPath;
    private static Path logFile;
    private static FileWriter fwLog;
    private static LocalDate accessDate;
    private static int fileSize;
    private static List<String> fileTypesList = new ArrayList<String>();

    public static void main(String[] args) {
        getOptions(); //configuring program

        //Collecting all files
        try {
            searchFileVisitor = new SearchFileVisitor();
            Files.walkFileTree(dirPath, searchFileVisitor);
        } catch (AccessDeniedException ade) {
            writeLogfile(fwLog, "Access denied: " + ade.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        writeLogfile(fwLog, "Total files in search directory: " + searchFileVisitor.getFoundItems().size());

        //What to do with founded files?
        for (FileItem item : searchFileVisitor.getFoundItems()) {
            if (fileTypesList.contains(item.getFileType()) && fileSize < item.getSize() && accessDate.isAfter(item.getLastAccessTime()) ) {
                System.out.println(item);
                writeLogfile(fwLog, item.toString());
            }
        }

        //close log-file
        try { fwLog.close(); } catch (IOException ioe) { ioe.printStackTrace(); }

    }

    public static class SearchFileVisitor extends SimpleFileVisitor<Path> {
        private static List<FileItem> foundItems = new ArrayList<FileItem>();

        public List<FileItem> getFoundItems() {
            return foundItems;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
            if (Files.isRegularFile(file)) {
                try {
                    foundItems.add(new FileItem(file));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return FileVisitResult.CONTINUE;
        }

    }

    public static void writeLogfile(FileWriter fwLog, String input) {
        try {
            fwLog.write(input + "\n");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static void getOptions() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.print("Enter the start directory address: ");
            dirPath = Paths.get(br.readLine());

            System.out.print("Enter filetype(separated by spaces): ");
            fileTypesList = Arrays.asList(br.readLine().split(" "));

            System.out.print("Enter the date(dd.MM.yyyy): ");
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            accessDate = LocalDate.parse(br.readLine(), dtf);

            System.out.print("Enter the minimum file size in Mb: ");
            fileSize = Integer.parseInt(br.readLine());

            logFile = Files.createFile(dirPath.resolve("BFA_log.log"));
            fwLog = new FileWriter(logFile.toFile());

            writeLogfile(fwLog, "Program started: " + Instant.now().toString());
            writeLogfile(fwLog, "File types: " + fileTypesList.toString());
            writeLogfile(fwLog, "Minimum file size in Mb: " + fileSize);
            writeLogfile(fwLog, "Last access date: " + accessDate.format(dtf));
            writeLogfile(fwLog, "\n");

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}