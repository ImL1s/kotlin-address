package io.github.iml1s.address

import kotlin.test.*

class Base58Test {

    @Test
    fun testEncode() {
        assertEquals("", Base58.encode(byteArrayOf()))
        assertEquals("1", Base58.encode(byteArrayOf(0)))
        assertEquals("11", Base58.encode(byteArrayOf(0, 0)))
        
        // Official Base58 vector: "Hello World!" -> "2NEpo7TZRRrLZSi2U"
        assertEquals("2NEpo7TZRRrLZSi2U", Base58.encode("Hello World!".encodeToByteArray()))
    }

    @Test
    fun encodeKeepsLeadingOnesForLeadingZeroBytes() {
        // Official Bitcoin base58 vector: leading 0x00 + non-zero payload.
        // Drives shipped Base58.encode (not a copy). copyOf() because encode mutates.
        val official = "00eb15231dfceb60925886b67d065299925915aeb172c06647".hexToByteArray()
        val officialEncoded = Base58.encode(official.copyOf())
        assertTrue(officialEncoded.startsWith("1"), officialEncoded)
        assertEquals("1NS17iag9jJgTHD1VXjvLCEnZuQ3rJDE9L", officialEncoded)
        assertContentEquals(official, Base58.decode(officialEncoded))

        val threeLeadingZeros = byteArrayOf(0, 0, 0, 1)
        val encoded = Base58.encode(threeLeadingZeros.copyOf())
        assertTrue(encoded.startsWith("111"), encoded)
        assertEquals(3, encoded.takeWhile { it == '1' }.length)
        assertContentEquals(threeLeadingZeros, Base58.decode(encoded))
    }

    @Test
    fun testDecode() {
        // Round trip
        val original = "The quick brown fox jumps over the lazy dog."
        val encoded = Base58.encode(original.encodeToByteArray())
        val decoded = Base58.decode(encoded)
        assertNotNull(decoded)
        assertEquals(original, decoded.decodeToString())
    }

    @Test
    fun testEncodeCheck_OfficialVector() {
        // Official Bitcoin Base58Check Example
        // Payload (20 bytes hash): 010966776006953D5567439E5E39F86A0D273BEE
        // Version: 0x00 (Mainnet P2PKH)
        val payload = "010966776006953D5567439E5E39F86A0D273BEE".hexToByteArray()
        val version: Byte = 0x00
        val encoded = Base58.encodeCheck(version, payload)
        
        // Expected: 16UwLL9Risc3QfPqBUvKofHmBQ7wMtjvM
        assertEquals("16UwLL9Risc3QfPqBUvKofHmBQ7wMtjvM", encoded)
    }

    @Test
    fun testDecodeCheck_OfficialVector() {
        val input = "16UwLL9Risc3QfPqBUvKofHmBQ7wMtjvM"
        val decoded = Base58.decodeCheck(input)
        
        assertNotNull(decoded)
        assertEquals(0x00.toByte(), decoded.first)
        assertEquals("010966776006953D5567439E5E39F86A0D273BEE", decoded.second.toHexString())
    }

    @Test
    fun testInvalidChecksum() {
        val input = "16UwLL9Risc3QfPqBUvKofHmBQ7wMtjvM"
        // Corrupt first char (will fail version or checksum)
        val corrupted = "2" + input.substring(1)
        assertNull(Base58.decodeCheck(corrupted))
    }

    private fun String.hexToByteArray(): ByteArray {
        return chunked(2).map { it.toInt(16).toByte() }.toByteArray()
    }

    private fun ByteArray.toHexString(): String {
        return joinToString("") { "%02x".format(it).uppercase() }
    }
}
