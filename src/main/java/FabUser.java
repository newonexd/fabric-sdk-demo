import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.identity.X509Enrollment;
import org.hyperledger.fabric.sdk.security.CryptoPrimitives;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.util.Set;

/**
 * @author newonexd
 * @ClassName FabUser
 * Description TODO
 * @date 2019-12-28 14:36
 * @Version 1.0
 */
public class FabUser implements User {

    private FabUser() {

    }

    /**
     * 创建用户实例
     * @param name
     * @param mspId
     * @param keyFile  当前用户秘钥文件路径
     * @param certFile 当前用户证书文件路径
     */
    FabUser(String name, String mspId, String keyFile, String certFile) {
        if ((this.enrollment = loadKeyAndCert(keyFile, certFile)) != null) {
            this.name = name;
            this.mspId = mspId;
        }
    }

    private String name;


    public void setAccount(String account) {
        this.account = account;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }


    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
    }

    private String account;
    private String affiliation;
    private String mspId;
    private Set<String> roles;
    private Enrollment enrollment;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Set<String> getRoles() {
        return this.roles;
    }

    @Override
    public String getAccount() {
        return this.account;
    }

    @Override
    public String getAffiliation() {
        return this.affiliation;
    }

    @Override
    public Enrollment getEnrollment() {
        return this.enrollment;
    }

    @Override
    public String getMspId() {
        return this.mspId;
    }


    /**
     * 从文件系统中加载秘钥与证书
     * @param keyFile
     * @param certFile
     * @return
     */
    private Enrollment loadKeyAndCert(String keyFile, String certFile) {
        byte[] keyPem;
        try {
            keyPem = Files.readAllBytes(Paths.get(Const.BASE_PATH + keyFile));
            byte[] certPem = Files.readAllBytes(Paths.get(Const.BASE_PATH + certFile));
            CryptoPrimitives primitives = new CryptoPrimitives();
            PrivateKey privateKey = primitives.bytesToPrivateKey(keyPem);
            return new X509Enrollment(privateKey, new String(certPem));
        } catch (IOException | IllegalAccessException | InstantiationException | CryptoException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
