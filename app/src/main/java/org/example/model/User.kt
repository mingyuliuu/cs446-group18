package org.example.model

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId
import java.time.LocalDate

class User : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var name: String = ""
    var email: String = ""
    var emailVerified: LocalDate? = null
    var avatar: String? = ""
    var journalEntries: RealmList<JournalEntry>? = realmListOf()
    var badges: RealmList<Badge>? = realmListOf()
}