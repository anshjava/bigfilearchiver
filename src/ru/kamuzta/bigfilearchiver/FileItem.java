package ru.kamuzta.bigfilearchiver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class FileItem {
    private Path path;
    private String directoryName;
    private String fileName;
    private String fileType;
    private long size;
    private LocalDate creationTime;
    private LocalDate lastAccessTime;

    public String getDirectoryName() {
        return directoryName;
    }

    public Path getPath() {
        return path;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public long getSize() {
        return size;
    }

    public LocalDate getCreationTime() {
        return creationTime;
    }

    public LocalDate getLastAccessTime() {
        return lastAccessTime;
    }

    public FileItem(Path path) throws Exception {
        if (Files.isRegularFile(path)) {
            this.path = path;
            this.fileName = path.getFileName().toString();
            this.directoryName = path.getParent().toString();
            if (this.fileName.contains(".")) {
                this.fileType = this.fileName.substring(this.fileName.lastIndexOf('.') + 1);

            } else {
                this.fileType = "unknown";
            }
            try {
                this.size = Files.isRegularFile(path) ? Files.size(path) : 0L;
                BasicFileAttributeView bfv = Files.getFileAttributeView(path, BasicFileAttributeView.class);
                BasicFileAttributes bfa = bfv.readAttributes();
                this.creationTime = LocalDate.from(Instant.ofEpochMilli(bfa.creationTime().toMillis()).atZone(ZoneId.systemDefault()).toLocalDate());
                this.lastAccessTime = LocalDate.from(Instant.ofEpochMilli(bfa.lastAccessTime().toMillis()).atZone(ZoneId.systemDefault()).toLocalDate());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new Exception();
        }
    }

    @Override
    public String toString() {
        return  "Directory: " + directoryName + " | " +
                "FileName: " + fileName + " | " +
                "FileType: " + fileType + " | " +
                "Size: " + size + " bytes | " +
                "Created: " + creationTime + " | " +
                "LastAccess: " + lastAccessTime;
    }
}
