package com.social.network.utils

import cats.MonadThrow
import com.github.gekomad.scalacompress.Compressors.{StreamableCompressor, compressStream, decompressStream}

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}
import scala.util.{Failure, Success}

object ImageCompressor {

  def compress[F[_]](input: Array[Byte])(implicit F: MonadThrow[F]): F[Array[Byte]] = {
    val in = new ByteArrayInputStream(input)
    val out = new ByteArrayOutputStream()
    compressStream(StreamableCompressor.ZSTANDARD, in, out) match {
      case Failure(exception) => F.raiseError(exception)
      case Success(_) => F.pure(out.toByteArray)
    }
  }

  def decompress[F[_]](input: Array[Byte])(implicit F: MonadThrow[F]): F[Array[Byte]] = {
    val in = new ByteArrayInputStream(input)
    val out = new ByteArrayOutputStream()
    decompressStream(StreamableCompressor.ZSTANDARD, in, out) match {
      case Failure(exception) => F.raiseError(exception)
      case Success(_) => F.pure(out.toByteArray)
    }
  }
}
