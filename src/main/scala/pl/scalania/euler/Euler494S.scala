package pl.scalania.euler

import pl.scalania.euler.euler494.Change
import pl.scalania.euler.euler494.Change.{Up__, Down}

import scala.annotation.tailrec

object Euler494S {
  val maxPower = 30

  def getFamilies(length: Int): Set[List[Change]] = {
    @tailrec
    def countRec(powLeft: Int, n: Long, acu: Set[List[Change]]): Set[List[Change]] = {
      if (powLeft == 0)
        acu
      else {
        val newAcu = acu ++ tree(n, length).map(_.families).getOrElse(Set())
        countRec(powLeft - 1, n * 2L, newAcu)
      }
    }
    countRec(maxPower, 4, Set())
  }

  def countFamilies(length: Int): Int = getFamilies(length).size


  def tree(n: Long, depth: Int): Option[Node] = {
    def treeOdd(n: Long, depth: Int): Option[Node] =
      depth match {
        case 1 => Some(Node(n, None, None))
        case _ =>
          val bigger = treeOdd(n * 2L, depth - 1)
          val smaller = {
            if (canGetSmaller(n))
              treeOdd((n - 1L) / 3L, depth - 1)
            else
              None
          }
          if (bigger.nonEmpty || smaller.nonEmpty)
            Some(Node(n, bigger, smaller))
          else
            None
      }
    def canGetSmaller(n: Long): Boolean = {
      (n - 1L) % 3L == 0L && ((n - 1L) / 3L) % 2L == 1L
    }

    if (canGetSmaller(n)) {
      val newN: Long = (n - 1L) / 3L
      treeOdd(newN, depth)
    }
    else
      None
  }

  case class Node(n: Long, bigger: Option[Node], smaller: Option[Node]) {

    def families: Set[List[Change]] = {
      def withNilIfLeaf: Set[List[Change]] = {
        if (isLeaf)
          Set(Nil)
        else
          Set()
      }
      val bs: Set[List[Change]] = if (bigger.nonEmpty) bigger.get.families.map(_ :+ Down()) else withNilIfLeaf
      val ss: Set[List[Change]] = if (smaller.nonEmpty) smaller.get.families.map(_ :+ Up__()) else withNilIfLeaf
      bs ++ ss
    }

    def isLeaf: Boolean = {
      bigger.isEmpty && smaller.isEmpty
    }
  }

}
