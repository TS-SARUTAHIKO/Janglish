package com.xxxsarutahikoxxx.kotli.janglish.parser

import com.xxxsarutahikoxxx.kotlin.Utilitys.out
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

object Wiktionary {

    /**
     * Wiktionary の Related Term と Derived Term をリスト化する
     *
     * 対象は スペース( )とバー(-)を含まず、元となる単語と先頭の[presize]個の文字が同じものである
     * */
    fun getDerivedAndRaleted(spell : String, presize : Int = 3) : List<String> {
        val document: Document = Jsoup.connect("https://en.wiktionary.org/wiki/$spell").get()


        // 英語部分のインデックスを把握する
        val (open, close) = document
            .getElementsByTag("h2")
            ?.run {
                val a = firstOrNull { it.children()?.getOrNull(0)?.run { id() == "English" && className() == "mw-headline" } ?:false }
                val b = firstOrNull { it.children()?.getOrNull(0)?.run { id() != "English" && className() == "mw-headline" } ?:false }

                (a?.siblingIndex()?:Int.MAX_VALUE) to (b?.siblingIndex()?:Int.MAX_VALUE)
            }?: (Int.MAX_VALUE to Int.MAX_VALUE)


        // Derived / Related のリストを抽出する
        val dt = listOf(
                *document.getElementsByTag("h4").toTypedArray(),
                *document.getElementsByTag("h5").toTypedArray()
            )
            .firstOrNull {
                it.child(0)?.text() == "Derived terms" &&
                it.siblingIndex() in open.until(close)
            }
        val rt = listOf(
                *document.getElementsByTag("h4").toTypedArray(),
                *document.getElementsByTag("h5").toTypedArray()
            )
            .firstOrNull {
                it.child(0)?.text() == "Related terms" &&
                it.siblingIndex() in open.until(close)
            }


        // 英語の範囲のものをリスト化する
        var list = mutableListOf<String>()
        dt?.let {
            list.addAll( it.nextElementSibling().getElementsByTag("a").map { it.text() } )
        }
        rt?.let {
            list.addAll( it.nextElementSibling().getElementsByTag("a").map { it.text() } )
        }

        //
        list = list.filter { ! it.contains("[ -]".toRegex()) }.toMutableList()

        val prefix = spell.substring(0, minOf(presize, spell.length))
        list = list.filter { it.startsWith(prefix) }.toMutableList()

        //
        return list
    }

    /**
     * Wiktionary の Related Term と Derived Term を再帰的に検索して関連語のリストを構築する
     *
     * TODO : Related / Derived は一方向の関連付けなのでマイナーな spell を開始点にするとリストは不完全になる
     * */
    fun createDerivedAndRelated(spell : String) : List<String> {
        val ret = mutableSetOf<String>(spell)
        val checked = mutableSetOf<String>()

        while( (ret - checked).isNotEmpty() ){
            val spell = (ret - checked).first()

            checked.add(spell)
            val list = getDerivedAndRaleted(spell)
            ret.addAll(list)
        }

        return ret.sorted()
    }
}