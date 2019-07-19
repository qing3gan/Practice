package util;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class FileCopy {

    public static void main(String[] args) throws IOException {
        //src,target
        if (args.length == 2) {
            String src = args[0];
            String target = args[1];
            Files.walkFileTree(Paths.get(target), new SimpleFileVisitor<Path>(){

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if(!file.getFileName().toString().endsWith("png")){
                        Files.delete(file);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
            Files.walkFileTree(Paths.get(src), new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                    if ("下架".equals(dir.getFileName().toString())) {
                        return FileVisitResult.SKIP_SUBTREE;
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    String parent = file.getParent().getFileName().toString();
                    if (file.getFileName().toString().startsWith(parent)) {
                        Files.copy(file, Paths.get(target, file.getFileName().toString()), StandardCopyOption.REPLACE_EXISTING);
                        return FileVisitResult.SKIP_SIBLINGS;
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } else {
            System.out.println("参数错误！请输入源目录和目标目录，空格隔开。");
        }
    }
}
