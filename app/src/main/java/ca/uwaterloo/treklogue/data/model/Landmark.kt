package ca.uwaterloo.treklogue.data.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Landmark() : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var name: String = ""
    var latitude: Double = 0.0
    var longitude: Double = 0.0

    constructor(name: String, latitude: Double, longitude: Double) : this() {
        this.name = name
        this.latitude = latitude
        this.longitude = longitude
    }
}