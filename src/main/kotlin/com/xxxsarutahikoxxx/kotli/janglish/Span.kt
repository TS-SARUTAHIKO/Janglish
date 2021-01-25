package com.xxxsarutahikoxxx.kotli.janglish


interface Span
class ListSpan(open : Char?, close : Char?) : Span {
    internal var _open = open
    internal var _close = close
    val open get() = _open
    val close get() = _close

    /** open-close の修飾子を変更する */
    fun setModifier( mod : Pair<Char?, Char?> ){
        _open = mod.first
        _close = mod.second
    }


    internal var _spans : MutableList<Span> = mutableListOf()
    val spans : List<Span> get() = _spans.toList()

    /** 子要素の中で[ListSpan]であるもののリストを返す */
    val listSpans get() = spans.filterIsInstance(ListSpan::class.java).toList()
    /** 子要素の中で[TextSpan]であるもののリストを返す */
    val textSpans get() = spans.filterIsInstance(TextSpan::class.java).toList()

    /** 内包する全ての[Span]を再帰的に検索してリスト化して返す */
    val allSpans : List<Span> get(){
        return spans.map {
            when(it){
                is ListSpan -> listOf(it, *it.allSpans.toTypedArray())
                is TextSpan -> listOf(it)
                else -> listOf()
            }
        }.flatten()
    }

    /**  */
    internal fun parse( resource : String, index : Int, decorations : Map<Char, Char>) : Int {
        // 現在の検査位置
        var index = index
        // テキスト開始位置
        var tStart = index

        // open-Modifier リストの作成
        val opens = decorations.keys

        while( index < resource.length ){
            when( resource[index] ){
                in opens -> {
                    // テキスト部分を切り出して保存する
                    val text = resource.substring(tStart, index)
                    if( text.isNotEmpty() )_spans.add( TextSpan(text) )

                    // 新たなSpanを生成して処理する
                    val span = ListSpan(resource[index], decorations[resource[index]]!!)
                    _spans.add(span)
                    index = span.parse(resource, index+1, decorations)
                    tStart = index
                }
                close -> {
                    // テキスト部分を切り出して保存する
                    val text = resource.substring(tStart, index)
                    if( text.isNotEmpty() )_spans.add( TextSpan(text) )

                    // 処理を終了する
                    index++
                    tStart = index
                    break
                }
                else -> { index++ }
            }
        }

        val text = resource.substring(tStart, index)
        if( text.isNotEmpty() )_spans.add( TextSpan(text) )

        return index
    }

    /**  */
    override fun toString(): String = ""+ (open?:"") + spans.joinToString("") + (close?:"")

    companion object {
        /** [parse]で用いられるデフォルトの修飾子の組み合わせ */
        var Decorations = mapOf(
                '(' to ')',
                '[' to ']',
                '{' to '}',
                '<' to '>',
                '〔' to '〕',
                '《' to '》',
                '〈' to '〉',
                '［' to '］',
                '【' to '】'
        )

        fun parse(text : String, decorations : Map<Char, Char> = Decorations) : ListSpan {
            return ListSpan(null, null).apply {
                this.parse(text, 0, decorations)
            }
        }
    }
}
class TextSpan(var text : String) : Span {
    override fun toString(): String = text
}

