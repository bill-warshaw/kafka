/**
  * Licensed to the Apache Software Foundation (ASF) under one or more
  * contributor license agreements.  See the NOTICE file distributed with
  * this work for additional information regarding copyright ownership.
  * The ASF licenses this file to You under the Apache License, Version 2.0
  * (the "License"); you may not use this file except in compliance with
  * the License.  You may obtain a copy of the License at
  *
  *    http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */

package kafka.log

import kafka.message.MessageAndOffset

class DefaultCompactionPredicate(config: LogConfig) extends CompactionPredicate(config) {

  override def shouldRetainMessage(source: LogSegment, map: OffsetMap, retainDeletes: Boolean, entry: MessageAndOffset, stats: CleanerStats): Boolean = {
    val key = entry.message.key
    if (key != null) {
      val foundOffset = map.get(key)
      /* two cases in which we can get rid of a message:
       *   1) if there exists a message with the same key but higher offset
       *   2) if the message is a delete "tombstone" marker and enough time has passed
       */
      val redundant = foundOffset >= 0 && entry.offset < foundOffset
      val obsoleteDelete = !retainDeletes && entry.message.isNull
      !redundant && !obsoleteDelete
    } else {
      stats.invalidMessage()
      false
    }
  }
}
