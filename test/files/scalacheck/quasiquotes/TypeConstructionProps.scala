import org.scalacheck._
import Prop._
import Gen._
import Arbitrary._

import scala.reflect.runtime.universe._
import Flag._

object TypeConstructionProps extends QuasiquoteProperties("type construction")  {
  property("bare idents contain type names") = test {
    tq"x" ≈ Ident(TypeName("x"))
  }

  property("splice type names into AppliedTypeTree") = forAll { (name1: TypeName, name2: TypeName) =>
    tq"$name1[$name2]" ≈ AppliedTypeTree(Ident(name1), List(Ident(name2)))
  }

  property("tuple type") = test {
    val empty = List[Tree]()
    val ts = List(tq"t1", tq"t2")
    assert(tq"(..$empty)" ≈ tq"scala.Unit")
    assert(tq"(..$ts)" ≈ tq"Tuple2[t1, t2]")
    assert(tq"(t0, ..$ts)" ≈ tq"Tuple3[t0, t1, t2]")
  }
}