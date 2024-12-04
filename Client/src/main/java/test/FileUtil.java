package test;

import command_models.MessageFileWrapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class FileUtil {
    public static MessageFileWrapper encodeFileToBase64(String filePathStr, String login, String to) throws IOException {
        Path filePath = Paths.get(filePathStr);
        byte[] fileBytes = Files.readAllBytes(filePath);
        String fileContent = Base64.getEncoder().encodeToString(fileBytes);
        return new MessageFileWrapper(filePath.getFileName().toString(), login, to, fileContent);
    }

    public static void decodeBase64ToFile(MessageFileWrapper messageFileWrapper, String targetDirectoryStr) throws IOException {
        byte[] fileBytes = Base64.getDecoder().decode(messageFileWrapper.getFileContent());
        Path targetDirectory = Paths.get(targetDirectoryStr).toAbsolutePath();
        Path targetFilePath = targetDirectory.resolve(messageFileWrapper.getFileName());
        Files.write(targetFilePath, fileBytes);
    }
}

