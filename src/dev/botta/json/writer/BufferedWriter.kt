package dev.botta.json.writer

import java.io.Writer

/**
 * A lightweight writing buffer to reduce the amount of write operations to be performed on the
 * underlying writer. This implementation is not thread-safe. It deliberately deviates from the
 * contract of Writer. In particular, it does not flush or close the wrapped writer nor does it
 * ensure that the wrapped writer is open.
 */
internal class BufferedWriter(private val writer: Writer, bufferSize: Int = 16): Writer() {
    private val buffer = CharArray(bufferSize)
    private var fill = 0

    override fun write(c: Int) {
        if (fill > buffer.size - 1) {
            flush()
        }
        buffer[fill++] = c.toChar()
    }

    override fun write(cbuf: CharArray, off: Int, len: Int) {
        if (fill > buffer.size - len) {
            flush()
            if (len > buffer.size) {
                writer.write(cbuf, off, len)
                return
            }
        }
        System.arraycopy(cbuf, off, buffer, fill, len)
        fill += len
    }

    override fun write(str: String, off: Int, len: Int) {
        if (fill > buffer.size - len) {
            flush()
            if (len > buffer.size) {
                writer.write(str, off, len)
                return
            }
        }
        str.toCharArray(buffer, fill, off, off + len)
        fill += len
    }

    /**
     * Flushes the internal buffer but does not flush the wrapped writer.
     */
    override fun flush() {
        writer.write(buffer, 0, fill)
        fill = 0
    }

    /**
     * Does not close or flush the wrapped writer.
     */
    override fun close() {}
}
