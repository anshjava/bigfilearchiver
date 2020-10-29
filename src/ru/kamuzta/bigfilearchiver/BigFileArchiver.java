package ru.kamuzta.bigfilearchiver;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class BigFileArchiver {
    private static SearchFileVisitor searchFileVisitor;
    private static Path dirPath;
    private static Path logFile;
    private static FileWriter fwLog;
    private static LocalDate accessDate;
    private static int fileSize;
    private static List<String> fileTypesList = new ArrayList<String>();
    private static int totalFilesInDir = 0;
    private static int filesToArchive = 0;
    private static int filesWasArchived = 0;

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

        totalFilesInDir = searchFileVisitor.getFoundItems().size();
        writeLogfile(fwLog, "Total files in search directory: " + totalFilesInDir);

        for (FileItem item : searchFileVisitor.getFoundItems()) {
            if (fileTypesList.contains(item.getFileType()) && fileSize < item.getSize() && accessDate.isAfter(item.getLastAccessTime()) ) {
                filesToArchive++;
            }
        }
        writeLogfile(fwLog, "Number of files that match the criteria: " + filesToArchive);
        System.out.print(filesToArchive + " files was founded. Archive & delete them?[Y/N]: ");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String answer = "";
        try { answer = br.readLine(); } catch (IOException ioe) { ioe.printStackTrace(); }

        if (!answer.equals("Y") && !answer.equals("y") ) {
            writeLogfile(fwLog, "Process aborted by user");
            System.exit(0);
        } else {
            System.out.println("ARCHIVING STARTED!");
            archiveFiles(searchFileVisitor.getFoundItems());
        }

        //close log-file
        try { fwLog.close(); } catch (IOException ioe) { ioe.printStackTrace(); }

    }

    private static void archiveFiles(List<FileItem> items) {
        for (FileItem item : items) {
            try {
                if (fileTypesList.contains(item.getFileType()) && fileSize < item.getSize() && accessDate.isAfter(item.getLastAccessTime())) {
                    FileOutputStream zipFile = new FileOutputStream(item.getPath() + ".zip");
                    ZipOutputStream zip = new ZipOutputStream(zipFile);
                    zip.putNextEntry(new ZipEntry(item.getFileName()));
                    Files.copy(item.getPath(), zip);
                    zip.close();
                    System.out.println("achived: " + item.getPath());
                    writeLogfile(fwLog, "achived: " + item.getPath());
                    filesWasArchived++;
                }
            } catch (IOException ioe) {
                System.out.println("error: " + item.getPath());
                writeLogfile(fwLog, "error: " + item.getPath());
            }
        }
        writeLogfile(fwLog, filesWasArchived + " files was archived!");
    }

    private static class SearchFileVisitor extends SimpleFileVisitor<Path> {
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

    private static void writeLogfile(FileWriter fwLog, String input) {
        try {
            fwLog.write(input + "\n");
            fwLog.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private static void getOptions() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.print("Enter the start directory address: ");
            dirPath = Paths.get(br.readLine());

            System.out.print("Enter filetype(separated by spaces): ");
            fileTypesList = Arrays.asList(br.readLine().split(" "));

            System.out.print("Enter the date(dd.MM.yyyy): ");
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            accessDate = LocalDate.parse(br.readLine(), dtf);

            System.out.print("Enter the minimum file size in bytes: ");
            fileSize = Integer.parseInt(br.readLine());

            try {
                logFile = Files.createFile(dirPath.resolve("BFA_log.log"));
            } catch (FileAlreadyExistsException e) {
                logFile = dirPath.resolve("BFA_log.log");
            }

            fwLog = new FileWriter(logFile.toFile());

            writeLogfile(fwLog, "Program started: " + LocalDate.now().format(dtf));
            writeLogfile(fwLog, "File types: " + fileTypesList.toString());
            writeLogfile(fwLog, "Minimum file size in bytes: " + fileSize);
            writeLogfile(fwLog, "Last access date: " + accessDate.format(dtf));
            writeLogfile(fwLog, "\n");

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}