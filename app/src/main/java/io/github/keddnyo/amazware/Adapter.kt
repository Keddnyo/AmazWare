package io.github.keddnyo.amazware

class Adapter(name: String?, firmware: String?) :
    HashMap<String?, String?>() {

    // Object
    companion object {
        const val NAME = "name"
        const val FIRMWARE = "firmware"
    }

    // Constructor with parameters
    init {
        super.put(NAME, name)
        super.put(FIRMWARE, firmware)
    }
}