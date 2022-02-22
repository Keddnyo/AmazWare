package io.github.keddnyo.amazware

class Device {
    fun name(name: String): String {
        return when (name) {
            "12" -> {
                "Amazfit Bip"
            } "20" -> {
                "(dongtinghu)"
            } "24" -> {
                "Xiaomi Mi Band 4 Chinese NFC"
            } "25" -> {
                "Xiaomi Mi Smart Band 4 Global"
            } "28" -> {
                "Amazfit Bip S"
            } "29" -> {
                "Amazfit Bip S Lite"
            } "30" -> {
                "Amazfit Verge Lite"
            } "31" -> {
                "(rio)"
            } "35" -> {
                "Amazfit GTR 47 mm Chinese"
            } "36" -> {
                "Amazfit GTR 47 mm Global"
            } "37" -> {
                "Amazfit GTR 42 mm Chinese"
            } "38" -> {
                "Amazfit GTR 42 mm Global"
            } "39" -> {
                "Amazfit Bip Lite"
            } "40" -> {
                "Amazfit GTS Chinese"
            } "41" -> {
                "Amazfit GTS Global"
            } "42" -> {
                "Amazfit Bip Lite"
            } "46" -> {
                "Amazfit GTR 47 mm Lite Global"
            } "50" -> {
                "Amazfit T-Rex"
            } "51" -> {
                "Amazfit GTR 12 mm SWK"
            } "52" -> {
                "Amazfit GTR 12 mm SWK Global"
            } "53" -> {
                "Amazfit X Chinese"
            } "56" -> {
                "(osprey)"
            } "57" -> {
                "(onyx)"
            } "58" -> {
                "Xiaomi Mi Band 5 Chinese NFC"
            } "59" -> {
                "Xiaomi Mi Band 5 Chinese"
            } "61" -> {
                "(corsica)"
            } "62" -> {
                "Amazfit Neo"
            } "63" -> {
                "Amazfit GTR 2 Chinese"
            } "64" -> {
                "Amazfit GTR 2 Global"
            } "67" -> {
                "Amazfit Pop Pro"
            } "68" -> {
                "Amazfit Pop"
            } "69" -> {
                "Amazfit Bip U Pro"
            } "70" -> {
                "Amazfit Bip U"
            } "71" -> {
                "Amazfit X Global"
            } "73" -> {
                "Amazfit Band 5"
            } "76" -> {
                "(ospreyw)"
            } "77" -> {
                "Amazfit GTS 2 Chinese"
            } "78" -> {
                "Amazfit GTS 2 Global"
            } "81" -> {
                "(onyxw)"
            } "82" -> {
                "(corsicaw)"
            } "83" -> {
                "Amazfit T-Rex Pro Chinese"
            } "92" -> {
                "Amazfit GTS 2 Mini Global"
            } "200" -> {
                "Amazfit T-Rex Pro Global"
            } "211" -> {
                "Mi Band 6 Chinese NFC"
            } "212" -> {
                "Mi Band 6 Global"
            } else -> {
                "Unknown"
            }
        }
    }
}