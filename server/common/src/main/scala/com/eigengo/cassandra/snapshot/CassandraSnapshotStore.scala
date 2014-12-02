package com.eigengo.cassandra.snapshot

import akka.persistence.cassandra.snapshot.{CassandraSnapshotStoreConfig, CassandraSnapshotStore => KCassandraSnapshotStore}
import akka.serialization.SerializationExtension
import com.typesafe.config.Config

// FIXME: once the upstream library code has been updated, we can delete this file

class CassandraSnapshotStore(configObject: Config) extends KCassandraSnapshotStore  {

  override val config = new CassandraSnapshotStoreConfig(configObject)
  override val serialization = SerializationExtension(context.system)

  import config._

  override val cluster = clusterBuilder.build
  override val session = cluster.connect()

  session.execute(createKeyspace(replicationFactor))
  session.execute(createTable)

  override val preparedWriteSnapshot = session.prepare(writeSnapshot).setConsistencyLevel(writeConsistency)
  override val preparedDeleteSnapshot = session.prepare(deleteSnapshot).setConsistencyLevel(writeConsistency)
  override val preparedSelectSnapshot = session.prepare(selectSnapshot).setConsistencyLevel(readConsistency)

  override val preparedSelectSnapshotMetadataForLoad =
    session.prepare(selectSnapshotMetadata(limit = Some(maxMetadataResultSize))).setConsistencyLevel(readConsistency)

  override val preparedSelectSnapshotMetadataForDelete =
    session.prepare(selectSnapshotMetadata(limit = None)).setConsistencyLevel(readConsistency)

}
