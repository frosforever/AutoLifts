package autolift.cats

import cats.{FlatMap, Functor}
import autolift.LiftFlatMap
import export._


trait CatsLiftFlatMap[Obj, Fn] extends LiftFlatMap[Obj, Fn]

@exports(Subclass)
object CatsLiftFlatMap extends LowPriorityCatsLiftFlatMap {
  def apply[Obj, Fn](implicit lift: CatsLiftFlatMap[Obj, Fn]): Aux[Obj, Fn, lift.Out] = lift

  @export(Subclass)
  implicit def base[M[_], A, C >: A, B](implicit flatMap: FlatMap[M]): Aux[M[A], C => M[B], M[B]] =
    new CatsLiftFlatMap[M[A], C => M[B]]{
      type Out = M[B]

      def apply(fa: M[A], f: C => M[B]) = flatMap.flatMap(fa)(f)
    }
}

trait LowPriorityCatsLiftFlatMap{
  type Aux[Obj, Fn, Out0] = CatsLiftFlatMap[Obj, Fn]{ type Out = Out0 }

  @export(Subclass)
  implicit def recur[F[_], G, Fn](implicit functor: Functor[F], lift: LiftFlatMap[G, Fn]): Aux[F[G], Fn, F[lift.Out]] =
    new CatsLiftFlatMap[F[G], Fn]{
      type Out = F[lift.Out]

      def apply(fg: F[G], f: Fn) = functor.map(fg){ g: G => lift(g, f) }
    }
}

final class LiftedFlatMap[A, B, M[_]](protected val f: A => M[B])(implicit flatMap: FlatMap[M]){
  def andThen[C >: B, D](that: LiftedFlatMap[C, D, M]) = new LiftedFlatMap({ x: A => flatMap.flatMap(f(x))(that.f) })

  def compose[C, D <: A](that: LiftedFlatMap[C, D, M]) = that andThen this

  def map[C](g: B => C): LiftedFlatMap[A, C, M] = new LiftedFlatMap({ x: A => flatMap.map(f(x))(g) })

  def apply[That](that: That)(implicit lift: LiftFlatMap[That, A => M[B]]): lift.Out = lift(that, f)
}

trait LiftedFlatMapImplicits{
  implicit def liftedFlatMapFunctor[A, M[_]] = new Functor[LiftedFlatMap[A, ?, M]]{
    def map[B, C](lb: LiftedFlatMap[A, B, M])(f: B => C) = lb map f
  }
}

trait LiftFlatMapContext{
  def liftFlatMap[A, B, M[_]](f: A => M[B])(implicit flatMap: FlatMap[M]) = new LiftedFlatMap(f)
}
