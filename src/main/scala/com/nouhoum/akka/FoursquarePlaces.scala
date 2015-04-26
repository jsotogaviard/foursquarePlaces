package com.nouhoum.akka

import scala.collection.mutable.ListBuffer
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.impl.Future
import scala.concurrent.impl.Future
import scala.concurrent.impl.Future

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Props
import akka.pattern.ask
import akka.util.Timeout


/**
 * https://api.foursquare.com/v2/venues/search?
 * client_id=CLIENT_ID&
 * client_secret=CLIENT_SECRET&
 * v=20130815&
 * ll=40.7,-74&
 */

object MainApp extends App {
  
  val FOURSQUARE_LIMIT = 50
  
  val FOURSQUARE_ASKERS = 10
  
  if (args.length == 0) {
    printUsage()
  } else {
    go(args)
  }

  def go(args: Array[String]) = {
    
    
    val start = System.currentTimeMillis

//    val data =
//      (for (file <- args) yield Source.fromFile(file).mkString).toList
//
//    val system = ActorSystem("phone-number")
//    val phoneNumberParser: ActorRef = system.actorOf(Props[Master], "phone-number-parser")
//
//    phoneNumberParser ! Task("Toto")
//
//    phoneNumberParser ! StartProcessing(data, 3)

    println("Total time : " + (System.currentTimeMillis - start) + " ms")
    println("=========================")
  }

  private def printUsage() {
  }

  
}

sealed trait Message

case class RequestedArea(
    centerLatitude : Double, 
    centerLongitude : Double, 
    radius : Double ) extends Message

case class VerifyRequestedArea(
    centerLatitude : Double, 
    centerLongitude : Double, 
    radius : Double) extends Message{
  
  def this(requestedArea : RequestedArea) = 
    this(requestedArea.centerLatitude, requestedArea.centerLongitude, requestedArea.radius)   
}

case class FoursquarePlace(id : String, name : String)
    
case class FoursquarePlaces(places : List[FoursquarePlace]) extends Message

class Director extends Actor {
  
//   val router = context.system.actorOf(Props[FoursquareAsker]
//    .withRouter(RoundRobinRouter(nrOfInstances = MainApp.FOURSQUARE_ASKERS)))
  
  override def receive = {
    case RequestedArea(centerLatitude, centerLongitude, radius) => {
      
      // Forward it to a foursquare asker
      val requestedArea = RequestedArea(centerLatitude, centerLongitude, radius)
      context.actorOf(Props[FoursquareAsker]) ! requestedArea
    }
    case FoursquarePlaces(places) => {
      // Enqueue it in 
      // in the result places
      
    }
    case _ => {
       println("wrong question")     
    }
  }
}

class FoursquareAsker extends Actor {
  
  override def receive = {
    case RequestedArea(centerLatitude, centerLongitude, radius) => {
      
      // Compute the division
      // in four parts of the current coordinates
      val requestedArea = RequestedArea(centerLatitude, centerLongitude, radius)
      val dividedAreas = divideInFour(requestedArea)
      
      // Send a request to foursquare
      // Get the resulting foursquare places
      val foursquarePlaces : List[FoursquarePlace] = foursquareQuery(requestedArea)
      if(foursquarePlaces.length <= MainApp.FOURSQUARE_LIMIT){
        
        // There are less than the 
        // the limit results
        // we can now
        // verify the results
        // Send the verify request tasks
        val futures = ListBuffer()
        implicit val timeout = Timeout(5 seconds) 
        for(dividedArea <- dividedAreas){
           val verifyRequestedArea = new VerifyRequestedArea(dividedArea)
        	 val future =  context.actorOf(Props[FoursquareAsker]) ? verifyRequestedArea
           futures += future
        }
        
        // Wait for the completion
        // for each one of them
        var nbPlaces = 0
        for(future <- futures){
           val result = Await.result(future, timeout.duration).asInstanceOf[FoursquarePlaces]
           nbPlaces += result.places.length
        }
        if(nbPlaces == foursquarePlaces.length){
          
          // Send the results
          // to the director
        } else {
          
          // The initial requested area
          // goes back to the whole 
          // request process
        }
       
      } else {
        
        // There are more results than the limit
        // We need to divide
        // the query in four
        // and send them to four square actors
        for(dividedArea <- dividedAreas){
           context.actorOf(Props[FoursquareAsker]) ! dividedArea
        }
      }
    }
    case VerifyRequestedArea(centerLatitude, centerLongitude, radius) => {
      
      // We need to do the query
      // and send it back to 
      // the sender
      val requestedArea = RequestedArea(centerLatitude, centerLongitude, radius)
      val foursquarePlaces : List[FoursquarePlace] = foursquareQuery(requestedArea)
      sender ! FoursquarePlaces(foursquarePlaces)
    }
    case _ => {
       println("wrong question")     
    }
  }
  
  def divideInFour(initialArea : RequestedArea) : List[RequestedArea] = {
    
    // Divide in four the given region
  }
  
  def foursquareQuery(requestedArea : RequestedArea) : List[FoursquarePlace] = {
//    https://api.foursquare.com/v2/venues/search?
// * client_id=CLIENT_ID&
// * client_secret=CLIENT_SECRET&
// * v=20130815&
// * ll=40.7,-74&
    val request = Http("https://api.foursquare.com/v2/venues/search")
        .param("client_id", "monkeys")
        .param("client_secret", "monkeys")
        .param("ll", "monkeys") //40.7,-74
        .asString
    val places = ListBuffer[FoursquarePlace]()
    return null
  }
}
