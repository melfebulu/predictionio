package io.prediction.api

import io.prediction.core.BasePreparator
import io.prediction.EmptyParams
import io.prediction.BaseParams
import org.apache.spark.rdd.RDD

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import scala.reflect._

abstract class LPreparator[
    PP <: BaseParams : ClassTag, TD, PD: ClassTag]
  extends BasePreparator[PP, RDD[TD], RDD[PD]] {

  def prepareBase(sc: SparkContext, rddTd: RDD[TD]): RDD[PD] = {
    rddTd.map(prepare)
  }
  
  def prepare(trainingData: TD): PD
}

abstract class PPreparator[PP <: BaseParams : ClassTag, TD, PD]
  extends BasePreparator[PP, TD, PD] {

  def prepareBase(sc: SparkContext, td: TD): PD = {
    prepare(sc, td)
    // TODO: Optinally check pd size. Shouldn't exceed a few KB.
  }
  
  def prepare(sc: SparkContext, trainingData: TD): PD
}

/******* Helper Functions ******/
class IdentityPreparator[TD] extends BasePreparator[EmptyParams, TD, TD] {
  def prepareBase(sc: SparkContext, td: TD): TD = td
}
    
