package requests;

import tools.Separator;

import java.util.Objects;

public class RequestRegistration extends Request{
    Boolean registered;

    RequestRegistration(String str) throws Exception {
        super(RequestType.REGISTER);
        try{
            this.registered = check(str);
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    Boolean check(String str)throws Exception{
        if (str.equals("registered")) return true;
        if (str.equals("not registered")) return false;
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
        if (registered) str+= "registered";
        else str+= "not registered";
        return  str;
    }
}
