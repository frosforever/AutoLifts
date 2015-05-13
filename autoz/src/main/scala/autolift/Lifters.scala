package autolift

import scalaz.{Functor, Bind}

//TODO: Better names
//autoLift
//autoLiftM

object Lifters extends Lifters

trait Lifters{
	def liftF[F[_]] = new LIFMaker[F]

	sealed class LIFMaker[F[_]]{
		final def apply[Function](f: Function)(implicit ev: Functor[F]) = new LiftIntoFunctor[F, Function](f)
	}

	def liftM[M[_]] = new LIBMaker[M]

	sealed class LIBMaker[F[_]]{
		final def apply[Function](f: Function)(implicit ev: Functor[F]) = new LiftIntoBind[F, Function](f)
	}
}

class LiftIntoFunctor[F[_]: Functor, Function](f: Function){
	def apply[That](that: That)(implicit into: LiftIntoF[F, That, Function]): into.Out = into(that, f)
}

trait LiftIntoF[F[_], Obj, Function]{
	type Out

	def apply(obj: Obj, f: Function): Out
}

object LiftIntoF extends LowPriorityLiftIntoF{
	def apply[F[_], Obj, Function](implicit lift: LiftIntoF[F, Obj, Function]): Aux[F, Obj, Function, lift.Out] = lift

	implicit def base[F[_], A, C >: A, B](implicit functor: Functor[F]): Aux[F, F[A], C => B, F[B]] =
		new LiftIntoF[F, F[A], C => B]{
			type Out = F[B]

			def apply(fa: F[A], f: C => B) = functor.map(fa)(f)
		}
}

trait LowPriorityLiftIntoF{
	type Aux[F[_], Obj, Function, Out0] = LiftIntoF[F, Obj, Function]{ type Out = Out0 }

	implicit def recur[F[_], G[_], In, Function](implicit functor: Functor[G], lift: LiftIntoF[F, In, Function]): Aux[F, G[In], Function, G[lift.Out]] =
		new LiftIntoF[F, G[In], Function]{
			type Out = G[lift.Out]

			def apply(gin: G[In], f: Function) = functor.map(gin){ in: In => lift(in, f) }
		}
}

class LiftIntoBind[M[_]: Functor, Function](f: Function){
	def apply[That](that: That)(implicit into: LiftIntoB[M, That, Function]): into.Out = into(that, f)
}

trait LiftIntoB[M[_], Obj, Function]{
	type Out

	def apply(obj: Obj, f: Function): Out
}

object LiftIntoB extends LowPriorityLiftIntoB{
	def apply[M[_], Obj, Function](implicit lift: LiftIntoB[M, Obj, Function]): Aux[M, Obj, Function, lift.Out] = lift

	implicit def base[M[_], A, C >: A, B](implicit bind: Bind[M]): Aux[M, M[A], C => M[B], M[B]] =
		new LiftIntoB[M, M[A], C => M[B]]{
			type Out = M[B]

			def apply(fa: M[A], f: C => M[B]) = bind.bind(fa)(f)
		}
}

trait LowPriorityLiftIntoB{
	type Aux[F[_], Obj, Function, Out0] = LiftIntoB[F, Obj, Function]{ type Out = Out0 }

	implicit def recur[F[_], G[_], In, Function](implicit functor: Functor[G], lift: LiftIntoF[F, In, Function]): Aux[F, G[In], Function, G[lift.Out]] =
		new LiftIntoB[F, G[In], Function]{
			type Out = G[lift.Out]

			def apply(gin: G[In], f: Function) = functor.map(gin){ in: In => lift(in, f) }
		}
}