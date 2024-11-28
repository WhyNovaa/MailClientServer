package requests;

import tools.Separator;

import java.util.Objects;

public class RequestSendMessage extends Request{
    Boolean is_sent;
    private static final String state = "sent";

    public String getState(){
        return state;
    }

    public RequestSendMessage(Boolean is_sent){
        super(RequestType.SEND_MESSAGE);
        this.is_sent = is_sent;
    }


    public RequestSendMessage(String str) {
        super(RequestType.SEND_MESSAGE);
        try{
            this.is_sent = check(str);
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    Boolean check(String str)throws Exception{
        if (str.equals(state)) return true;
        if (str.equals("not " + state)) return false;
        throw new Exception("incorrect state of sending");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestSendMessage req = (RequestSendMessage) o;

        return Objects.equals(this.is_sent, req.is_sent) && Objects.equals(this.getType(), req.getType());
    }

    @Override
    public String serializeToStr() {
        String str = getType().toString() + Separator.SEPARATOR ;
        if (is_sent) str+= state;
        else str+= "not " + state;
        return  str;
    }
}
