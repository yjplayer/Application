package cn.dev.application.loopjnet;

import android.text.TextUtils;

/**
 * Created by air on 16/1/28.
 */
public class Request {

    private String url;
    private boolean encrypt;
    private String method;
    private RequestBody requestBody;

    public Request(Builder builder) {
        this.url = builder.url;
        this.method = builder.method;
        this.encrypt = builder.encrypt;
        this.requestBody = builder.requestBody;
        this.requestBody.encrypt(true);
    }

    public String url() {
        return url;
    }

    public String method() {
        return method;
    }

    public RequestBody body() {
        return requestBody;
    }

    public static class Builder{

        private String url;
        private boolean encrypt;
        private String method = "POST";
        private RequestBody requestBody;

        public Builder url(String url){
            if (TextUtils.isEmpty(url)){
                throw new RuntimeException("the url must not be null");
            }
            this.url = url;
            return this;
        }

        public Builder encrypt(Boolean encrypt){
            this.encrypt = encrypt;
            return this;
        }

        public Builder method(String method){
            if (TextUtils.isEmpty(method)){
                this.method = "POST";
            }
            return this;
        }

        public Builder body(RequestBody requestBody){
            this.requestBody = requestBody == null? new RequestBody():requestBody;
            return this;
        }

        public Builder addParams(String key,Object value){
            if (requestBody == null){
                requestBody = new RequestBody();
            }
            requestBody.addParams(key,value);
            return this;
        }

        public Request build(){
            return new Request(this);
        }
    }
}
