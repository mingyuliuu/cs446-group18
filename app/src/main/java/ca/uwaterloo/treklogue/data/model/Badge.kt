package ca.uwaterloo.treklogue.data.model

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Badge() : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var ownerId: String = ""
    var name: String = ""
    var landmarks: RealmList<Landmark>? = realmListOf()

    constructor(name: String = "", ownerId: String = "") : this() {
        this.name = name
        this.ownerId = ownerId
    }
}