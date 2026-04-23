package com.inkFront.inFront.dto.auth;

public class CsrfTokenDTO {

    private String token;
    private String headerName;
    private String parameterName;

    public CsrfTokenDTO() {
    }

    public CsrfTokenDTO(String token, String headerName, String parameterName) {
        this.token = token;
        this.headerName = headerName;
        this.parameterName = parameterName;
    }

    public String getToken() {
        return token;
    }

    public String getHeaderName() {
        return headerName;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }
}