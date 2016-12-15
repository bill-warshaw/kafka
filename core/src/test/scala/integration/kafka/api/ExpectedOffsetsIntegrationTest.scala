package integration.kafka.api

import kafka.integration.KafkaServerTestHarness
import kafka.server.KafkaConfig
import kafka.utils.TestUtils
import org.apache.kafka.clients.producer.ProducerRecord
import org.junit.Test

class ExpectedOffsetsIntegrationTest extends KafkaServerTestHarness {


  def generateConfigs() =
    TestUtils.createBrokerConfigs(1, zkConnect, false).map(KafkaConfig.fromProps)

  @Test
  def testBaseOffsetIsGood(): Unit = {
    val producer = TestUtils.createNewProducer(brokerList, checkOffsets = true)
    val record = new ProducerRecord[Array[Byte], Array[Byte]]("topic", 0, System.currentTimeMillis(), "key".getBytes, "value".getBytes, 0L)
    val future = producer.send(record)
    future.get()
  }

  @Test
  def testBaseOffsetIsWrong(): Unit = {
    val producer = TestUtils.createNewProducer(brokerList, checkOffsets = true)
    val record = new ProducerRecord[Array[Byte], Array[Byte]]("topic", 0, System.currentTimeMillis(), "key".getBytes, "value".getBytes, 999L)
    val future = producer.send(record)
    future.get()
  }
}
