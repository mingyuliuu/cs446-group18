package ca.uwaterloo.treklogue.data.model

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class JournalEntry() : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var ownerId: String = ""
    var landmark: Landmark? = null
    var visitedAt: String = "" // TODO: Use proper Date object
    var photos: RealmList<String> = realmListOf()
    var description: String = ""

    constructor(
        ownerId: String = "",
        landmark: Landmark? = null,
        visitedAt: String = ""
    ) : this() {
        this.ownerId = ownerId
        this.landmark = landmark
        this.visitedAt = visitedAt
    }
}