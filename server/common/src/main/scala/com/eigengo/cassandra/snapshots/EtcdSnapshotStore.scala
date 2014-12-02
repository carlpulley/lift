package com.eigengo.cassandra

package snapshots

import akka.actor.Props

class EtcdSnapshotStore extends EtcdConfig(config => Props(new CassandraSnapshotStore(config)), "cassandra-snapshot-store")
