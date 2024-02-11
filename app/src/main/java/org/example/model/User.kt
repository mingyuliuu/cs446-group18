package org.example.model

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId
import java.util.Date

class User : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var name: String = ""
    var email: String = ""
    var emailVerified: String? = null // TODO: Use proper Date object
    var avatar: String? = null
    var journalEntries: RealmList<JournalEntry>? = realmListOf()
    var badges: RealmList<Badge>? = realmListOf()
}