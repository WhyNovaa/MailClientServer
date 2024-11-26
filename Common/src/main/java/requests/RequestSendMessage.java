package requests;

import tools.Separator;

import java.util.Objects;

public class RequestSendMessage extends Request{
    Boolean sent;

    RequestSendMessage(String str) throws Exception {
        super(RequestType.SEND_MESSAGE);
        try{
            this.sent = check(str);
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    Boolean check(String str)throws Exception{
        if (str.equals("sent")) return true;
        if (str.equals("not sent")) return false;
        throw new Exception("incorrect state of sending");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestSendMessage req = (RequestSendMessage) o;

        return Objects.equals(this.sent, req.sent) && Objects.equals(this.getType(), req.getType());
    }

    @Override
    public String serializeToStr() {
        String str = getType().toString() + Separator.SEPARATOR ;
        if (sent) str+= "sent";
        else str+= "not sent";
        return  str;
    }
}
