package ca.uwaterloo.treklogue.data.model

data class Landmark(
    var id: String? = null,
    var name: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
)