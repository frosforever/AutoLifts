package autolift.cats

import autolift.LiftAp
import cats.{Functor, Applicative}
import export._

trait CatsLiftAp[Obj, Fn] extends LiftAp[Obj, Fn]

@exports(Subclass)
object CatsLiftAp extends LowPriorityCatsLiftAp {
  def apply[Obj, Fn](implicit lift: CatsLiftAp[Obj, Fn]): Aux[Obj, Fn, lift.Out] = lift

  @export(Subclass)
  implicit def base[F[_], A, B](implicit ap: Applicative[F]): Aux[F[A], F[A => B], F[B]] =
    new CatsLiftAp[F[A], F[A => B]]{
      type Out = F[B]

      def apply(fa: F[A], ff: F[A => B]): F[B] = ap.map2(fa, ff){ (a, f) => f(a) }
    }
}

trait LowPriorityCatsLiftAp{
  type Aux[Obj, Fn, Out0] = CatsLiftAp[Obj, Fn]{ type Out = Out0 }

  @export(Subclass)
  implicit def recur[F[_], G, Fn](implicit functor: Functor[F], lift: LiftAp[G, Fn]): Aux[F[G], Fn, F[lift.Out]] =
    new CatsLiftAp[F[G], Fn]{
      type Out = F[lift.Out]

      def apply(fg: F[G], f: Fn) = functor.map(fg){ g: G => lift(g, f) }
    }
}

final class LiftedAp[F[_], A, B](protected val f: F[A => B])(implicit ap: Applicative[F]){
  def andThen[C >: B, D](lf: LiftedAp[F, C, D]) = new LiftedAp(ap.map2(f, lf.f){
    (f1, f2) => f1 andThen f2
  })

  def compose[C, D <: A](lf: LiftedAp[F, C, D]) = lf andThen this

  def map[C](g: B => C): LiftedAp[F, A, C] = new LiftedAp(ap.map(f){ _ andThen g })

  def apply[That](that: That)(implicit lift: LiftAp[That, F[A => B]]): lift.Out = lift(that, f)
}

trait LiftApContext{
  def liftAp[A, B, F[_]](f: F[A => B])(implicit ap: Applicative[F]) = new LiftedAp(f)
}
