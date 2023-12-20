package com.flynaut.healthtag.model


 class SpinnerCategoryModel {
    var name: String? = null
    var id: String? = null

    constructor(name: String?, id: String?) {
        this.name = name
        this.id = id
    }

    /**
     * Pay attention here, you have to override the toString method as the
     * ArrayAdapter will reads the toString of the given object for the name
     *
     * @return contact_name
     */
    override fun toString(): String {
        return name!!
    }
}