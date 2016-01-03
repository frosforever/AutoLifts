package autolift.cats

import autolift.LiftMap
import cats.Functor
import export._

trait CatsLiftMap[Obj, Fn] extends LiftMap[Obj, Fn]

@exports(Subclass)
object CatsLiftMap extends LowPriorityCatsLiftMap{
  def apply[Obj, Fn](implicit lift: CatsLiftMap[Obj, Fn]): Aux[Obj, Fn, lift.Out] = lift

  @export(Subclass)
  implicit def base[F[_], A, C >: A, B](implicit functor: Functor[F]): Aux[F[A], C => B, F[B]] =
    new CatsLiftMap[F[A], C => B]{
      type Out = F[B]

      def apply(fa: F[A], f: C => B) = functor.map(fa)(f)
    }
}

trait LowPriorityCatsLiftMap{
  type Aux[Obj, Fn, Out0] = CatsLiftMap[Obj, Fn]{ type Out = Out0 }

  @export(Subclass)
  implicit def recur[F[_], G, Fn](implicit functor: Functor[F], lift: LiftMap[G, Fn]): Aux[F[G], Fn, F[lift.Out]] =
    new CatsLiftMap[F[G], Fn]{
      type Out = F[lift.Out]

      def apply(fg: F[G], f: Fn) = functor.map(fg){ g: G => lift(g, f) }
    }
}