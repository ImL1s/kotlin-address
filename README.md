# kotlin-address

<p align="center">
  <img src="../docs/images/kmp_crypto_banner.png" alt="kotlin-address" width="100%">
</p>

<p align="center">
  <a href="#"><img src="https://img.shields.io/badge/kotlin-2.1.0-blue.svg?logo=kotlin" alt="Kotlin"></a>
  <a href="#"><img src="https://img.shields.io/badge/multiplatform-android%20%7C%20ios%20%7C%20watchOS%20%7C%20JVM-orange" alt="Multiplatform"></a>
  <a href="LICENSE"><img src="https://img.shields.io/badge/license-Apache%202.0-blue.svg" alt="License"></a>
</p>

<p align="center">
  <strong>🏠 Pure Kotlin Multiplatform address generation for cryptocurrency wallets.</strong>
</p>

---

## ✨ Features

| Feature | Description |
|---------|-------------|
| **BIP44** | Legacy P2PKH addresses (`1...`) |
| **BIP49** | Nested SegWit P2SH-P2WPKH (`3...`) |
| **BIP84** | Native SegWit P2WPKH (`bc1q...`) |
| **BIP86** | Taproot P2TR (`bc1p...`) |
| **Bech32/Bech32m** | BIP173/350 compliant encoding |
| **Base58Check** | Legacy address encoding |
| **Ethereum** | EIP-55 checksum addresses |
| **Multi-network** | Mainnet, Testnet, Regtest |

---

## 🚀 Quick Start

```kotlin
import io.github.iml1s.address.*

// Generate Bitcoin addresses from public key
val pubKey: ByteArray = ... // 33-byte compressed public key

// BIP84 Native SegWit (recommended)
val segwitAddress = AddressGenerator.generateP2WPKH(pubKey)
// Result: "bc1q..."

// BIP86 Taproot
val taprootAddress = AddressGenerator.generateP2TR(pubKey)
// Result: "bc1p..."

// BIP44 Legacy
val legacyAddress = AddressGenerator.generateP2PKH(pubKey)
// Result: "1..."

// Ethereum address
val ethPubKey: ByteArray = ... // 65-byte uncompressed public key
val ethAddress = EthereumAddress.fromPublicKey(ethPubKey)
// Result: "0x..."
```

### Derivation Paths

```kotlin
// BIP44 Bitcoin
val bip44 = DerivationPath.bip44Bitcoin(account = 0, index = 0)
println(bip44.toPathString()) // "m/44'/0'/0'/0/0"

// BIP84 Native SegWit
val bip84 = DerivationPath.bip84Bitcoin(account = 0, index = 5)
println(bip84.toPathString()) // "m/84'/0'/0'/0/5"

// BIP44 Ethereum
val ethPath = DerivationPath.bip44Ethereum()
println(ethPath.toPathString()) // "m/44'/60'/0'/0/0"

// Parse from string
val parsed = DerivationPath.parse("m/84'/0'/0'/0/10")
```

---

## 📦 Installation

```kotlin
// build.gradle.kts
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation("com.github.iml1s:kotlin-address:1.0.0")
        }
    }
}
```

---

## 📄 License

```
Copyright 2024 ImL1s

Licensed under the Apache License, Version 2.0
```
