#!/bin bash

# 进入Maven项目的resources文件夹
cd $PWD/src/main/resources/

# 删除旧的证书文件(如果没有则自动跳过)
rm -rf ./crypto-config
# 将新的证书文件拷贝过来
cp -r ~/go/src/github.com/hyperledger/fabric-samples/first-network/crypto-config .

exit 0