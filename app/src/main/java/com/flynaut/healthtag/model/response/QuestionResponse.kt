package com.flynaut.healthtag.model.response

data class QuestionResponse(
    val `data`: List<QuestionAnswer>,
    val message: String,
    var totalValue: Int = 0,
    val status: Int
) {
    fun getTotalResultValue() : Int{
        totalValue = 0
        for (element in data){
            totalValue +=  element.selectedAnswerValue ?: 0
        }
        return totalValue
    }

//    fun isAllQuestionAnswered(): Boolean {
//        if (data.isNullOrEmpty()) {
//            return false
//        }
//
//        return data.all { questionAnswer ->
//            questionAnswer.selectedAnswerValue != null
//        }
//    }

    fun isAllQuestionAnswered(): Boolean {
        for (element in data) {
            if (element.selectedAnswerValue == null)
                return false
        }
        return true
    }


//    fun isAllQuestionAnswered() : Boolean{
//        for( i in 0..data.size){
//            if(data[i].selectedAnswerValue == null)
//                return false
//        }
//        return true
//    }

}

data class QuestionAnswer(
    val __v: Int,
    val _id: String,
    val answers: List<Answer>,
    val category: String,
    var selectedAnswerValue: Int?,
    val question: String
)

data class Answer(
    val _id: String,
    val answer: String,
    val value: Int
)