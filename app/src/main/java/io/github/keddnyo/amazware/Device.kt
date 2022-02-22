package io.github.keddnyo.amazware

class Device {
    fun name(name: String): String {
        return when (name) {
            "12" -> {
                "Amazfit Bip"
            } "20" -> {
                "Amazfit Bip2"
            } "24" -> {
                "Xiaomi Mi Band 4 Chinese NFC"
            } "25" -> {
                "Xiaomi Mi Smart Band 4 Global"
            } "28" -> {
                "Amazfit Bip S"
            } "29" -> {
                "Amazfit Bip S Lite"
            } "30" -> {
                "Amazfit Verge Lite Global"
            } "31" -> {
                "Mi Band 3i"
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
                "Zepp Z Chinese"
            } "57" -> {
                "Zepp E Circle Chinese"
            } "58" -> {
                "Xiaomi Mi Band 5 Chinese NFC"
            } "59" -> {
                "Xiaomi Mi Band 5 Chinese"
            } "61" -> {
                "Zepp E Square Chinese"
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
                "Zepp Z Global"
            } "77" -> {
                "Amazfit GTS 2 Chinese"
            } "78" -> {
                "Amazfit GTS 2 Global"
            } "81" -> {
                "Zepp E Circle Global"
            } "82" -> {
                "Zepp E Square Global"
            } "83" -> {
                "Amazfit T-Rex Pro Chinese"
            } "92" -> {
                "Amazfit GTS 2 Mini Global"
            } "200" -> {
                "Amazfit T-Rex Pro Global"
            } "206" -> {
                "Amazfit GTR2e Chinese"
            } "207" -> {
                "Amazfit GTS2e Chinese"
            } "209" -> {
                "Amazfit GTR2e Global"
            } "210" -> {
                "Amazfit GTS2e Global"
            } "211" -> {
                "Mi Band 6 Chinese NFC"
            } "212" -> {
                "Mi Band 6 Global"
            } else -> {
                "(Unknown - $name)"
            }
        }
    }
}