package requests;

import command_models.MessageFileWrapper;
import command_models.Message;
import tools.Separator;

import java.util.ArrayList;

public abstract class Request {
    private RequestType type;

    public Request(RequestType type) {
        this.type = type;
    }

    public RequestType getType() {
        return type;
    }


    public abstract boolean equals(Object o);

    public abstract String serializeToStr();

    public static Request deserializeFromStr(String str) throws Exception {
        String[] args = str.split(Separator.SEPARATOR);
        String type = args[0];
        Request req = switch (type) {
            case "LOGIN" -> {
                try {
                    yield new RequestAuthorization(args[1], args[2]);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            case "REGISTER" -> {
                try {
                    yield new RequestRegistration(args[1]);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            case "GET_MESSAGE" -> {
                ArrayList<Message> messages = new ArrayList<>();
                for (int i = 1; i < args.length; i += 4) {
                    String subject = (i < args.length) ? args[i] : "";
                    String from = (i + 1 < args.length) ? args[i + 1] : "";
                    String to = (i + 2 < args.length) ? args[i + 2] : "";
                    String body = (i + 3 < args.length) ? args[i + 3] : "";
                    messages.add(new Message(subject, from, to, body));
                }
                yield new RequestGetMessage(messages);
            }
            case "GET_FILE" -> {
                ArrayList<MessageFileWrapper> files = new ArrayList<>();
                for (int i = 1; i < args.length; i += 4) {
                    String filename = (i < args.length) ? args[i] : "";
                    String from = (i + 1 < args.length) ? args[i + 1] : "";
                    String to = (i + 2 < args.length) ? args[i + 2] : "";
                    String fileContent = (i + 3 < args.length) ? args[i + 3] : "";
                    files.add(new MessageFileWrapper(filename, from, to, fileContent));
                }
                yield new RequestGetFile(files);
            }
            case "SEND_MESSAGE" -> new RequestSendMessage(args[1]);
            default -> null;
        };

        return req;
    }

}

