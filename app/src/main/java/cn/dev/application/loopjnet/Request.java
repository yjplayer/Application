package cn.dev.application.loopjnet;

/**
 * Created by air on 16/1/28.
 */
public class Request {

    private String url;
    private String method;
//    private String

    public Request(Builder builder) {

    }

    public static class Builder{

        public Request build(){
            return new Request(this);
        }
    }
}
