package com.xxxsarutahikoxxx.kotli.janglish.tag


private fun tag(code : String, name : String = code, sub : VocabularyTag.()->(Unit) = {}){
    val tag = VocabularyTag.make(code, null, name).first
    tag.sub()
}
private fun VocabularyTag.tag(code : String, name : String = code, sub : VocabularyTag.()->(Unit) = {}){
    val tag = VocabularyTag.make(code, this.code, name).first
    tag.sub()
}

internal fun tagTemplete(){
    tag("生活"){
        tag("用品")
        tag("食品")
        tag("服飾")
    }
    tag("社会"){
        tag("経済")
        tag("組織"){
            tag("事件")
            tag( "医療")
        }
        tag("政治")
    }
    tag("学問"){
        tag("概念"){
            tag("color", "色")
        }
        tag("地理・気候"){
            tag("地理")
            tag("気候")
        }
        tag("生物"){
            tag("植物")
            tag("哺乳類")
            tag("鳥類")
            tag("爬虫類・両生類")
            tag("昆虫")
            tag("海洋生物")
        }
        tag("科学"){

        }
        tag("数学"){

        }
        tag("暦"){
            tag("週")
            tag("月")
        }
    }
    tag("Human", "人間"){
        tag("Human (Parts)", "身体部位"){
            tag("Head (Parts)", "頭")
            tag("Hand (Parts)", "手")
            tag("Leg (Parts)", "足")
            tag("Body (Parts)", "胴体")
            tag("Organ (Parts)", "臓器")
        }
        tag("Human (Action)", "身体動作"){
            tag("Head (Action)", "頭"){
                tag("Eye (Action)", "目")
                tag("Ear (Action)", "耳")
                tag("Nose (Action)", "鼻")
            }
            tag("Hand (Action)", "手")
            tag("Leg (Action)", "足")
        }
        tag("身体状態")
        tag("身体特性")
        tag("精神動作"){
            tag("対人")
            tag("個人")
        }
        tag("精神状態")
        tag("精神特性")
    }
    tag("文法"){
        tag("前置詞")
        tag("接続詞")
        tag("助動詞")
        tag("和製英語")
    }
    tag("レベル分類"){
        tag("Weblio"){
            tag("Weblio Lv.1")
            tag("Weblio Lv.2")
            tag("Weblio Lv.3")
            tag("Weblio Lv.4")
            tag("Weblio Lv.5")
            tag("Weblio Lv.6")
            tag("Weblio Lv.7")
            tag("Weblio Lv.8")
            tag("Weblio Lv.9")
            tag("Weblio Lv.10")
            tag("Weblio Lv.11")
            tag("Weblio Lv.12")
        }
        tag("スペースアルク"){
            tag("ALC Lv.1")
            tag("ALC Lv.2")
            tag("ALC Lv.3")
            tag("ALC Lv.4")
            tag("ALC Lv.5")
            tag("ALC Lv.6")
            tag("ALC Lv.7")
            tag("ALC Lv.8")
            tag("ALC Lv.9")
            tag("ALC Lv.10")
            tag("ALC Lv.11")
            tag("ALC Lv.12")
        }
        tag("Oxford"){
            tag("3000")
            tag("5000")
        }
    }

}