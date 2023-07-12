package com.example.damusalama

class tmdata {
    @JvmField
    var group: String? = null
    @JvmField
    var n: String? = null
    @JvmField
    var location: String? = null
    var number: String? = null
    var latitude: String? = null
    var longitude: String? = null

    constructor() {}
    constructor(group: String?, n: String?, location: String?, number: String?, latitude: String?, longitude: String?) {
        this.group = group
        this.n = n
        this.location = location
        this.number = number
        this.latitude = latitude
        this.longitude = longitude
    }

    fun getn(): String? {
        return n
    }

    fun setn(n: String?) {
        this.n = n
    }
}