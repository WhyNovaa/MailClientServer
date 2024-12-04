package test;

import command_models.FileWrapper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.Base64;

public class FileUtil {
    public static FileWrapper encodeFileToBase64(String filePathStr, String login, String to) throws IOException {
        Path filePath = Paths.get(filePathStr);
        byte[] fileBytes = Files.readAllBytes(filePath);
        String fileContent = Base64.getEncoder().encodeToString(fileBytes);
        return new FileWrapper(filePath.getFileName().toString(),login,to, fileContent);
    }

    public static void decodeBase64ToFile(FileWrapper fileWrapper, String targetDirectoryStr) throws IOException {
        byte[] fileBytes = Base64.getDecoder().decode(fileWrapper.getFileContent());
        Path targetDirectory = Paths.get(targetDirectoryStr);
        Path targetFilePath = targetDirectory.resolve(fileWrapper.getFileName());
        Files.write(targetFilePath, fileBytes);
    }
}

