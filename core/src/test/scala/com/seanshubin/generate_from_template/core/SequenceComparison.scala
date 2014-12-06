package com.seanshubin.generate_from_template.core

import scala.annotation.tailrec

sealed trait SequenceComparisonResult[+T] {
  def areSame: Boolean

  def toMultipleLineString: Seq[String]
}

object SequenceComparisonResult {

  case object Same extends SequenceComparisonResult[Nothing] {
    def areSame = true

    def toMultipleLineString = Seq()
  }

  case class Missing[T](index: Int, actualSeq: Seq[T], expectedSeq: Seq[T]) extends SequenceComparisonResult[T] {
    def areSame = false

    def toMultipleLineString = {
      val element = expectedSeq(index)
      val header = s"missing element at index $index: $element"
      Seq(header)
    }
  }

  case class Extra[T](index: Int, actualSeq: Seq[T], expectedSeq: Seq[T]) extends SequenceComparisonResult[T] {
    def areSame = false

    def toMultipleLineString = {
      val element = actualSeq(index)
      val header = s"extra element at index $index: $element"
      Seq(header)
    }
  }

  case class Difference[T](index: Int, actualSeq: Seq[T], expectedSeq: Seq[T]) extends SequenceComparisonResult[T] {
    def areSame = false

    def toMultipleLineString = {
      val actual = actualSeq(index)
      val expected = expectedSeq(index)
      val header = s"difference at index $index"
      Seq(header,
        s"actual  : $actual",
        s"expected: $expected")
    }
  }

}

object SequenceComparison {
  def compare[T](actualSeq: Seq[T], expectedSeq: Seq[T]): SequenceComparisonResult[T] = {
    import com.seanshubin.generate_from_template.core.SequenceComparisonResult._

    @tailrec
    def compareLists(index: Int, remainingActual: List[T], remainingExpected: List[T]): SequenceComparisonResult[T] = {
      (remainingActual.headOption, remainingExpected.headOption) match {
        case (Some(actual), Some(expected)) =>
          if (actual == expected) compareLists(index + 1, remainingActual.tail, remainingExpected.tail)
          else Difference[T](index, actualSeq, expectedSeq)
        case (Some(actual), None) => Extra(index, actualSeq, expectedSeq)
        case (None, Some(expected)) => Missing(index, actualSeq, expectedSeq)
        case (None, None) => Same
      }
    }
    compareLists(0, actualSeq.toList, expectedSeq.toList)
  }
}
