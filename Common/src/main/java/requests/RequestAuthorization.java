package requests;

import tools.Separator;

import java.util.Objects;

public class RequestAuthorization extends Request{//2 possible answers but type specification needed
    private static final String state = "authorized";
    Boolean authorized;

    public String getState(){
        return state;
    }
    public RequestAuthorization(Boolean flag){
        super(RequestType.SEND_MESSAGE);
        authorized = flag;
    }

    public RequestAuthorization(String str) {
        super(RequestType.LOGIN);
        try{
            this.authorized = check(str);
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    Boolean check(String str)throws Exception{
        if (str.equals(state)) return true;
        if (str.equals("not " + state)) return false;
        throw new Exception("incorrect state of authorization");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestAuthorization req = (RequestAuthorization) o;

        return Objects.equals(this.authorized, req.authorized) && Objects.equals(this.getType(), req.getType());
    }

    @Override
    public String serializeToStr() {
        String str = getType().toString() + Separator.SEPARATOR ;
        if (authorized) str+= state;
        else str+= "not " + state;
      return  str;
    }
}
