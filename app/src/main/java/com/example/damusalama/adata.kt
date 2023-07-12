package com.example.damusalama

class adata {
    @JvmField
    var swali: String? = null
    var jibu: String? = null
    @JvmField
    var jib: String? = null


    constructor(swali: String?, number: String?, jibu: String?, jib: String?) {
        this.swali = swali
        this.jibu = jibu
        this.jib = jib
    }

    fun getswali(): String? {
        return swali
    }

    fun setswali(swali: String?) {
        this.swali = swali
    }

    fun getjibu(): String? {
        return jibu
    }

    fun setjibu(jibu: String?) {
        this.jibu = jibu
    }

    fun getjib(): String? {
        return jib
    }

    fun setjib(jib: String?) {
        this.jib = jib
    }
}