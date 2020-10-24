package ru.kamuzta.bigfilearchiver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.AclEntry;
import java.nio.file.attribute.AclFileAttributeView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public class BigFileArchiver {


    public static void main(String[] args) {
        Path file = Paths.get("F:/javaprojects/files");
        FileItem fileItem;
        try {
            fileItem = new FileItem(file);
        } catch (Exception e) {
            System.out.println("Path is not a Regular File");
        }
        /*BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String dirName = br.readLine();
        br.close();
        Path dirPath = Paths.get(dirName);
        if (!Files.isDirectory(dirPath)) {
            System.out.println(dirPath.normalize().toString() + " - не папка");
        } else {
            //SearchFileVisitor sfv = new SearchFileVisitor();
            //Files.walkFileTree(dirPath, sfv);
            List<Item> itemList = getFileTree(dirName);
            System.out.println("Всего папок - " + getDirCount(itemList));
            System.out.println("Всего файлов - " + getFileCount(itemList));
            System.out.println("Общий размер - " + getFullSize(itemList));
        }*/
    }

/*
    public static List<Item> getFileTree(String root) throws IOException {
        List<Item> fileTree = new ArrayList<Item>();
        PriorityQueue<Path> queue = new PriorityQueue<Path>();
        queue.add(Paths.get(root));
        while (!queue.isEmpty()) {
            Path currentDir = queue.peek();
            DirectoryStream<Path> stream = Files.newDirectoryStream(currentDir);
            for (Path file : stream) {
                if (!Files.isDirectory(file)) {
                    fileTree.add(new Item(file.toAbsolutePath()));
                } else  if (Files.isDirectory(file)){
                    fileTree.add(new Item(file.toAbsolutePath()));
                    queue.offer(file);
                }
            }
            queue.poll();
        }
        return fileTree;
    }

    public static int getDirCount(List<Item> itemList) {
        int countDir = 0;
        for (Item item : itemList) {
            if (!item.isFile) {
                countDir++;
            }
        }
        return countDir;
    }

    public static int getFileCount(List<Item> itemList) {
        int count = 0;
        for (Item item : itemList) {
            if (item.isFile) {
                count++;
            }
        }
        return count;
    }

    public static long getFullSize(List<Item> itemList) {
        long fullSize = 0;
        for (Item item : itemList) {
            if (item.isFile) {
                fullSize += item.size;
            }
        }
        return fullSize;
    }

    public static class SearchFileVisitor extends SimpleFileVisitor<Path> {
        private static List<Item> foundPaths = new ArrayList<Item>();
        public List<Item> getFoundPaths() {
            return foundPaths;
        }

        public int getDirCount() {
            int count = 0;
            for (Item item : foundPaths) {
                if (!item.isFile) {
                    count++;
                }
            }
            return count;
        }

        public int getFileCount() {
            int count = 0;
            for (Item item : foundPaths) {
                if (item.isFile) {
                    count++;
                }
            }
            return count;
        }

        public long getFullSize() {
            long fullSize = 0;
            for (Item item : foundPaths) {
                if (item.isFile) {
                    fullSize += item.size;
                }
            }
            return fullSize;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
            foundPaths.add(new Item(file));
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) {
            return FileVisitResult.SKIP_SUBTREE;
        }
    }

 */
}

