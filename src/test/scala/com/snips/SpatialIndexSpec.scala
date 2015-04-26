package com.snips

import scala.collection.mutable.ArrayBuffer

import org.junit.runner.RunWith
import org.specs2.mutable._
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class SnipsSpatialIndexSpec extends Specification {
  
	"The spatial index " should {

	    " return the closest point with a predefined configuration " in {
         closest.name === "one"
	    }
	}
}
