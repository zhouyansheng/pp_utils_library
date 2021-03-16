package com.yunqipei.utilslibrary.utils.fingerprint;

import android.app.KeyguardManager;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.security.keystore.KeyProperties;
import android.util.Base64;


import androidx.annotation.RequiresApi;

import com.yunqipei.utilslibrary.utils.PreferencesUtil;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;


/**
 * Created by hzlinxuanxuan on 2016/9/12.
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class FingerprintHelper extends FingerprintManager.AuthenticationCallback {
    public static final int LOGIN_PASSWORD_TYPE = 111;
    public static final int WITHDRAW_PASSWORD_TYPE = 222;

    private static FingerprintHelper sInstance;
    private Context mContext;
    private FingerprintManager manager;
    private CancellationSignal mCancellationSignal;

    private SimpleAuthenticationCallback callback;
    private LocalSharedPreference mLocalSharedPreference;
    private CryptoObjectHelper mCryptoObjectHelper;
    //PURPOSE_ENCRYPT,则表示生成token，否则为取出token
    private int purpose = KeyProperties.PURPOSE_ENCRYPT;
    //        private String data = "123456";
    //加密的密码数据 111 是登陆密码 222 是支付密码
    private int mPwdType;
    //用户手机号
    private String mLoginPhone = "";
    //需要保存的值
    private String mPasswordvalue;

    public FingerprintHelper(Context context) {
        mContext = context;
        manager = context.getSystemService(FingerprintManager.class);
        mLocalSharedPreference = LocalSharedPreference.getInstance(context);
        mCryptoObjectHelper = new CryptoObjectHelper();
    }

    public static FingerprintHelper getInstance(Context context) {
        if (sInstance == null) {
            synchronized (FingerprintHelper.class) {
                if (sInstance == null) {
                    sInstance = new FingerprintHelper(context);
                }
            }
        }
        return sInstance;
    }

    public LocalSharedPreference getLocalSharedPreference() {
        return mLocalSharedPreference;
    }

    public void generateKey() {
        //在keystore中生成加密密钥
        mCryptoObjectHelper.generateKey(CryptoObjectHelper.keyName);
        setPurpose(KeyProperties.PURPOSE_ENCRYPT);
    }

    public void setPurpose(int purpose) {
        this.purpose = purpose;
    }

    public void setPwdType(int pwdType) {
        mPwdType = pwdType;
    }

    public void setPasswordValue(String passwordValue) {
        mPasswordvalue = passwordValue == null ? "" : passwordValue;
    }

    /**
     * 登陆的时候 phone 可变 所以需要设置
     *
     * @param phone
     */
    public void setLoginPhone(String phone) {
        mLoginPhone = phone == null ? "" : phone;
    }

    public void setCallback(SimpleAuthenticationCallback callback) {
        this.callback = callback;
    }

    public boolean authenticate() {
        if (detect()) {
            //验证可用性
            try {
                FingerprintManager.CryptoObject object;
                if (purpose == KeyProperties.PURPOSE_DECRYPT) {

                    String IV = null;
                    if (mPwdType == LOGIN_PASSWORD_TYPE) {
                        //登陆
                        IV = mLocalSharedPreference.getKeyIv("" + mPwdType, mLoginPhone);
                    } else if (mPwdType == WITHDRAW_PASSWORD_TYPE) {
                        //取现
                        String phone = PreferencesUtil.getPhone();
                        IV = mLocalSharedPreference.getKeyIv("" + mPwdType, phone);
                    }
                    object = mCryptoObjectHelper.getCryptoObject(Cipher.DECRYPT_MODE, Base64.decode(IV, Base64.URL_SAFE));
                    if (object == null) {
                        return false;
                    }
                } else {
                    object = mCryptoObjectHelper.getCryptoObject(Cipher.ENCRYPT_MODE, null);
                }
                mCancellationSignal = new CancellationSignal();
                manager.authenticate(object, mCancellationSignal, 0, this, null);
                return true;
            } catch (SecurityException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * 检测指纹是否可用
     *
     * @return
     */
    public boolean detect() {
        if (manager.isHardwareDetected()) {
            KeyguardManager keyguardManager = (KeyguardManager) mContext.getSystemService(Context.KEYGUARD_SERVICE);
            if (keyguardManager.isKeyguardSecure() && manager.hasEnrolledFingerprints()) {
                //设置了安全保护  有指纹密码
                return true;
            } else {
                //提示开启系统指纹锁
//                if (isShow) {
//                    ToastMaker.show("请先在系统中设置指纹");
//                }
                //没有指纹或者指纹清空了就清除指纹登陆密码
                clearAllString();
            }
        }
        return false;
    }

    /**
     * 清除
     */
    public void clearAllString() {
        mLocalSharedPreference.clearAllString();
    }

    /**
     * 清除登陆密码
     */
    public void clearLoginPassword() {
        String phone = PreferencesUtil.getPhone();
        mLocalSharedPreference.saveLoginPassword(phone, null);
    }

    /**
     * 清除提现密码
     */
    public void clearWithdrawPassword() {
        String phone = PreferencesUtil.getPhone();
        mLocalSharedPreference.saveWithdrawPassword(phone, null);
    }

    public void stopAuthenticate() {
        if (mCancellationSignal != null) {
            mCancellationSignal.cancel();
            mCancellationSignal = null;
        }
        callback = null;
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        if (callback != null) {
            callback.onAuthenticationFail();
        }
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        if (callback == null) {
            return;
        }
        if (result.getCryptoObject() == null) {
            callback.onAuthenticationFail();
            return;
        }
        final Cipher cipher = result.getCryptoObject().getCipher();
        if (purpose == KeyProperties.PURPOSE_DECRYPT) {
            //取出secret key并返回
            String data = null;
            if (mPwdType == LOGIN_PASSWORD_TYPE) {
                //登陆
                data = mLocalSharedPreference.getLoginPassword(mLoginPhone);
            } else if (mPwdType == WITHDRAW_PASSWORD_TYPE) {
                //取现
                String phone = PreferencesUtil.getPhone();
                data = mLocalSharedPreference.getWithdrawPassword(phone);
            }

            try {
                byte[] decrypted = cipher.doFinal(Base64.decode(data, Base64.URL_SAFE));
                callback.onAuthenticationSucceeded(new String(decrypted));
            } catch (BadPaddingException | IllegalBlockSizeException e) {
                e.printStackTrace();
                callback.onAuthenticationFail();
            }
        } else {
            //将前面生成的data包装成secret key，存入沙盒
            try {
                byte[] encrypted = cipher.doFinal(mPasswordvalue.getBytes());
                byte[] IV = cipher.getIV();
                String se = Base64.encodeToString(encrypted, Base64.URL_SAFE);
                String siv = Base64.encodeToString(IV, Base64.URL_SAFE);

                boolean password = false;
                if (mPwdType == LOGIN_PASSWORD_TYPE) {
                    //登陆
                    String phone = PreferencesUtil.getPhone();
                    password = mLocalSharedPreference.saveLoginPassword(phone, se);
                } else if (mPwdType == WITHDRAW_PASSWORD_TYPE) {
                    //取现
                    String phone = PreferencesUtil.getPhone();
                    password = mLocalSharedPreference.saveWithdrawPassword(phone, se);
                }
                if (password && mLocalSharedPreference.saveKayIv("" + mPwdType, siv)) {
                    callback.onAuthenticationSucceeded(se);
                } else {
                    callback.onAuthenticationFail();
                }
            } catch (BadPaddingException | IllegalBlockSizeException e) {
                e.printStackTrace();
                callback.onAuthenticationFail();
            }
        }
    }

    @Override
    public void onAuthenticationFailed() {
    }

    public interface SimpleAuthenticationCallback {
        void onAuthenticationSucceeded(String value);

        void onAuthenticationFail();
    }
}
