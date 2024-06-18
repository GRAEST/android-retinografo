package br.com.graest.retinografo.data.model

enum class DialogType {
    CREATE,
    EDIT;

    companion object {
        fun stringfy(param: DialogType): String {
            return if (param == CREATE) {
                "Create"
            } else {
                "Edit"
            }
        }
    }
}
