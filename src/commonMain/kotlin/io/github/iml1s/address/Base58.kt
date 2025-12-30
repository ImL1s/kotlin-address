package io.github.iml1s.address

/**
 * Base58Check 編碼實現
 *
 * 用於 Legacy 和 Nested SegWit 地址編碼
 *
 * @see <a href="https://en.bitcoin.it/wiki/Base58Check_encoding">Base58Check</a>
 */
object Base58 {

    private const val ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz"
    private val INDEXES = IntArray(128) { -1 }.apply {
        for (i in ALPHABET.indices) {
            this[ALPHABET[i].code] = i
        }
    }

    /**
     * Base58 編碼
     */
    fun encode(input: ByteArray): String {
        if (input.isEmpty()) return ""

        // 計算前導零的數量
        var zeros = 0
        while (zeros < input.size && input[zeros].toInt() == 0) {
            zeros++
        }

        // 轉換為 Base58
        val encoded = CharArray(input.size * 2)
        var outputStart = encoded.size

        var start = zeros
        while (start < input.size) {
            outputStart--
            encoded[outputStart] = ALPHABET[divmod(input, start, 256, 58)]
            if (input[start].toInt() == 0) {
                start++
            }
        }

        // 跳過結果中的前導零
        while (outputStart < encoded.size && encoded[outputStart] == ALPHABET[0]) {
            outputStart++
        }

        // 保留原始前導零
        repeat(zeros) {
            outputStart--
            encoded[outputStart] = ALPHABET[0]
        }

        return String(encoded, outputStart, encoded.size - outputStart)
    }

    /**
     * Base58 解碼
     */
    fun decode(input: String): ByteArray? {
        if (input.isEmpty()) return ByteArray(0)

        // 轉換為 Base58 數字
        val input58 = ByteArray(input.length)
        for (i in input.indices) {
            val c = input[i]
            val digit = if (c.code < 128) INDEXES[c.code] else -1
            if (digit < 0) return null
            input58[i] = digit.toByte()
        }

        // 計算前導零
        var zeros = 0
        while (zeros < input58.size && input58[zeros].toInt() == 0) {
            zeros++
        }

        // 轉換為 byte 陣列
        val decoded = ByteArray(input.length)
        var outputStart = decoded.size

        var start = zeros
        while (start < input58.size) {
            outputStart--
            decoded[outputStart] = divmod(input58, start, 58, 256).toByte()
            if (input58[start].toInt() == 0) {
                start++
            }
        }

        // 跳過結果中的前導零
        while (outputStart < decoded.size && decoded[outputStart].toInt() == 0) {
            outputStart++
        }

        return ByteArray(zeros + (decoded.size - outputStart)).also { result ->
            decoded.copyInto(result, zeros, outputStart, decoded.size)
        }
    }

    /**
     * Base58Check 編碼（包含 checksum）
     *
     * @param version 版本位元組
     * @param payload 資料
     * @return 編碼後的字串
     */
    fun encodeCheck(version: Byte, payload: ByteArray): String {
        val data = ByteArray(1 + payload.size)
        data[0] = version
        payload.copyInto(data, 1)

        val checksum = doubleSha256(data).sliceArray(0 until 4)
        return encode(data + checksum)
    }

    /**
     * Base58Check 解碼
     *
     * @param input 編碼的字串
     * @return Pair(version, payload) 或 null
     */
    fun decodeCheck(input: String): Pair<Byte, ByteArray>? {
        val decoded = decode(input) ?: return null
        if (decoded.size < 5) return null

        val data = decoded.sliceArray(0 until decoded.size - 4)
        val checksum = decoded.sliceArray(decoded.size - 4 until decoded.size)
        val expectedChecksum = doubleSha256(data).sliceArray(0 until 4)

        if (!checksum.contentEquals(expectedChecksum)) return null

        val version = data[0]
        val payload = data.sliceArray(1 until data.size)

        return Pair(version, payload)
    }

    private fun divmod(number: ByteArray, start: Int, base: Int, divisor: Int): Int {
        var remainder = 0
        for (i in start until number.size) {
            val digit = number[i].toInt() and 0xFF
            val temp = remainder * base + digit
            number[i] = (temp / divisor).toByte()
            remainder = temp % divisor
        }
        return remainder
    }

    /**
     * 平台特定的 SHA256 實現（需要在各平台實現）
     */
    private fun doubleSha256(data: ByteArray): ByteArray {
        val hash1 = sha256(data)
        return sha256(hash1)
    }

    /**
     * 平台特定的 SHA256
     */
    private fun sha256(data: ByteArray): ByteArray {
        return org.kotlincrypto.hash.sha2.SHA256().digest(data)
    }
}

