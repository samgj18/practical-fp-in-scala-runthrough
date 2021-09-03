package shop.infrastructure.ext.http4s

import shop.infrastructure.ext.derevo._

import org.http4s.QueryParamDecoder

object queryParam extends Derive[QueryParamDecoder]
