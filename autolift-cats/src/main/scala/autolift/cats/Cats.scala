package autolift

import export._
import autolift.cats._

@reexports[CatsLiftMap, CatsLiftAp, CatsLiftFlatMap, CatsLiftFlatten, CatsLiftFilter,/* LiftJoin, LiftJoinWith, */
  CatsLiftExists,
  CatsLiftA2, CatsLiftA3, CatsLiftA4, CatsLiftA5,
  CatsLiftM2, CatsLiftM3, CatsLiftM4, CatsLiftM5]
object Cats extends Syntax with Context{
  implicit def mkF[Obj, Fn](implicit lift: CatsLiftMap[Obj, Fn]): CatsLiftMap.Aux[Obj, Fn, lift.Out] = lift
  implicit def mkAp[Obj, Fn](implicit lift: CatsLiftAp[Obj, Fn]): CatsLiftAp.Aux[Obj, Fn, lift.Out] = lift
  implicit def mkFM[Obj, Fn](implicit lift: CatsLiftFlatMap[Obj, Fn]): CatsLiftFlatMap.Aux[Obj, Fn, lift.Out] = lift
  implicit def mkFl[M[_], Obj](implicit lift: CatsLiftFlatten[M, Obj]): CatsLiftFlatten.Aux[M, Obj, lift.Out] = lift
  implicit def mkExists[Obj, Fn](implicit lift: CatsLiftExists[Obj, Fn]): CatsLiftExists.Aux[Obj, Fn, lift.Out] = lift
  //implicit def mkJ[Obj1, Obj2](implicit lift: LiftJoin[Obj1, Obj2]): LiftJoin.Aux[Obj1, Obj2, lift.Out] = lift
  //implicit def mkJw[Obj1, Obj2, Fn](implicit lift: LiftJoinWith[Obj1, Obj2, Fn]): LiftJoinWith.Aux[Obj1, Obj2, Fn, lift.Out] = lift
}