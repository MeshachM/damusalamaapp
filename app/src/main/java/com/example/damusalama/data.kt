package com.example.damusalama

class data {
    @JvmField
    var dadd: String? = null
    @JvmField
    var spinner: String? = null
    @JvmField
    var number: String? = null

    constructor() {}
    constructor(dadd: String?, number: String?, spinner: String?) {
        this.dadd = dadd
        this.number = number
        this.spinner = spinner
    }
}