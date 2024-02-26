package ca.uwaterloo.treklogue.data.model

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class User() : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var name: String = ""
    var email: String = ""
    var emailVerified: String = "" // TODO: Use proper Date object
    var avatar: String = ""
    var journalEntries: RealmList<JournalEntry> = realmListOf()
    var badges: RealmList<Badge> = realmListOf()

    constructor(name: String = "", email: String = "") : this() {
        this.name = name
        this.email = email
    }
}