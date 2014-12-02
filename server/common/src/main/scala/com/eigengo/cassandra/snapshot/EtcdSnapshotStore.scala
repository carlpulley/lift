package com.eigengo.cassandra

package snapshot

import akka.actor.Props

class EtcdSnapshotStore extends EtcdConfig(config => Props(new CassandraSnapshotStore(config)), "cassandra-snapshot-store")
