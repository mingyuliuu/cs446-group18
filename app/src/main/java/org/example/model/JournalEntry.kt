package org.example.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId
import java.time.LocalDate

class JournalEntry : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var landmark: Landmark? = null
    var visitedAt: LocalDate? = null
    var photos: List<String>? = mutableListOf()
    var description: String? = ""
}