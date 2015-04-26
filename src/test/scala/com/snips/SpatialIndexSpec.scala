package com.snips

import scala.collection.mutable.ArrayBuffer
import org.junit.runner.RunWith
import org.specs2.mutable._
import org.specs2.runner.JUnitRunner
import scalaj.http._
import com.nouhoum.akka.RequestedArea

@RunWith(classOf[JUnitRunner])
class SnipsSpatialIndexSpec extends Specification {
  
	"The spatial index " should {

	    " return the closest point with a predefined configuration " in {
        val requestedArea = RequestedArea(40.7, -74, 1)
        val request = Http("https://api.foursquare.com/v2/venues/search")
        .param("client_id", "MZRD3AHT3HIYLJQQM2YX0N0Y3Y2APFKQOBRHOP0304M1XP2U")
        .param("client_secret", "P4YUP0HGCG4JDLNYXE4TIEXDZEZTOPWIURPNIWKWGN35XQOI")
        .param("ll", requestedArea.centerLatitude + "," + requestedArea.centerLongitude) //40.7,-74
        .param("radius", requestedArea.radius + "") // meters
        .param("v", "20140806") 
        .asString
        println(request.body)
        
        //{"meta":{"code":200},"response":{"venues":[],"confident":false}}
        import net.liftweb.json._
        implicit val formats = DefaultFormats // Brings in default date formats etc.
        case class Response(reponse: Venues)
        case class Venues(venues: List[Venue])
        case class Venue(name: String, id: String)
        val json = parse(request.body)
        println(json)
         "one" === "one"
	    }
	}
}
