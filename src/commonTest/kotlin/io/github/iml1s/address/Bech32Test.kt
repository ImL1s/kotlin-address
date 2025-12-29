package io.github.iml1s.address

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class Bech32Test {

    @Test
    fun testBech32Encode() {
        // 測試向量: BIP173
        val hrp = "bc"
        val data = byteArrayOf(0, 14, 20, 15, 7, 13, 26, 0, 25, 18, 6, 11, 13, 8, 21, 4, 20, 3, 17, 2, 29, 3, 12, 29, 3, 4, 15, 24, 20, 6, 14, 30, 22)
        
        val result = Bech32.encode(hrp, data)
        assertTrue(result.startsWith("bc1"))
    }

    @Test
    fun testBech32Decode() {
        // 有效的 Bech32 地址
        val address = "bc1qw508d6qejxtdg4y5r3zarvary0c5xw7kv8f3t4"
        val decoded = Bech32.decode(address)
        
        assertNotNull(decoded)
        assertEquals("bc", decoded.hrp)
    }

    @Test
    fun testBech32mEncode() {
        // Taproot 地址使用 Bech32m
        val hrp = "bc"
        val witnessVersion = 1
        val program = ByteArray(32) { 0x01 }
        
        val result = Bech32.encodeSegwitAddress(hrp, witnessVersion, program)
        assertNotNull(result)
        assertTrue(result.startsWith("bc1p"))
    }

    @Test
    fun testSegwitAddressRoundTrip() {
        val hrp = "bc"
        val witnessVersion = 0
        val program = ByteArray(20) { (it + 1).toByte() }
        
        val encoded = Bech32.encodeSegwitAddress(hrp, witnessVersion, program)
        assertNotNull(encoded)
        
        val decoded = Bech32.decodeSegwitAddress(encoded)
        assertNotNull(decoded)
        assertEquals(witnessVersion, decoded.first)
        assertTrue(program.contentEquals(decoded.second))
    }
}
