package requests;

import tools.Separator;

import java.util.Objects;

public class RequestAuthorization extends Request{//2 possible answers but type specification needed
    private static final String state = "authorized";
    Boolean authorized;
    private String jwt_token;

    public String getState(){
        return state;
    }

    public RequestAuthorization(Boolean flag, String jwt){
        super(RequestType.LOGIN);
        authorized = flag;
        jwt_token = jwt;
    }

    public RequestAuthorization(String authorization, String jwt){
        super(RequestType.LOGIN);
        jwt_token = jwt;
        try{
            this.authorized = check(authorization);
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

    public Boolean isAuthorized(){
        return authorized;
    }

    public String getJwt_token(){
        return jwt_token;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestAuthorization req = (RequestAuthorization) o;

        return Objects.equals(this.jwt_token, req.jwt_token) && Objects.equals(this.authorized, req.authorized) && Objects.equals(this.getType(), req.getType());
    }

    @Override
    public String serializeToStr() {
        String str = getType().toString() + Separator.SEPARATOR ;
        if (authorized) str+= state;
        else str+= "not " + state;
        str += Separator.SEPARATOR + jwt_token;
      return  str;
    }
}
