package ru.kamuzta.bigfilearchiver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;

public class FileItem {
    private Path path;
    private String fileName;
    private String fileType;
    private long size;
    private Instant creationTime;
    private Instant lastAccessTime;

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

    public Instant getCreationTime() {
        return creationTime;
    }

    public Instant getLastAccessTime() {
        return lastAccessTime;
    }

    public FileItem(Path path) throws Exception {
        if (Files.isRegularFile(path)) {
            this.path = path;
            this.fileName = path.getFileName().toString();
            this.fileType = this.fileName.substring(this.fileName.lastIndexOf(".") + 1);
            try {
                this.size = Files.isRegularFile(path) ? Files.size(path) : 0L;
                BasicFileAttributeView bfv = Files.getFileAttributeView(path, BasicFileAttributeView.class);
                BasicFileAttributes bfa = bfv.readAttributes();
                this.creationTime = Instant.ofEpochMilli(bfa.creationTime().toMillis());
                this.lastAccessTime = Instant.ofEpochMilli(bfa.lastAccessTime().toMillis());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new Exception();
        }
    }

    @Override
    public String toString() {
        return  "Path: " + path + "\n" +
                "FileName: " + fileName + "\n" +
                "FileType: " + fileType + "\n" +
                "Size: " + size + " bytes\n" +
                "Created: " + creationTime + "\n" +
                "LastAccess: " + lastAccessTime + "\n";
    }
}
