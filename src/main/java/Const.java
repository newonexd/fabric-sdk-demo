/**
 * @author newonexd
 * @ClassName Const
 * Description 常量信息
 * @date 2019-12-28 11:05
 * @Version 1.0
 */
public final class Const {
    public static String BASE_PATH = Const.class.getClassLoader().getResource("./").getPath();
    public static final String PEER0_ORG1_DOMAIN_NAME = "peer0.org1.example.com";

    public static final String PEER0_ORG2_DOMAIN_NAME = "peer0.org2.example.com";

    public static final String PEER0_ORG1_HOST = "grpcs://localhost:7051";

    public static final String PEER0_ORG2_HOST = "grpcs://localhost:9051";

    public static final String PEER0_ORG1_TLS_DIR = "crypto-config/peerOrganizations/org1.example.com/tlsca/tlsca.org1.example.com-cert.pem";

    public static final String PEER0_ORG2_TLS_DIR = "crypto-config/peerOrganizations/org2.example.com/tlsca/tlsca.org2.example.com-cert.pem";

    public static final String ORDERER_DOMAIN_NAME = "orderer.example.com";

    public static final String ORDERER_TLS_DIR = "crypto-config/ordererOrganizations/example.com/orderers/orderer.example.com/tls/ca.crt";

    public static final String ORDERER_HOST = "grpcs://localhost:7050";

    public static final String CHANNEL_NAME = "mychannel";

    public static final String CHAINCODE_NAME = "mycc";


    public static final String USER_MSP_ID="Org1MSP";

    public static final String USER_AFFILIATION="org1";

    public static final String USER_KEY_FILE = "crypto-config/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp/keystore/priv_sk";

    public static final String USER_CERT_FILE = "crypto-config/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp/signcerts/Admin@org1.example.com-cert.pem";

}
