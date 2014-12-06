package com.seanshubin.generate_from_template.core

case class SetComparisonResult[T](missing: Set[T], extra: Set[T]) {
  def areSame = missing.isEmpty && extra.isEmpty

  def toMultipleLineString = if (areSame) Seq()
  else {
    Seq("missing") ++ setToStrings(missing) ++ Seq("extra") ++ setToStrings(extra)
  }

  def setToStrings(set: Set[T]): Seq[String] = {
    set.map(_.toString).map(indent).toSeq.sorted
  }

  def indent(s: String): String = s"  $s"
}

object SetComparison {
  def compare[T](actualSet: Set[T], expectedSet: Set[T]): SetComparisonResult[T] = {
    val missing = expectedSet -- actualSet
    val extra = actualSet -- expectedSet
    SetComparisonResult(missing, extra)
  }
}
