package autolift.cats

import autolift._

//TODO: ScalaDocs

trait Context //extends LiftJoinWithContext
  extends LiftApContext
  with LiftMapContext
  with LiftFlatMapContext
  with LiftFilterContext
  with LiftExistsContext
  //with LiftA2Context
  //with LiftA3Context
  //with LiftA4Context
  //with LiftA5Context
  with LiftM2Context
  with LiftM3Context
  with LiftM4Context
  with LiftM5Context