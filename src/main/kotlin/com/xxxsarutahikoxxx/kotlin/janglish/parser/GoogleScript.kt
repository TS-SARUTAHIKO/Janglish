package com.xxxsarutahikoxxx.kotlin.janglish.parser

import com.xxxsarutahikoxxx.kotlin.Utilitys.out
import org.jsoup.Jsoup

object GoogleScript {
    fun toJapanese( en : String ) : String {
        var url = "https://script.google.com/macros/s/AKfycbw-YylUWN5x62zkFjsGsSd2THcJuZeFYAvArTr28-kEPxyWkxxyEAV1yg/exec"
        url += "?text=${en}&source=en&target=ja"

        return Jsoup.connect(url).get().body().text()
    }
    fun toEnglish( ja : String ) : String {
        var url = "https://script.google.com/macros/s/AKfycbw-YylUWN5x62zkFjsGsSd2THcJuZeFYAvArTr28-kEPxyWkxxyEAV1yg/exec"
        url += "?text=${ja}&source=ja&target=en"

        return Jsoup.connect(url).get().body().text()
    }
}