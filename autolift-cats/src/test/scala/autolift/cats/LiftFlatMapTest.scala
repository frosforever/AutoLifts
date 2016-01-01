package autolift.cats

import cats.implicits._
import autolift.Cats._

class LiftFlatMapTest extends BaseSpec {

  val intListF = { x: Int => List(x + 1) }
  val anyOptionF = { x: Any => Option(1) }
  val anyListF = { x: Any => List(1) }

  "liftFlatMap on an Option[List]" should "work" in {
    val in = Option(List(1))
    val out = in liftFlatMap intListF

    same[Option[List[Int]]](out, Option(List(2)))
  }

  "liftFlatMap on an Option[List]" should "work with functions" in {
    val in = Option(List(1, 2))
    val out = in liftFlatMap anyOptionF

    same[Option[Int]](out, Option(1))
  }

  "LiftedFlatMap" should "work on a List" in {
    val lf = liftFlatMap(intListF)
    val out = lf(List(1))

    same[List[Int]](out, List(2))
  }

  "LiftedFlatMap" should "work on an Option[List]" in {
    val lf = liftFlatMap(intListF)
    val out = lf(Option(List(1)))

    same[Option[List[Int]]](out, Option(List(2)))
  }

  "LiftedFlatMap on a List" should "work with functions" in {
    val lf = liftFlatMap(anyListF)
    val out = lf(List(1, 2, 3))

    same[List[Int]](out, List(1, 1, 1))
  }

  "LiftedFlatMap" should "map" in {
    val lf = liftFlatMap(intListF) map (_ + 1)
    val out = lf(List(0, 1, 2))

    same[List[Int]](out, List(2, 3, 4))
  }

  "LiftedFlatMap" should "andThen with other LiftMap" in {
    val lf = liftFlatMap(anyListF)
    val lf2 = liftFlatMap(intListF)
    val comp = lf andThen lf2
    val out = comp(List(4))

    same[List[Int]](out, List(2))
  }

  "LiftedMap" should "compose with other LiftMap" in {
    val lf = liftFlatMap(anyListF)
    val lf2 = liftFlatMap(intListF)
    val comp = lf2 compose lf
    val out = comp(List(4))

    same[List[Int]](out, List(2))
  }
}