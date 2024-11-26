package requests;

import tools.Separator;

import java.util.Objects;

public class RequestAuthorization extends Request{//2 possible answers but type specification needed

    Boolean authorized;

    RequestAuthorization(String str) throws Exception {
        super(RequestType.LOGIN);
        try{
            this.authorized = check(str);
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    Boolean check(String str)throws Exception{
        if (str.equals("authorized")) return true;
        if (str.equals("not authorized")) return false;
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
        if (authorized) str+= "authorized";
        else str+= "not authorized";
      return  str;
    }
}
