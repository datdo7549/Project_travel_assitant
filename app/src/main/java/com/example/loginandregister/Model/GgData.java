package com.example.loginandregister.Model;

public class GgData {
    private String grant_type;
    private String client_id;
    private String client_secret;
    private String redirect_uri;
    private String id_token;
    private String code;

    public GgData(String grant_type, String client_id, String client_secret, String redirect_uri, String id_token, String code) {
        this.grant_type = grant_type;
        this.client_id = client_id;
        this.client_secret = client_secret;
        this.redirect_uri = redirect_uri;
        this.id_token = id_token;
        this.code = code;
    }
}
