package io.github.iml1s.address

import kotlin.test.Test
import kotlin.test.assertEquals

import io.github.iml1s.address.Base58
import io.github.iml1s.address.DeterministicWallet
import io.github.iml1s.address.KeyPath

class DeterministicWalletTest {

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun testBIP32_Vector1() {
        // BIP32 Test Vector 1
        // Seed: 000102030405060708090a0b0c0d0e0f
        val seed = "000102030405060708090a0b0c0d0e0f".hexToByteArray()
        val master = DeterministicWallet.generate(seed)

        // Chain m
        val actualXprv = master.encode(DeterministicWallet.xprv)
        println("    Master xpub: ${master.extendedPublicKey().encode(DeterministicWallet.xpub)}")
        val expectedXprv = "xprv9uHRZZhk6KAJC1avXpDAp4MDc3sQKNxDiPvvkX8Br5ngLNv1TxvUxt4cV1rGL5hj6KCesnDYUhd7oWgT11eZG7XnxHrnYeSvkzY7d2bhkJ7"
        
        val child1 = master.derivePrivateKey(KeyPath("m/0'"))
        val actualXpub = master.extendedPublicKey().encode(DeterministicWallet.xpub)
        println("Master xprv: $actualXprv")
        println("Master xpub: $actualXpub")
        assertEquals("xprv9s21ZrQH143K3QTDL4LXw2F7HEK3wJUD2nW2nRk4stbPy6cq3jPPqjiChkVvvNKmPGJxWUtg6LnF5kejMRNNU3TGtRBeJgk33yuGBxrMPHi", actualXprv)
        assertEquals("xpub661MyMwAqRbcFtXgS5sYJABqqG9YLmC4Q1Rdap9gSE8NqtwybGhePY2gZ29ESFjqJoCu1Rupje8YtGqsefD265TMg7usUDFdp6W1EGMcet8", actualXpub)

        // Chain m/0H (hardened 0)
        val m0h = master.derivePrivateKey(DeterministicWallet.hardened(0))
        assertEquals("xprv9uHRZZhk6KAJC1avXpDAp4MDc3sQKNxDiPvvkX8Br5ngLNv1TxvUxt4cV1rGL5hj6KCesnDYUhd7oWgT11eZG7XnxHrnYeSvkzY7d2bhkJ7", m0h.encode(DeterministicWallet.xprv))
        assertEquals("xpub68Gmy5EdvgibQVfPdqkBBCHxA5htiqg55crXYuXoQRKfDBFA1WEjWgP6LHhwBZeNK1VTsfTFUHCdrfp1bgwQ9xv5ski8PX9rL2dZXvgGDnw", m0h.extendedPublicKey().encode(DeterministicWallet.xpub))
    }

    private fun String.hexToByteArray(): ByteArray {
        return chunked(2).map { it.toInt(16).toByte() }.toByteArray()
    }
}
