import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * @author newonexd
 * @ClassName Demo
 * Description Fabric java SDK 简单调用
 * @date 2019-12-27 15:37
 * @Version 1.0
 */
public class Demo {

    public static void main(String[] args) throws Exception {

        //*****************************************************
        //*********Hyperledger Fabric客户端初始化配置************
        //*****************************************************


        //创建默认的加密套件
        CryptoSuite suite = CryptoSuite.Factory.getCryptoSuite();
        //Hyperledger Fabric 客户端
        HFClient hfClient = HFClient.createNewInstance();
        hfClient.setCryptoSuite(suite);

        //创建一个用户并加载证书与秘钥文件
        FabUser fabUser = new FabUser("admin", Const.USER_MSP_ID, Const.USER_KEY_FILE, Const.USER_CERT_FILE);
        hfClient.setUserContext(fabUser);

        //创建通道实例
        Channel channel = hfClient.newChannel(Const.CHANNEL_NAME);


        //*****************************************************
        //******************配置Peer节点*********************
        //*****************************************************

        /**
         * 添加peer节点信息，客户端可以向该peer节点发送查询与调用链码的请求
         * 需配置peer节点域名，peer节点主机地址+端口号(主机地址需要与Fabric网络中peer节点对应)
         * 如果开启TLS的话需配置TLS根证书
         */
        Peer peer = hfClient.newPeer(
                Const.PEER0_ORG1_DOMAIN_NAME, Const.PEER0_ORG1_HOST,
                loadTLSFile(Const.PEER0_ORG1_TLS_DIR, Const.PEER0_ORG1_DOMAIN_NAME));
        Peer peer1 = hfClient.newPeer(
                Const.PEER0_ORG2_DOMAIN_NAME, Const.PEER0_ORG2_HOST,
                loadTLSFile(Const.PEER0_ORG2_TLS_DIR, Const.PEER0_ORG2_DOMAIN_NAME));
        channel.addPeer(peer1);
        channel.addPeer(peer);


        //*****************************************************
        //******************配置Orderer节点*********************
        //*****************************************************

        /**
         * 添加orderer节点信息，客户端接受到peer节点的背书响应后发送到该orderer节点
         * 需配置orderer节点域名，orderer节点主机地址+端口号(主机地址需要与Fabric网络中orderer节点对应)
         * 如果开启TLS的话需配置TLS根证书
         */
        Orderer orderer = hfClient.newOrderer(
                Const.ORDERER_DOMAIN_NAME, Const.ORDERER_HOST,
                loadTLSFile(Const.ORDERER_TLS_DIR, Const.ORDERER_DOMAIN_NAME));
        channel.addOrderer(orderer);
        //通道初始化
        channel.initialize();
        //创建与Fabric中链码对应的实例
        ChaincodeID chaincodeID = ChaincodeID.newBuilder().setName(Const.CHAINCODE_NAME).build();

        //*****************************************************
        //******************查询或者调用链码功能******************
        //*****************************************************

        String queryFunc = "query";
        String[] queryArgs = {"a"};
        query(hfClient, channel, chaincodeID, queryFunc, queryArgs);


        String invokeFunc = "invoke";
        String[] invokeArgs = {"a", "b", "10"};
        invoke(hfClient, channel, chaincodeID, invokeFunc, invokeArgs);


        query(hfClient, channel, chaincodeID, queryFunc, queryArgs);
    }
    //********************************************************************************************************************************************************************

    /**
     * 为Fabric网络中节点配置TLS根证书
     *
     * @param rootTLSCert 根证书路径
     * @param hostName    节点域名
     * @return
     * @throws IOException
     */
    private static Properties loadTLSFile(String rootTLSCert, String hostName) throws IOException {
        Properties properties = new Properties();
        properties.put("pemBytes", Files.readAllBytes(Paths.get(Const.BASE_PATH + rootTLSCert)));
        properties.setProperty("sslProvider", "openSSL");
        properties.setProperty("negotiationType", "TLS");
        properties.setProperty("trustServerCertificate", "true");
        properties.setProperty("hostnameOverride", hostName);
        return properties;
    }

    /**
     * @param hfClient    Hyperledger Fabric 客户端实例
     * @param channel     通道实例
     * @param chaincodeID 链码ID
     * @param func        查询功能名称
     * @param args        查询功能需要的参数
     * @throws ProposalException
     * @throws InvalidArgumentException
     */
    private static void query(HFClient hfClient, Channel channel, ChaincodeID chaincodeID, String func, String[] args) throws ProposalException, InvalidArgumentException {
        QueryByChaincodeRequest req = hfClient.newQueryProposalRequest();
        req.setChaincodeID(chaincodeID);
        req.setFcn(func);
        req.setArgs(args);
        // 向peer节点发送调用链码的提案并等待返回查询响应集合
        Collection<ProposalResponse> queryResponse = channel.queryByChaincode(req);
        for (ProposalResponse pres : queryResponse) {
            System.out.println(pres.getProposalResponse().getResponse().getPayload().toStringUtf8());
        }
    }

    /**
     * @param hfClient    Hyperledger Fabric 客户端实例
     * @param channel     通道实例
     * @param chaincodeID 链码ID
     * @param func        查询功能名称
     * @param args        查询功能需要的参数
     * @throws InvalidArgumentException
     * @throws ProposalException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private static void invoke(HFClient hfClient, Channel channel, ChaincodeID chaincodeID, String func, String[] args) throws InvalidArgumentException, ProposalException, ExecutionException, InterruptedException {
        //提交链码交易
        TransactionProposalRequest req2 = hfClient.newTransactionProposalRequest();
        req2.setChaincodeID(chaincodeID);
        req2.setFcn(func);
        req2.setArgs(args);
        //配置提案等待时间
        req2.setProposalWaitTime(3000);
        // 向peer节点发送调用链码的提案并等待返回背书响应集合
        Collection<ProposalResponse> rsp2 = channel.sendTransactionProposal(req2);
        for (ProposalResponse pres : rsp2) {
            System.out.println(pres.getProposalResponse().getResponse().getPayload().toStringUtf8());
        }
        //将背书响应集合发送到Orderer节点
        BlockEvent.TransactionEvent event = channel.sendTransaction(rsp2).get();
        System.out.println("区块是否有效：" + event.isValid());
    }
}

