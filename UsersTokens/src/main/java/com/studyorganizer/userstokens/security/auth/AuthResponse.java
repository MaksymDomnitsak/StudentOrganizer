package com.studyorganizer.userstokens.security.auth;


import com.studmodel.UserRole;

public class AuthResponse {

  private Long userId;

  private String email;

  private String userName;
  private String jwtToken;

  private String accessToken;

  private Boolean isEventer;

  private UserRole role;

  public AuthResponse() {
  }

  public AuthResponse(Long userId, String email, String userName,Boolean isEventer, String jwtToken, String accessToken, UserRole role) {
    this.userId = userId;
    this.email = email;
    this.userName = userName;
    this.jwtToken = jwtToken;
    this.isEventer = isEventer;
    this.accessToken = accessToken;
    this.role = role;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getJwtToken() {
    return jwtToken;
  }

  public void setJwtToken(String jwtToken) {
    this.jwtToken = jwtToken;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public UserRole getRole() {
    return role;
  }

  public void setRole(UserRole role) {
    this.role = role;
  }

  public Boolean getEventer() {
    return isEventer;
  }

  public void setEventer(Boolean eventer) {
    isEventer = eventer;
  }
}
