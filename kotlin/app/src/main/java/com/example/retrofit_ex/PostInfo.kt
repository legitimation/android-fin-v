package com.example.retrofit_ex

import java.util.*

class PostInfo {
    private var __v = 0
    var name: String? = null
    var title: String? = null
    var content: String? = null
    private var _id: String? = null
    var namesofliked: ArrayList<String>? = null

    constructor() {}
    constructor(name: String?, title: String?, content: String?, namesofliked: ArrayList<String>?) {
        this.name = name
        this.title = title
        this.content = content
        this.namesofliked = namesofliked
    }

    fun set__v(__v: Int) {
        this.__v = __v
    }

    fun set_id(_id: String?) {
        this._id = _id
    }

    fun get__v(): Int {
        return __v
    }

    fun get_id(): String? {
        return _id
    }
}