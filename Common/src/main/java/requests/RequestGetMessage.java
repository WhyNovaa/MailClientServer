package requests;

import command_models.Message;
import tools.Separator;

import java.util.ArrayList;
import java.util.Objects;

public class RequestGetMessage extends Request {
    private ArrayList<Message> messages;

    public RequestGetMessage(ArrayList<Message> mes) {
        super(RequestType.GET_MESSAGE);
        this.messages = mes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestGetMessage req = (RequestGetMessage) o;

        return Objects.equals(this.messages, req.messages)
                && Objects.equals(this.getType(), req.getType());
    }

    @Override
    public String serializeToStr() {
        String str = this.getType().toString();
        for (int i = 0; i < messages.size(); i++) {
            str += Separator.SEPARATOR + messages.get(i).serializeToStr();
        }
        return str;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }
}
