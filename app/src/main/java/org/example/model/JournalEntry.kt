package org.example.model

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class JournalEntry : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var landmark: Landmark? = null
    var visitedAt: String? = null // TODO: Use proper Date object
    var photos: RealmList<String> = realmListOf()
    var description: String? = ""
}