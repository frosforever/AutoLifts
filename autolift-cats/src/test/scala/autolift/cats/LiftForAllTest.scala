package autolift.cats

import cats._
import cats.implicits._
import autolift.Cats._
import autolift._

class LiftForAllTest extends BaseSpec{
	case class Bar[A](a: A)

	implicit val fn = new Functor[Bar]{
		def map[A, B](fa: Bar[A])(f: A => B) = Bar(f(fa.a))
	}

	"liftForAll" should "work on a List" in{
		val out = List(1, 2, 3).liftForall(even)

		same[Boolean](out, false)
	}

	"liftForAll" should "work on a List[Option]" in{
		val in = List(Option(1), None, Option(3))
		val out = in liftForall even

		same[List[Boolean]](out, List(false, true, false))
	}

	"liftForAll" should "work with functions" in{
		val in = Bar(List(1, 2, 3))
		val out = in liftForall any

		same[Bar[Boolean]](out, Bar(false))
	}
}