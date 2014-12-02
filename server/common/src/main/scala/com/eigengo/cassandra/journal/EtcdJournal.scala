package com.eigengo.cassandra

package journal

import akka.actor.Props

class EtcdJournal extends EtcdConfig(config => Props(new CassandraJournal(config)), "cassandra-journal")
