package tk.saiprasadm.smartled;

/**
 * Created by SAI on 25-06-2016.
 * This class will be used for encryption of ssid and password data by using 128-bit AES Encryption algorithm
 */
public class Encryption {
    private String dataTobeEncrypted;
    private String encryptedData;
    private String key;

    Encryption(String data) {
        this.dataTobeEncrypted = data;
    }

    public void setDataTobeEncrypted(String data) {
        this.dataTobeEncrypted = data;
    }

    public String getEncryptedData() {

        return this.dataTobeEncrypted;
    }


}
