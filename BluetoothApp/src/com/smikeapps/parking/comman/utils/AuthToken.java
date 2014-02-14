package com.smikeapps.parking.comman.utils;

import java.io.Serializable;

import com.smikeapps.parking.common.context.AccountPreference;

public class AuthToken implements Serializable {

    private static final long serialVersionUID = 1891050963856615922L;

    
    private String accessToken;
    private String userId;
    private String createdOn;

    public AuthToken() {
    }

    public AuthToken(String accessToken, String userId, String createdOn) {
        super();
        this.accessToken = accessToken;
        this.userId = userId;
        this.createdOn = createdOn;
        AccountPreference.setTokenString(accessToken);
    }

    /**
     * Getter/Setter methods
     */

    public String getAccessToken() {
       
        return accessToken;
    }

    public String getUserId() {
       
        return userId;
    }


    public String getCreatedOn() {
       
        return createdOn;
    }
}
