package requests;

import command_models.Authorization;
import command_models.Message;
import command_models.Registration;
import commands.*;
import tools.Separator;

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
                    yield new RequestAuthorization(args[1]);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            //case "REGISTER" -> new RequestRegistration(new Registration(args[1], args[2]));
            //case "MESSAGE" -> new  RequestMessage(new Message(args[1], args[2], args[3], args[4]));
            default -> null;
        };

        return req;
    }

}

