package io.github.keddnyo.amazware

class Adapter(name: String?, firmware: String?) :
    HashMap<String?, String?>() {
    companion object {
        const val NAME = "name"
        const val FIRMWARE = "firmware"
    }

    // Конструктор с параметрами
    init {
        super.put(NAME, name)
        super.put(FIRMWARE, firmware)
    }
}