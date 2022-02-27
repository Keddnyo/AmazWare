package io.github.keddnyo.amazware.fragments.utils

class Adapter(name: String?, description: String?) :
    HashMap<String?, String?>() {

    // Object
    companion object {
        const val NAME = "name"
        const val DESCRIPTION = "firmware"
    }

    // Constructor with parameters
    init {
        super.put(NAME, name)
        super.put(DESCRIPTION, description)
    }
}