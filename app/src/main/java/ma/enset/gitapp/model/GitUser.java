package ma.enset.gitapp.model;

import com.google.gson.annotations.SerializedName;

public class GitUser {
    public int id;
    public String login;
    @SerializedName("avatar_url")
    public String avatarUser;
    @SerializedName("html_url")
    public String htmlUrl;

}
