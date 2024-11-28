package requests;

import tools.Separator;

import java.util.Objects;

public class RequestRegistration extends Request{
    Boolean registered;
    private static final String state = "registered";

    public String getState(){
        return state;
    }

    public RequestRegistration(Boolean flag){
        super(RequestType.REGISTER);
        registered = flag;
    }

    public RequestRegistration(String str) {
        super(RequestType.REGISTER);
        try{
            this.registered = check(str);
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    Boolean check(String str)throws Exception{
        if (str.equals(state)) return true;
        if (str.equals("not " + state)) return false;
        throw new Exception("incorrect state of registration");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestRegistration req = (RequestRegistration) o;

        return Objects.equals(this.registered, req.registered) && Objects.equals(this.getType(), req.getType());
    }

    @Override
    public String serializeToStr() {
        String str = getType().toString() + Separator.SEPARATOR ;
        if (registered) str+= state;
        else str+= "not " + state;
        return  str;
    }
}
