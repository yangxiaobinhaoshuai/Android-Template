package com.example.java_io

import java.io.OutputStream

/**
 * progress: 1~100
 */
typealias ProgressListener = (progress: Int) -> Unit
class ProgressOutputStream(
	private val totalSize: Long,
	private val outputStream: OutputStream,
	private val progressListener:ProgressListener
) : OutputStream() {

	private var totalBytesWritten = 0L

	init {
	    if (totalSize == 0L) throw IllegalArgumentException("dividend can NOT be 0.")
	}

	override fun write(b: Int) {
		outputStream.write(b)
		totalBytesWritten++
		progressListener.invoke((totalBytesWritten * 100 / totalSize).toInt())
	}

	override fun write(b: ByteArray?) {
		super.write(b)
		outputStream.write(b)
	}

	override fun write(b: ByteArray?, off: Int, len: Int) {
		outputStream.write(b, off, len)
		totalBytesWritten += len
		progressListener.invoke((totalBytesWritten * 100 / totalSize).toInt())
	}

	override fun flush() {
		super.flush()
		outputStream.flush()
	}

	override fun close() {
		super.close()
		outputStream.close()
	}
}
