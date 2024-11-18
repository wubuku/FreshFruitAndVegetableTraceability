#!/bin/bash

# 设置变量
OUTPUT_DIR="../src/main/resources/keys"
KEYSTORE="$OUTPUT_DIR/jwt-signing-keys.jks"
KEYSTORE_PASSWORD="ffvtraceability"
KEY_ALIAS="jwt-signing-key"
KEY_PASSWORD="ffvtraceability"
VALIDITY=3650 # 10 years

# 创建输出目录
mkdir -p "$OUTPUT_DIR"

# 生成密钥对
keytool -genkeypair \
  -alias $KEY_ALIAS \
  -keyalg RSA \
  -keysize 2048 \
  -validity $VALIDITY \
  -keystore $KEYSTORE \
  -storetype JKS \
  -storepass $KEYSTORE_PASSWORD \
  -keypass $KEY_PASSWORD \
  -dname "CN=FFV Auth Server,OU=FFV,O=FFV,L=City,ST=State,C=CN"

echo "Keystore created at: $KEYSTORE"
echo "Please update application.yml with these credentials:"
echo "security.jwt.key-store-password: $KEYSTORE_PASSWORD"
echo "security.jwt.private-key-passphrase: $KEY_PASSWORD"
