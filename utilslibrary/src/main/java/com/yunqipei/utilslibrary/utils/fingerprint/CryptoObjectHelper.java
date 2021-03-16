package com.yunqipei.utilslibrary.utils.fingerprint;

import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;


import androidx.annotation.RequiresApi;

import java.security.KeyStore;
import java.security.KeyStoreException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

@RequiresApi(api = Build.VERSION_CODES.M)
public class CryptoObjectHelper {

    public static final String keyName = "com.peipeiyun.autopartsmaster";
    private KeyStore mStore;

    CryptoObjectHelper() {
        try {
            mStore = KeyStore.getInstance("AndroidKeyStore");
            mStore.load(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void generateKey(String keyAlias) {
        try {
            if (!mStore.isKeyEntry(keyAlias)) {
                createKey(keyAlias);
            }
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

    }

    private void createKey(String keyAlias) {
        //这里使用AES + CBC + PADDING_PKCS7，并且需要用户验证方能取出
        try {
            final KeyGenerator generator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            KeyGenParameterSpec keyGenSpec = new KeyGenParameterSpec.Builder(keyAlias, KeyProperties.PURPOSE_DECRYPT | KeyProperties.PURPOSE_ENCRYPT)
                    .setUserAuthenticationRequired(true)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build();
            generator.init(keyGenSpec);
            generator.generateKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    FingerprintManager.CryptoObject getCryptoObject(int purpose, byte[] IV) {
//        byte[] IV1=new byte[]{116,-61,54,75,-69,-35,113,-117,115,2,47,-69,39,45,28,-117};
        try {
            mStore.load(null);
            final SecretKey key = (SecretKey) mStore.getKey(keyName, null);
            if (key == null) {
                return null;
            }
            final Cipher cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC
                    + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
            if (purpose == KeyProperties.PURPOSE_ENCRYPT) {
                cipher.init(purpose, key);
            } else {
                cipher.init(purpose, key, new IvParameterSpec(IV));
            }
            return new FingerprintManager.CryptoObject(cipher);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
