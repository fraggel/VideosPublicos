package es.fraggel;

import com.jcraft.jsch.UserInfo;

/**
 * Created by u028978 on 31/05/2017.
 */
public class MyUserInfo implements UserInfo {
    private final String user_;
    private final String password_;
    private final String passphrase_;

    public MyUserInfo(String user, String password, String passphrase) {
        user_ = user;
        password_ = password;
        passphrase_ = passphrase;
    }

    public String getUser() {
        return user_;
    }

    public String getPassphrase() {
        return passphrase_;
    }

    public String getPassword() {
        return password_;
    }

    public boolean promptPassphrase(String message) {
        return true;
    }

    public boolean promptPassword(String message) {
        return true;
    }

    public boolean promptYesNo(String message) {
        return true;
    }

    public void showMessage(String message) {
    }
}
