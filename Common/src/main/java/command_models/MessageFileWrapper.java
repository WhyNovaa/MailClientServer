package command_models;

import tools.Separator;

public class MessageFileWrapper {
    private String from;
    private String to;
    private String fileName;
    private String fileContent;

    public MessageFileWrapper(String fileName, String from, String to, String fileContent) {
        this.fileName = fileName;
        this.fileContent = fileContent;
        this.to = to;
        this.from = from;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileContent() {
        return fileContent;
    }
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageFileWrapper file = (MessageFileWrapper) o;

        return this.fileName.equals(file.fileName) && this.fileContent.equals(file.fileContent)
                && this.from.equals(file.from)
                && this.to.equals(file.to);
    }

    public static MessageFileWrapper deserializeFromStr(String str){
        String[] arr = str.split(Separator.SEPARATOR);
        return new MessageFileWrapper(arr[0],arr[1],arr[2],arr[3]);
    }

    public String serializeToStr() {
        return this.fileName + Separator.SEPARATOR + this.from
                + Separator.SEPARATOR + this.to + Separator.SEPARATOR + this.fileContent;
    }
}

