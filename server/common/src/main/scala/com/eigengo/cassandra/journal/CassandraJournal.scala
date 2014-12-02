package com.eigengo.cassandra.journal

import akka.persistence._
import akka.persistence.cassandra.journal.{CassandraJournal => KCassandraJournal, CassandraJournalConfig}
import akka.serialization.SerializationExtension
import com.typesafe.config.Config

// FIXME: once the upstream library code has been updated, we can delete this file

class CassandraJournal(configObject: Config) extends KCassandraJournal {

  override val config = new CassandraJournalConfig(configObject)
  override val serialization = SerializationExtension(context.system)
  override val persistence = Persistence(context.system)

  import config._

  override val cluster = clusterBuilder.build
  override val session = cluster.connect()

  session.execute(createKeyspace(replicationFactor))
  session.execute(createTable)

  override val preparedWriteHeader = session.prepare(writeHeader)
  override val preparedWriteMessage = session.prepare(writeMessage)
  override val preparedConfirmMessage = session.prepare(confirmMessage)
  override val preparedDeleteLogical = session.prepare(deleteMessageLogical)
  override val preparedDeletePermanent = session.prepare(deleteMessagePermanent)
  override val preparedSelectHeader = session.prepare(selectHeader).setConsistencyLevel(readConsistency)
  override val preparedSelectMessages = session.prepare(selectMessages).setConsistencyLevel(readConsistency)

}
